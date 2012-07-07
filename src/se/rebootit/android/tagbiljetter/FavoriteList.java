/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.os.*;
import android.util.*;
import android.view.View;
import android.view.View.*;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.*;

import se.rebootit.android.tagbiljetter.models.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class FavoriteList extends CustomActivity implements OnClickListener
{
	DataParser dataParser = Biljetter.getDataParser();
	ArrayList<Object> lstItems = new ArrayList<Object>();
	SuperListAdapter adapter;

	SharedPreferences sharedPreferences = Biljetter.getSharedPreferences();

	ListView lstFavorites;
	TextView txtNoFavorites;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favoritelist);

		// Set the correct header
		getSupportActionBar().setTitle(getString(R.string.Favorites_header));

		txtNoFavorites = (TextView)findViewById(R.id.nofavorites);

		adapter = new SuperListAdapter(this, 0, this.lstItems);
		lstFavorites = (ListView)findViewById(R.id.favoritelist);
		lstFavorites.setAdapter(adapter);
		lstFavorites.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> info, View v, int position, long id)
			{
				FavoriteItem item = (FavoriteItem)lstItems.get(position);

				Intent intent = new Intent(FavoriteList.this, OrderOptions.class);
				intent.putExtra("favorite", (Parcelable)item);
				startActivityForResult(intent, 0);
			}
		});

		updateList();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// Update the list with the content from an ArrayList
	public void updateList()
	{
		ArrayList<Object> list = new ArrayList<Object>();
		list.addAll(dataParser.getFavorites());

		lstItems.clear();
		lstItems.addAll(list);
		adapter.notifyDataSetChanged();

		txtNoFavorites.setVisibility(lstItems.size() == 0 ? TextView.VISIBLE : TextView.GONE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0 && resultCode == RESULT_OK) {
			finish();
		}
		updateList();
	}

	@Override
	public void onClick(View v) {
		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// App icon in action bar clicked; go home
		if (item.getItemId() == android.R.id.home)
		{
			Intent intent = new Intent(this, Order.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
