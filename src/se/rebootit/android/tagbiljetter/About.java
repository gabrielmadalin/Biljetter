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
import android.text.method.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class About extends Activity implements OnClickListener
{
	DataParser dataParser = Biljetter.getDataParser();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		((Button)findViewById(R.id.btnWebpage)).setOnClickListener(this);
		((Button)findViewById(R.id.btnDonate)).setOnClickListener(this);
		((Button)findViewById(R.id.btnChangelog)).setOnClickListener(this);

		// Make sure the links in the text is clickable
		((TextView)findViewById(R.id.txtDescription)).setMovementMethod(LinkMovementMethod.getInstance());

		try
		{
			// Add the version number
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int versionNumber = pinfo.versionCode;
			String versionName = pinfo.versionName;

			((TextView)findViewById(R.id.txtVersion)).setText(versionName+" (build "+versionNumber+")");
		}
		catch (Exception e) { }
	}

	public void onClick(View v)
	{
		Intent intent;
		switch(v.getId())
		{
			// Open our github page in the web browser
			case R.id.btnWebpage:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/erifre/Biljetter"));
				startActivity(intent);
				break;

			// Open our flattr page in the web browser
			case R.id.btnDonate:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flattr.com/thing/371293"));
				startActivity(intent);
				break;

			// Open our Wizard
			case R.id.btnChangelog:

				Dialog dialog = new Dialog(this);
				dialog.setCancelable(true);
				dialog.setContentView(R.layout.changelog);
				dialog.setTitle(getString(R.string.About_changelog));

				TextView txtChangelog = (TextView)dialog.findViewById(R.id.txtChangelog);
				txtChangelog.setText(dataParser.readAsset("changelog.txt", this).toString());

				dialog.show();

				break;
		}
	}
}
