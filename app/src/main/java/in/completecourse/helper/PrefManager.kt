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
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.apply()
        }

    fun setLogin(isLoggedIn: Boolean) {
        editor!!.putBoolean(IS_LOGGED_IN, isLoggedIn)
        // commit changes
        editor!!.commit()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGGED_IN, false)

    var userClass: String?
        get() = pref.getString(USER_CLASS, "")
        set(userClass) {
            editor!!.putString(USER_CLASS, userClass)
            editor!!.commit()
        }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "vidya_complete_course"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USER_CLASS = "user_class"
    }

    init {
        // shared pref mode
        val PRIVATE_MODE = 0
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }
}