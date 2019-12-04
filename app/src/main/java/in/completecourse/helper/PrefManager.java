package in.completecourse.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private final SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "vidya_complete_course";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_CLASS = "user_class";

    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setUserClass(String userClass){
        editor.putString(USER_CLASS, userClass);
        editor.commit();
    }

    public String getUserClass(){
        return pref.getString(USER_CLASS, "");
    }
}
