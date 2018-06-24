package br.com.avana.elivreapp.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import br.com.avana.elivreapp.R;

public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_time);
        //addPreferencesFromResource(R.xml.pref_map);
    }
}
