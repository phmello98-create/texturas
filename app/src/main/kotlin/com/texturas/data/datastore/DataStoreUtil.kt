package com.texturas.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "texturas_prefs")

private val KEY_HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")

suspend fun Context.hasSeenOnboarding(): Boolean {
    return dataStore.data
        .map { it[KEY_HAS_SEEN_ONBOARDING] ?: false }
        .first()
}

suspend fun Context.setHasSeenOnboarding(hasSeen: Boolean) {
    dataStore.edit { it[KEY_HAS_SEEN_ONBOARDING] = hasSeen }
}
