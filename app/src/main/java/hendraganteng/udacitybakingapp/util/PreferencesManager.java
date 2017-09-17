package hendraganteng.udacitybakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;


/**
 * Helper for easy way accessing PreferencesManager
 *
 * @author hendrawd
 * @version 1.1
 */
public class PreferencesManager {
    private static SharedPreferences sharedPreferences;

    private PreferencesManager() {
    }

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Unimplemented yet
     * Use this if you want to give name to your SharedPreferences
     *
     * @param context  android context
     * @param prefName name of SharedPreferences
     */
    public static void init(Context context, String prefName) {
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        initIfNull(context);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void putFloat(Context context, String key, Float value) {
        initIfNull(context);
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        initIfNull(context);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static void putLong(Context context, String key, Long value) {
        initIfNull(context);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static void putString(Context context, String key, String value) {
        initIfNull(context);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void putStringSet(Context context, String key, Set<String> value) {
        initIfNull(context);
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        initIfNull(context);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static Float getFloat(Context context, String key, Float defValue) {
        initIfNull(context);
        return sharedPreferences.getFloat(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        initIfNull(context);
        return sharedPreferences.getInt(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        initIfNull(context);
        return sharedPreferences.getLong(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        initIfNull(context);
        return sharedPreferences.getString(key, defValue);
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defValue) {
        initIfNull(context);
        return sharedPreferences.getStringSet(key, defValue);
    }

    public static void remove(Context context, String key) {
        initIfNull(context);
        if (sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply();
        }
    }

    private static void initIfNull(Context context) {
        if (sharedPreferences == null)
            init(context);
    }
}