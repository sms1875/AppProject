package com.example.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedPreferences  : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}


// 데이터 조회 SharedPreferences.prefs.getString("key", "token")
// 데이터 저장 SharedPreferences.prefs.setString("key", "result.token")
//출처: https://leveloper.tistory.com/133 [꾸준하게]

