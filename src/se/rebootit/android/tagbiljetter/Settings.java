/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.util.*;
import android.view.View;
import android.view.View.*;
import android.widget.*;

import java.text.*;
import java.util.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */

public class Settings extends CustomActivity implements OnClickListener
{
	SharedPreferences sharedPreferences = Biljetter.getSharedPreferences();
	String[] locales = { "", "en", "sv" };
	Integer[] themes = { com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar, com.actionbarsherlock.R.style.Theme_Sherlock_Light, com.actionbarsherlock.R.style.Theme_Sherlock };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		// Set the correct header
		getSupportActionBar().setTitle(getString(R.string.Settings_header));

		((Button)findViewById(R.id.btnSave)).setOnClickListener(this);

		// The spinner for language selection
		Spinner spnLanguages = (Spinner)findViewById(R.id.spnLanguage);
		ArrayAdapter<CharSequence> adapterLanguages = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		adapterLanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLanguages.setAdapter(adapterLanguages);
		spnLanguages.setSelection(Arrays.asList(locales).indexOf(sharedPreferences.getString("locale", "")));

		// The spinner for theme selection
		Spinner spnThemes = (Spinner)findViewById(R.id.spnThemes);
		ArrayAdapter<CharSequence> adapterThemes = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_item);
		adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnThemes.setAdapter(adapterThemes);
		spnThemes.setSelection(Arrays.asList(this.themes).indexOf(sharedPreferences.getInt("theme", 0)));

		((CheckBox)findViewById(R.id.chkSilence)).setChecked(sharedPreferences.getBoolean("silencesms", false));
		((CheckBox)findViewById(R.id.chkNotification)).setChecked(sharedPreferences.getBoolean("shownotification", true));
		((CheckBox)findViewById(R.id.chkKeepScreenOn)).setChecked(sharedPreferences.getBoolean("keepscreenon", false));

		long lngLastScan = sharedPreferences.getLong("lastmessage", 0);
		String strLastScan = (lngLastScan > 0 ? new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(lngLastScan)) : getString(R.string.Settings_never));
		((TextView)findViewById(R.id.txtLastScan)).setText(getString(R.string.Settings_lastscan)+" "+strLastScan);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSave)
		{
			Editor e = sharedPreferences.edit();

			// Save the locale selection
			e.putString("locale", this.locales[((Spinner)findViewById(R.id.spnLanguage)).getSelectedItemPosition()]);
			Biljetter.setLocale(this);

			// Save the theme selection
			e.putInt("theme", this.themes[((Spinner)findViewById(R.id.spnThemes)).getSelectedItemPosition()]);

			e.putBoolean("silencesms", ((CheckBox)findViewById(R.id.chkSilence)).isChecked());
			e.putBoolean("shownotification", ((CheckBox)findViewById(R.id.chkNotification)).isChecked());
			e.putBoolean("keepscreenon", ((CheckBox)findViewById(R.id.chkKeepScreenOn)).isChecked());

			if (((CheckBox)findViewById(R.id.chkClearLastScan)).isChecked()) {
				e.remove("lastmessage");
			}

			e.commit();

			setResult(RESULT_OK, getIntent());
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// App icon in action bar clicked; go home
		if (item.getItemId() == android.R.id.home)
		{
			Intent intent = new Intent(this, TicketList.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
