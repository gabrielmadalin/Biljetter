/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.view.View;
import android.view.View.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class About extends CustomActivity
{
	DataParser dataParser = Biljetter.getDataParser();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		// Set the correct header
		getSupportActionBar().setTitle(getString(R.string.About_header));

		TextView txtDescription = ((TextView)findViewById(R.id.txtDescription));
		txtDescription.setText(Html.fromHtml(getString(R.string.About_description)));
		txtDescription.setMovementMethod(LinkMovementMethod.getInstance());

		try
		{
			// Add the version number
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int versionNumber = pinfo.versionCode;
			String versionName = pinfo.versionName;

			((TextView)findViewById(R.id.txtVersion)).setText(versionName+" (build "+versionNumber+")");
		}
		catch (Exception e) { }

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
