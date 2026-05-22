package de.jstrecker.businesscard.stores

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesStore(
    private val dataStore: DataStore<Preferences>,
    key: String,
    private val defaultValue: String = ""
) : Store {
    private val prefKey = stringPreferencesKey(key)

    override val getAccessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[prefKey] ?: defaultValue
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[prefKey] = token
        }
    }
}
