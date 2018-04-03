package org.bitman.ay27.view.main;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import org.bitman.ay27.R;
import org.bitman.ay27.PickerWidget.preference_support.PreferenceFragment;
import org.bitman.ay27.PickerWidget.preference_support.PreferenceManagerCompat;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-27.
 */
public class Preferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        final ListPreference chooseEditor = (ListPreference) findPreference("preference_editor_list");
        chooseEditor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("preference", "" + newValue);
                String chooseItem = (String) newValue;
                SharedPreferences.Editor editor = preference.getEditor();
                String[] items = getResources().getStringArray(R.array.EditorChoose);
                for (int i = 0; i < items.length; i++) {
                    if (chooseItem.equals(items[i]))
                        editor.putInt("editor", i);
                }
                editor.commit();
                return true;
            }
        });
    }
}
