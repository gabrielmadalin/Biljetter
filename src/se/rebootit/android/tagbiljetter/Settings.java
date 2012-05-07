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

public class Settings extends SherlockActivity implements OnClickListener
{
	SharedPreferences sharedPreferences = Biljetter.getSharedPreferences();
	String[] locales = { "", "en", "sv" };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		((Button)findViewById(R.id.btnSave)).setOnClickListener(this);

		Spinner spinner = (Spinner) findViewById(R.id.spnLanguage);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(Arrays.asList(locales).indexOf(sharedPreferences.getString("locale", "")));

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

			// Save the users locale and set it
			e.putString("locale", this.locales[((Spinner)findViewById(R.id.spnLanguage)).getSelectedItemPosition()]);
			Biljetter.setLocale(this);

			e.putBoolean("silencesms", ((CheckBox)findViewById(R.id.chkSilence)).isChecked());
			e.putBoolean("shownotification", ((CheckBox)findViewById(R.id.chkNotification)).isChecked());
			e.putBoolean("keepscreenon", ((CheckBox)findViewById(R.id.chkKeepScreenOn)).isChecked());

			if (((CheckBox)findViewById(R.id.chkClearLastScan)).isChecked()) {
				e.remove("lastmessage");
			}

			e.commit();

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
