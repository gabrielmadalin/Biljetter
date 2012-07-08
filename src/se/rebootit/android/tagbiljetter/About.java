/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.util.*;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.method.*;
import android.view.View;
import android.view.View.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class About extends CustomActivity implements OnClickListener
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

		((ImageButton)findViewById(R.id.btnFlattr)).setOnClickListener(this);
		((ImageButton)findViewById(R.id.btnPayPal)).setOnClickListener(this);
		((ImageButton)findViewById(R.id.btnBitcoin)).setOnClickListener(this);

		try
		{
			// Add the version number
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int versionNumber = pinfo.versionCode;
			String versionName = pinfo.versionName;

			((TextView)findViewById(R.id.txtVersion)).setText("Version "+versionName+" (build "+versionNumber+")");
		}
		catch (Exception e) { }

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressWarnings("deprecation")
	public void onClick(View v)
	{
		Intent intent;
		switch(v.getId())
		{
			case R.id.btnFlattr:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flattr.com/thing/371293"));
				startActivity(intent);
				break;

			case R.id.btnPayPal:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=erik@fredriksen.se&item_name=Biljetter%20-%20Donation&currency_code=EUR"));
				startActivity(intent);
				break;

			case R.id.btnBitcoin:
				String strAddress = "1HmJMPH728Y6CQUFHeR8VDZxS1FCnMHipb";
				if (isIntentAvailable(this, "bitcoin:"+strAddress)) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("bitcoin:"+strAddress));
					startActivity(intent);
				}
				else
				{
					ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
					clipboard.setText(strAddress);

					Toast.makeText(this, getString(R.string.About_addresscopied), Toast.LENGTH_LONG).show();
				}
				break;
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

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}


}
