package de.jstrecker.businesscard.stores

import kotlinx.coroutines.flow.Flow

interface Store {
    val getAccessToken: Flow<String>
    suspend fun saveToken(token: String)
}
