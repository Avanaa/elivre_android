package br.com.avana.elivreapp.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String MAP_VIEWED = "br.com.avana.elivreapp.MAP_VIEWED";

    public static boolean isTargetFirstTimeSeen(Activity activity){

        SharedPreferences preferences = activity.getSharedPreferences(MAP_VIEWED, Context.MODE_PRIVATE);
        if (preferences == null){
            createTargetFirsTimeSeen(activity);
        }
        return preferences.getBoolean(MAP_VIEWED, true);
    }

    private static void createTargetFirsTimeSeen(Activity activity) {

        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(MAP_VIEWED, true)
            .apply();
    }

    public static void setTargetFirstTimeSeen(Activity activity){

        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(MAP_VIEWED, false)
            .apply();
    }
}
