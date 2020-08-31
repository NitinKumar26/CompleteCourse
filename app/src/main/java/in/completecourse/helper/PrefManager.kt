package `in`.completecourse.helper

import `in`.completecourse.LoginActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private val pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor = pref.edit()
        editor!!.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor?.apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }

    fun saveUser(name: String?, userId: String?){
        editor = pref.edit()
        editor!!.putString(USERNAME, name)
        editor!!.putString(USERID, userId)
        editor?.apply()
    }

    fun getUserId(): String?{
        return pref.getString(USERID, "")
    }

    // --Commented out by Inspection START (2/11/19 12:17 AM):
    //    public String getUserClass(){
    //        return pref.getString(USER_CLASS, "");
    //    }
    // --Commented out by Inspection STOP (2/11/19 12:17 AM)

    fun logoutUser(context: Context) {
        editor = pref.edit()
        // Clearing all data from Shared Preferences
        editor?.clear()
        editor?.apply()

        // After logout redirect user to Login Activity
        val i = Intent(context, LoginActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        // Staring Login Activity
        context.startActivity(i)
    }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "vidya_complete_course"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val USERNAME = "username"
        private const val USERID = "userid"
    }

    init {
        // shared pref mode
        val privateMode = 0
        pref = context.getSharedPreferences(PREF_NAME, privateMode)
    }

}