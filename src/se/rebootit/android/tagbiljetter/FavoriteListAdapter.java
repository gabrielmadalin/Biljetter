/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import se.rebootit.android.tagbiljetter.models.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */

public class FavoriteListAdapter extends ArrayAdapter
{
	private final Context context;
	private List<Object> lstItems;
	private LayoutInflater factory;

    public FavoriteListAdapter(Context context, int textViewResourceId, ArrayList<Object> lstItems)
    {
        super(context, textViewResourceId, lstItems);
        this.context = context;
        this.lstItems = lstItems;
		this.factory = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	public int getCount() {
		return lstItems.size();
	}

	public Object getItem(int position) {
		return lstItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout itemLayout = (LinearLayout)factory.inflate(R.layout.favoritelist_item, null);
		View layout = null;
		Object object = lstItems.get(position);

		if (object instanceof FavoriteItem)
		{
			FavoriteItem item = (FavoriteItem)object;
			TransportCompany transportCompany = item.getTransportCompany();

			layout = (RelativeLayout)itemLayout.findViewById(R.id.favoriteitem);

			TextView txtHeader = (TextView)layout.findViewById(R.id.header);
			TextView txtDescription = (TextView)layout.findViewById(R.id.description);
			ImageView imgLogo = (ImageView)itemLayout.findViewById(R.id.logo);

			int logo = context.getResources().getIdentifier(transportCompany.getLogo() == null ? "nologo" : transportCompany.getLogo(), "drawable", "se.rebootit.android.tagbiljetter");
			imgLogo.setImageResource(logo);

			int logobg = context.getResources().getIdentifier(transportCompany.getLogo()+"_bg", "drawable", "se.rebootit.android.tagbiljetter");
			layout.setBackgroundResource(logobg == 0 ? R.drawable.header_background : logobg);

			txtHeader.setTextColor(Color.parseColor(transportCompany.getTextColor()));
			txtDescription.setTextColor(Color.parseColor(transportCompany.getTextColor()));

			txtHeader.setText(transportCompany.getName());
			txtDescription.setText(item.getTransportArea().getName()+", "+item.getTicketType().getName());

			itemLayout.removeAllViews();
			itemLayout.addView(layout);
		}
		else if (object instanceof String)
		{
			layout = (LinearLayout)itemLayout.findViewById(R.id.string);
			TextView txtHeader = (TextView)layout.findViewById(R.id.header);
			txtHeader.setText((String)object);
			itemLayout.removeAllViews();
			itemLayout.addView(layout);
		}

		// Give even rows a background color
		if (position % 2 == 1) {
			itemLayout.setBackgroundColor(0x30558cd0);
		}

		return itemLayout;
	}

	public boolean isEnabled(int position) {
		return (lstItems.get(position) instanceof FavoriteItem ? true : false);
	}
}
