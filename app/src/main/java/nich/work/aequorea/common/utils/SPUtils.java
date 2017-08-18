package nich.work.aequorea.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import nich.work.aequorea.Aequorea;

public class SPUtils {
    private static SharedPreferences mSp;
    
    private static SharedPreferences getSp() {
        if (mSp == null) {
            mSp = Aequorea.getApp().getSharedPreferences("Aequorea", Context.MODE_PRIVATE);
        }
        return mSp;
    }
    
    public static void setString(String key, String value) {
        getSp().edit().putString(key, value).apply();
    }
    
    public static String getString(String key) {
        return getSp().getString(key, "");
    }
    
    public static void setInt(String key, int value) {
        getSp().edit().putInt(key, value).apply();
    }
    
    public static int getInt(String key) {
        return getSp().getInt(key, 0);
    }
}
