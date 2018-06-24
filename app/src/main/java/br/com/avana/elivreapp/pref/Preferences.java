package br.com.avana.elivreapp.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.avana.elivreapp.R;

public class Preferences {

    public static boolean isTargetFirstTimeSeen(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.preference_is_first_time_seen), true);
    }

    public static void setTargetFirstTimeSeen(Context context){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(context.getString(R.string.preference_is_first_time_seen), false)
                .apply();
    }

    public static boolean isMapThemeDark(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.preference_map_theme_dark), false);
    }

    public static int getTimeOcurrenceInterval(Context context){
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.preference_time_limit_ocurrences), "24"));
    }
}
