package br.app.saidabet.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import br.app.saidabet.SaiDaBetApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Best-effort blocker: when a blocked package comes to foreground,
 * shows a Toast and tries to go Home (GLOBAL_ACTION_HOME).
 * Full reliable blocking is hard on modern Android; this is MVP stub.
 */
class BetBlockAccessibilityService : AccessibilityService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var lastBlockedPkg: String? = null
    private var lastBlockAt = 0L

    private var cachedPackages: Set<String> = emptySet()
    private var cachedDomains: Set<String> = emptySet()

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "Accessibility service connected")
        refreshCache()
        // refresh every 30s
        mainHandler.post(object : Runnable {
            override fun run() {
                refreshCache()
                mainHandler.postDelayed(this, 30_000)
            }
        })
    }

    private fun refreshCache() {
        scope.launch {
            try {
                val prefs = SaiDaBetApp.instance.prefs
                cachedPackages = prefs.enabledPackageNames()
                cachedDomains = prefs.enabledDomains()
                Log.d(TAG, "Cache: ${cachedPackages.size} pkgs, ${cachedDomains.size} domains")
            } catch (e: Exception) {
                Log.w(TAG, "refreshCache failed", e)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) return

        val pkg = event.packageName?.toString() ?: return
        if (pkg == packageName) return

        val now = System.currentTimeMillis()
        if (pkg == lastBlockedPkg && now - lastBlockAt < 2500) return

        val blockedByPackage = pkg in cachedPackages
        val blockedByDomain = if (!blockedByPackage) {
            detectDomainHit(event)
        } else null

        if (blockedByPackage || blockedByDomain != null) {
            lastBlockedPkg = pkg
            lastBlockAt = now
            val label = blockedByDomain ?: pkg
            Log.i(TAG, "Blocked foreground: $label")
            mainHandler.post {
                Toast.makeText(
                    this,
                    "Sai da Bet: isso é uma bet. Respira. Você consegue. 💚",
                    Toast.LENGTH_LONG
                ).show()
                // Best-effort: leave the app
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
        }
    }

    private fun detectDomainHit(event: AccessibilityEvent): String? {
        val textBlob = buildString {
            event.text?.forEach { append(it).append(' ') }
            event.contentDescription?.let { append(it).append(' ') }
            try {
                val root = rootInActiveWindow ?: return@buildString
                collectText(root, this, 0)
            } catch (_: Exception) { }
        }.lowercase()

        if (textBlob.isBlank()) return null
        return cachedDomains.firstOrNull { domain ->
            textBlob.contains(domain.lowercase())
        }
    }

    private fun collectText(
        node: android.view.accessibility.AccessibilityNodeInfo,
        out: StringBuilder,
        depth: Int
    ) {
        if (depth > 4) return
        node.text?.let { out.append(it).append(' ') }
        node.contentDescription?.let { out.append(it).append(' ') }
        // URL bars often in contentDescription / text of EditText
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            collectText(child, out, depth + 1)
            child.recycle()
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility interrupted")
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "SaiDaBetA11y"

        fun isEnabled(context: android.content.Context): Boolean {
            val expected = "${context.packageName}/${BetBlockAccessibilityService::class.java.canonicalName}"
            val enabled = android.provider.Settings.Secure.getString(
                context.contentResolver,
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            return enabled.split(':').any { it.equals(expected, ignoreCase = true) }
        }

        fun openAccessibilitySettings(context: android.content.Context) {
            val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
