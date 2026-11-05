package br.app.saidabet.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "saidabet_prefs")

class PrefsRepository(private val context: Context) {

    private object Keys {
        val onboardingDone = booleanPreferencesKey("onboarding_done")
        val cleanStartMs = longPreferencesKey("clean_start_ms")
        val dailySpend = floatPreferencesKey("daily_spend_reais")
        val disabledBlocks = stringSetPreferencesKey("disabled_block_ids")
        val supportPhone = stringPreferencesKey("support_phone")
        val supportName = stringPreferencesKey("support_name")
        val trialStartMs = longPreferencesKey("trial_start_ms")
        val unlocked = booleanPreferencesKey("local_unlocked")
        val paywallSeen = booleanPreferencesKey("paywall_seen")
    }

    val onboardingDone: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.onboardingDone] ?: false }

    val cleanStartMs: Flow<Long> =
        context.dataStore.data.map { it[Keys.cleanStartMs] ?: System.currentTimeMillis() }

    val dailySpend: Flow<Float> =
        context.dataStore.data.map { it[Keys.dailySpend] ?: 50f }

    val disabledBlockIds: Flow<Set<String>> =
        context.dataStore.data.map { it[Keys.disabledBlocks] ?: emptySet() }

    val supportPhone: Flow<String> =
        context.dataStore.data.map { it[Keys.supportPhone] ?: "" }

    val supportName: Flow<String> =
        context.dataStore.data.map { it[Keys.supportName] ?: "" }

    val unlocked: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.unlocked] ?: false }

    val trialStartMs: Flow<Long> =
        context.dataStore.data.map { it[Keys.trialStartMs] ?: 0L }

    suspend fun setOnboardingDone(done: Boolean = true) {
        context.dataStore.edit { prefs ->
            prefs[Keys.onboardingDone] = done
            if (prefs[Keys.cleanStartMs] == null) {
                prefs[Keys.cleanStartMs] = System.currentTimeMillis()
            }
            if (prefs[Keys.trialStartMs] == null || prefs[Keys.trialStartMs] == 0L) {
                prefs[Keys.trialStartMs] = System.currentTimeMillis()
            }
        }
    }

    suspend fun resetStreak() {
        context.dataStore.edit { it[Keys.cleanStartMs] = System.currentTimeMillis() }
    }

    suspend fun setDailySpend(value: Float) {
        context.dataStore.edit { it[Keys.dailySpend] = value.coerceAtLeast(0f) }
    }

    suspend fun setBlockEnabled(id: String, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.disabledBlocks]?.toMutableSet() ?: mutableSetOf()
            if (enabled) current.remove(id) else current.add(id)
            prefs[Keys.disabledBlocks] = current
        }
    }

    suspend fun setSupportContact(name: String, phone: String) {
        context.dataStore.edit {
            it[Keys.supportName] = name
            it[Keys.supportPhone] = phone.filter { ch -> ch.isDigit() || ch == '+' }
        }
    }

    suspend fun setUnlocked(value: Boolean) {
        context.dataStore.edit { it[Keys.unlocked] = value }
    }

    suspend fun ensureTrialStarted() {
        context.dataStore.edit { prefs ->
            if ((prefs[Keys.trialStartMs] ?: 0L) == 0L) {
                prefs[Keys.trialStartMs] = System.currentTimeMillis()
            }
        }
    }

    suspend fun daysClean(): Long {
        val start = cleanStartMs.first()
        val diff = System.currentTimeMillis() - start
        return TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
    }

    suspend fun isTrialActive(): Boolean {
        if (unlocked.first()) return true
        val start = trialStartMs.first()
        if (start == 0L) return true
        val days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - start)
        return days < 7
    }

    suspend fun trialDaysLeft(): Int {
        if (unlocked.first()) return 999
        val start = trialStartMs.first().takeIf { it > 0 } ?: System.currentTimeMillis()
        val days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - start)
        return (7 - days).toInt().coerceAtLeast(0)
    }

    /** Snapshot for AccessibilityService (sync-ish via runBlocking at call site). */
    suspend fun enabledPackageNames(): Set<String> {
        val disabled = disabledBlockIds.first()
        return DefaultBlockTargets.all
            .filter { it.id !in disabled }
            .flatMap { it.packageNames }
            .toSet()
    }

    suspend fun enabledDomains(): Set<String> {
        val disabled = disabledBlockIds.first()
        return DefaultBlockTargets.all
            .filter { it.id !in disabled }
            .flatMap { it.domains }
            .map { it.lowercase() }
            .toSet()
    }
}
