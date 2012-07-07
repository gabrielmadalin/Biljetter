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
	FavoriteListAdapter adapter;

	SharedPreferences sharedPreferences = Biljetter.getSharedPreferences();

	ArrayList<Object> lstItems = new ArrayList<Object>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favoritelist);

		adapter = new FavoriteListAdapter(this, R.layout.favoritelist_item, lstItems);

		ListView lstFavorites = (ListView)findViewById(R.id.favoritelist);
		lstFavorites.setAdapter(adapter);
		lstFavorites.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> info, View v, int position, long id)
			{
				FavoriteItem item = (FavoriteItem)lstItems.get(position);

				Intent intent = new Intent(FavoriteList.this, OrderOptions.class);
				intent.putExtra("transportcompany", (Parcelable)item.getTransportCompany());
				intent.putExtra("transportarea", (Parcelable)item.getTransportArea());
				intent.putExtra("tickettype", (Parcelable)item.getTicketType());
				startActivityForResult(intent, 0);
			}
		});

		ArrayList<Object> foo = new ArrayList<Object>();
		foo.addAll(dataParser.getFavorites());
		updateList(foo);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// Update the list with the content from an ArrayList
	public void updateList(ArrayList<Object> list)
	{
		lstItems.clear();
		lstItems.addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0 && resultCode == RESULT_OK) {
			finish();
		}
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
