package br.com.avana.elivreapp.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.com.avana.elivreapp.R;

public class PreferenceManager {

    public static boolean isTargetFirstTimeSeen(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_is_first_time_seen),Context.MODE_PRIVATE);
        return preferences.getBoolean(context.getString(R.string.preference_is_first_time_seen), true);
    }

    public static void setTargetFirstTimeSeen(Activity activity){

        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(activity.getString(R.string.preference_is_first_time_seen), false)
            .apply();
    }

    public static boolean isMapThemeDark(Context context){
        return context.getSharedPreferences(context.getString(R.string.preference_map_theme_dark), Context.MODE_PRIVATE)
                .getBoolean(context.getString(R.string.preference_map_theme_dark), false);
    }

    public static void setMapThemeDark(Context context, boolean mode){
         context.getSharedPreferences(context.getString(R.string.preference_map_theme_dark), Context.MODE_PRIVATE)
                 .edit()
                 .putBoolean(context.getString(R.string.preference_map_theme_dark), mode)
                 .apply();
    }

    public static int getTimeOcurrenceInterval(Context context){
        return context.getSharedPreferences(context.getString(R.string.preference_time_limit_ocurrences), Context.MODE_PRIVATE)
                .getInt(context.getString(R.string.preference_time_limit_ocurrences), 24);
    }
}
