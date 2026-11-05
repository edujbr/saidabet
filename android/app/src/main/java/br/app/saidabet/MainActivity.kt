package br.app.saidabet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.app.saidabet.data.DefaultBlockTargets
import br.app.saidabet.service.BetBlockAccessibilityService
import br.app.saidabet.ui.blocklist.BlocklistScreen
import br.app.saidabet.ui.emergency.EmergencyScreen
import br.app.saidabet.ui.home.HomeScreen
import br.app.saidabet.ui.onboarding.OnboardingScreen
import br.app.saidabet.ui.paywall.PaywallScreen
import br.app.saidabet.ui.theme.SaiDaBetTheme
import br.app.saidabet.ui.theme.SdBg
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaiDaBetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = SdBg
                ) { inner ->
                    SaiDaBetNav(Modifier.padding(inner))
                }
            }
        }
    }
}

private object Routes {
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Blocklist = "blocklist"
    const val Emergency = "emergency"
    const val Paywall = "paywall"
}

@Composable
private fun SaiDaBetNav(modifier: Modifier = Modifier) {
    val app = SaiDaBetApp.instance
    val prefs = app.prefs
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val nav = rememberNavController()

    val onboardingDone by prefs.onboardingDone.collectAsState(initial = false)
    val cleanStart by prefs.cleanStartMs.collectAsState(initial = System.currentTimeMillis())
    val dailySpend by prefs.dailySpend.collectAsState(initial = 50f)
    val disabled by prefs.disabledBlockIds.collectAsState(initial = emptySet())
    val supportName by prefs.supportName.collectAsState(initial = "")
    val supportPhone by prefs.supportPhone.collectAsState(initial = "")
    val unlocked by prefs.unlocked.collectAsState(initial = false)
    val trialStart by prefs.trialStartMs.collectAsState(initial = 0L)

    var a11yOn by remember { mutableStateOf(BetBlockAccessibilityService.isEnabled(context)) }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                a11yOn = BetBlockAccessibilityService.isEnabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
    }

    val daysClean = ((System.currentTimeMillis() - cleanStart) / (1000L * 60 * 60 * 24)).coerceAtLeast(0)
    val moneySaved = daysClean * dailySpend
    val trialDaysLeft = run {
        if (unlocked) 999
        else {
            val start = if (trialStart > 0) trialStart else System.currentTimeMillis()
            val days = ((System.currentTimeMillis() - start) / (1000L * 60 * 60 * 24))
            (7 - days).toInt().coerceAtLeast(0)
        }
    }

    val startDest = if (onboardingDone) Routes.Home else Routes.Onboarding

    NavHost(
        navController = nav,
        startDestination = startDest,
        modifier = modifier
    ) {
        composable(Routes.Onboarding) {
            OnboardingScreen(
                accessibilityEnabled = a11yOn,
                onOpenAccessibility = { BetBlockAccessibilityService.openAccessibilitySettings(context) },
                onFinished = {
                    scope.launch {
                        prefs.setOnboardingDone(true)
                        prefs.ensureTrialStarted()
                        nav.navigate(Routes.Home) {
                            popUpTo(Routes.Onboarding) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Routes.Home) {
            HomeScreen(
                daysClean = daysClean,
                dailySpend = dailySpend,
                moneySaved = moneySaved,
                accessibilityOn = a11yOn,
                trialDaysLeft = trialDaysLeft,
                unlocked = unlocked,
                onDailySpendChange = { v -> scope.launch { prefs.setDailySpend(v) } },
                onResetStreak = { scope.launch { prefs.resetStreak() } },
                onOpenAccessibility = { BetBlockAccessibilityService.openAccessibilitySettings(context) },
                onOpenBlocklist = { nav.navigate(Routes.Blocklist) },
                onOpenEmergency = { nav.navigate(Routes.Emergency) },
                onOpenPaywall = { nav.navigate(Routes.Paywall) }
            )
        }
        composable(Routes.Blocklist) {
            BlocklistScreen(
                targets = DefaultBlockTargets.all,
                disabledIds = disabled,
                onToggle = { id, enabled -> scope.launch { prefs.setBlockEnabled(id, enabled) } },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.Emergency) {
            EmergencyScreen(
                supportName = supportName,
                supportPhone = supportPhone,
                onSaveContact = { n, p -> scope.launch { prefs.setSupportContact(n, p) } },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.Paywall) {
            PaywallScreen(
                trialDaysLeft = trialDaysLeft,
                unlocked = unlocked,
                onUnlockLocal = { scope.launch { prefs.setUnlocked(true) } },
                onBack = { nav.popBackStack() }
            )
        }
    }
}
