package com.dmc.goyiyi.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.dataStore by preferencesDataStore("user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH = stringPreferencesKey("refresh")
        val USER_ID = stringPreferencesKey("user_id")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(refresh: String) {
        context.dataStore.edit { prefs ->
            prefs[REFRESH] = refresh
        }
    }

    suspend fun saveUserId(uid: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = uid
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[TOKEN] }.first()
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.map { it[USER_ID] }.first()
    }


}
