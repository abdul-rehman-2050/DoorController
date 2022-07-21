package com.asta.door.controller

import android.content.SharedPreferences

object AppSettings {
    private val prefs: SharedPreferences = App.self.getSharedPreferences(App.packageId, 0)

    var userUID: String
        get() = getString(PREF_USER_UID, "")
        set(value) = setString(PREF_USER_UID, value)

    var localHostURL: String
        get() = getString(PREF_LOCAL_HOST_URL, "")
        set(value) = setString(PREF_LOCAL_HOST_URL, value)

    private fun getString(key: String, defaultVal: String): String {
        return prefs.getString(key, defaultVal) ?: ""
    }

    private fun setString(key: String, value: String?) {
        if (value == null) {
            remove(key)
        } else {
            prefs.edit()
                .putString(key, value)
                .apply()
        }
    }

    private fun remove(key: String) {
        prefs.edit()
            .remove(key)
            .apply()
    }

    private const val PREF_USER_UID = "pref_user_uid"
    private const val PREF_LOCAL_HOST_URL = "pref_local_host_url"
}