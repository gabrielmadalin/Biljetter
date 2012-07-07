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

public class CustomActivity extends SherlockActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		// Set the app locale based on user settings
		Biljetter.setLocale(this);
		
		super.setTheme(Biljetter.getSharedPreferences().getInt("theme", 0));
		super.onCreate(savedInstanceState);
	}
}
