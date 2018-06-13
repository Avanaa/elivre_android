package br.com.avana.elivreapp.pref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.com.avana.elivreapp.R;

public class Preferences {

    public static boolean isTargetFirstTimeSeen(Activity activity){

        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        if (preferences == null){
            createTargetFirsTimeSeen(activity);
        }
        return preferences.getBoolean(activity.getString(R.string.preference_map_viewed), true);
    }

    private static void createTargetFirsTimeSeen(Activity activity) {

        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(activity.getString(R.string.preference_map_viewed), true)
            .apply();
    }

    public static void setTargetFirstTimeSeen(Activity activity){

        activity.getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putBoolean(activity.getString(R.string.preference_map_viewed), false)
            .apply();
    }
}
