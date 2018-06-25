package br.com.avana.elivreapp.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.avana.elivreapp.R;

public class Preferences {

    public static boolean isFirstTimeSeenMapScreen(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.preference_is_map_screen_viewed), true);
    }

    public static void setMapScreenViewed(Context context){
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit.putBoolean(context.getString(R.string.preference_is_map_screen_viewed), false);
                edit.apply();
    }
    
    public static boolean isFirstTimeSeenFormScreen(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.preference_is_form_screen_viewed), true);
    }

    public static void setFormScreenViewed(Context context){
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit.putBoolean(context.getString(R.string.preference_is_form_screen_viewed), false);
                edit.apply();
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
