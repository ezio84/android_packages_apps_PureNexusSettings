/*
 * Copyright (C) 2015 The Pure Nexus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.purenexussettings;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.purenexussettings.preferences.SeekBarPreference;

public class NavbarFragment extends PreferenceFragment
            implements OnPreferenceChangeListener  {

    private static final String CATEGORY_NAVBAR = "navigation_bar";

    private static final String NAVDIMEN = "navbar_dimen_frag";
    private static final String NAVBUTTON = "navbar_button_frag";
    private static final String LONG_PRESS_KILL_DELAY = "long_press_kill_delay";

    private Preference mNavDimen;
    private Preference mNavButton;
    private SeekBarPreference mLongpressKillDelay;

    public NavbarFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navbar_fragment);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        final PreferenceCategory navbarCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_NAVBAR);

        mNavDimen = (Preference)findPreference(NAVDIMEN);
        mNavButton = (Preference)findPreference(NAVBUTTON);

        mLongpressKillDelay = (SeekBarPreference) findPreference(LONG_PRESS_KILL_DELAY);
        int killconf = Settings.System.getInt(resolver,
                Settings.System.LONG_PRESS_KILL_DELAY, 1000);
        mLongpressKillDelay.setValue(killconf);
        mLongpressKillDelay.setOnPreferenceChangeListener(this);

        // Enable or disable NavbarImeSwitcher based on boolean: config_show_cmIMESwitcher
        boolean showCmImeSwitcher = getResources().getBoolean(
                com.android.internal.R.bool.config_show_cmIMESwitcher);
        if (!showCmImeSwitcher) {
            Preference pref = findPreference(Settings.System.STATUS_BAR_IME_SWITCHER);
            if (pref != null) {
                navbarCategory.removePreference(pref);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mLongpressKillDelay) {
            int killconf = (Integer) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.LONG_PRESS_KILL_DELAY, killconf);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen prefScreen, Preference pref) {
        if (pref == mNavDimen) {
            ((TinkerActivity)getActivity()).displaySubFrag(getString(R.string.navbardimenfrag_title));

            return true;
        }
        if (pref == mNavButton) {
            ((TinkerActivity)getActivity()).displaySubFrag(getString(R.string.navbarbuttonfrag_title));

            return true;
        }
        return false;
    }
}
