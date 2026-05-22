package de.jstrecker.businesscard.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

private lateinit var appContext: Context

fun initAndroidContext(context: Context) {
    appContext = context
}

actual fun createDataStore(name: String): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.create {
        appContext.preferencesDataStoreFile(name)
    }
}
