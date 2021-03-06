/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter.contact;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import se.rebootit.android.tagbiljetter.*;
import se.rebootit.android.tagbiljetter.models.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */

public class CompanyListAdapter extends BaseAdapter
{
	private final List<TransportCompany> lstCompanies;
	private final Context context;
 
	public CompanyListAdapter(List<TransportCompany> lstCompanies, Context context) {
		this.lstCompanies = lstCompanies;
		this.context = context;
	}
 
	public int getCount() {
		return lstCompanies.size();
	}
 
	public TransportCompany getItem(int position) {
		return lstCompanies.get(position);
	}
 
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout itemLayout;
		TransportCompany transportCompany = lstCompanies.get(position);

		itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.companylist_item, parent, false);
		
		if (transportCompany.getEmail() != null && !"".equals(transportCompany.getEmail()))
		{
			ImageView imgLogo = (ImageView)itemLayout.findViewById(R.id.companylogo);
			TextView txtName = (TextView)itemLayout.findViewById(R.id.companyname);

			int logo = context.getResources().getIdentifier(transportCompany.getLogo() == null ? "nologo" : transportCompany.getLogo(), "drawable","se.rebootit.android.tagbiljetter");
			imgLogo.setImageResource(logo);

			int logobg = context.getResources().getIdentifier(transportCompany.getLogo()+"_bg", "drawable","se.rebootit.android.tagbiljetter");
			itemLayout.setBackgroundResource(logobg == 0 ? R.drawable.header_background : logobg);

			txtName.setTextColor(Color.parseColor(transportCompany.getTextColor()));
			txtName.setText(transportCompany.getName());

			return itemLayout;
		}
		else
		{
			itemLayout = new LinearLayout(Biljetter.getContext());
			itemLayout.setVisibility(LinearLayout.GONE);
			return itemLayout;
		}
	}
}
