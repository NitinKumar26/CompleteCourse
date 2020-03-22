package `in`.completecourse.helper

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor = pref.edit()
            editor!!.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor?.apply()
        }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "vidya_complete_course"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }

    init {
        // shared pref mode
        val privateMode = 0
        pref = context.getSharedPreferences(PREF_NAME, privateMode)
    }

}