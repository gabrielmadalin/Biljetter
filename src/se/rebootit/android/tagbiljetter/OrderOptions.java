/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.telephony.*;
import android.util.*;
import android.view.Gravity;
import android.view.View;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import se.rebootit.android.tagbiljetter.models.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */

public class OrderOptions extends CustomActivity implements OnClickListener, OnItemSelectedListener
{
	DataParser dataParser = Biljetter.getDataParser();

	FavoriteItem favoriteItem;
	TransportCompany transportCompany;
	TransportArea transportArea;
	TicketType ticketType;

	List<TransportArea> lstAreas;
	List<TicketType> lstTypes;

	String strNumber, strMessage;

	TextView txtAreaDescription, txtTypeDescription;
	Spinner spnArea, spnType;
	CheckBox chkFavorite;
	Button btnSend;

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int id)
		{
			if (id == DialogInterface.BUTTON_POSITIVE)
			{
				Toast.makeText(Biljetter.getContext(), getString(R.string.OrderOptions_sending), Toast.LENGTH_LONG).show();

				//~ SmsManager sm = SmsManager.getDefault();
				//~ sm.sendTextMessage(strNumber, null, strMessage, null, null);

				setResult(RESULT_OK, getIntent());

				finish();
			}
			else if (id == DialogInterface.BUTTON_NEGATIVE) {
				Toast.makeText(Biljetter.getContext(), getString(R.string.OrderOptions_interrupted), Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderoptions);

		Intent intent = getIntent();
		this.favoriteItem = (FavoriteItem)intent.getParcelableExtra("favorite");

		if (this.favoriteItem == null) {
			this.transportCompany = (TransportCompany)intent.getParcelableExtra("transportcompany");
		}
		else
		{
			this.transportCompany = (TransportCompany)this.favoriteItem.getTransportCompany();
			this.transportArea = (TransportArea)this.favoriteItem.getTransportArea();
			this.ticketType = (TicketType)this.favoriteItem.getTicketType();
		}

		LinearLayout layoutHeader = (LinearLayout)findViewById(R.id.header);
		TextView txtCompanyname = (TextView)findViewById(R.id.companyname);
		ImageView imgCompanyLogo = (ImageView)findViewById(R.id.companylogo);
		txtAreaDescription = (TextView)findViewById(R.id.txtAreaDescription);
		txtTypeDescription = (TextView)findViewById(R.id.txtTypeDescription);
		chkFavorite = (CheckBox)findViewById(R.id.chkFavorite);
		btnSend = (Button)findViewById(R.id.btnSend);

		int logo = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo() == null ? "nologo" : transportCompany.getLogo(), "drawable", "se.rebootit.android.tagbiljetter");
		imgCompanyLogo.setImageResource(logo);

		int logobg = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo()+"_bg", "drawable", "se.rebootit.android.tagbiljetter");
		layoutHeader.setBackgroundResource(logobg == 0 ? R.drawable.header_background : logobg);

		txtCompanyname.setTextColor(Color.parseColor(transportCompany.getTextColor()));
		txtCompanyname.setText(transportCompany.getName());

		spnArea = (Spinner)findViewById(R.id.spnArea);
		ArrayAdapter<CharSequence> adapterArea = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.lstAreas = transportCompany.getTransportAreas();
		for (TransportArea area : this.lstAreas) {
			adapterArea.add(area.getName());
		}
		spnArea.setAdapter(adapterArea);

		spnType = (Spinner)findViewById(R.id.spnType);
		ArrayAdapter<CharSequence> adapterType = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.lstTypes = transportCompany.getTicketTypes();
		for (TicketType type : this.lstTypes) {
			adapterType.add(type.getName());
		}
		spnType.setAdapter(adapterType);

		btnSend.setOnClickListener(this);

		spnArea.setOnItemSelectedListener(this);
		spnType.setOnItemSelectedListener(this);

		// Set values to match favorite
		if (this.transportArea != null && this.ticketType != null) {
			spnArea.setSelection(adapterArea.getPosition(this.transportArea.getName()));
			spnType.setSelection(adapterType.getPosition(this.ticketType.getName()));

			spnArea.setEnabled(false);
			spnType.setEnabled(false);

			chkFavorite.setChecked(true);
		}

		// Respond to favorite checkbox
		chkFavorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					dataParser.addFavorite(getFavorite());
					spnArea.setEnabled(false);
					spnType.setEnabled(false);
					Toast.makeText(Biljetter.getContext(), getString(R.string.Favorites_added), Toast.LENGTH_SHORT).show();
				}
				else
				{
					dataParser.removeFavorite(getFavorite());
					spnArea.setEnabled(true);
					spnType.setEnabled(true);
					Toast.makeText(Biljetter.getContext(), getString(R.string.Favorites_removed), Toast.LENGTH_SHORT).show();
				}
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public FavoriteItem getFavorite()
	{
		TransportArea transportArea = this.lstAreas.get(((Spinner)findViewById(R.id.spnArea)).getSelectedItemPosition());
		TicketType ticketType = this.lstTypes.get(((Spinner)findViewById(R.id.spnType)).getSelectedItemPosition());

		return new FavoriteItem(transportCompany, transportArea, ticketType);
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSend)
		{
			TransportArea transportArea = this.lstAreas.get(((Spinner)findViewById(R.id.spnArea)).getSelectedItemPosition());
			TicketType ticketType = this.lstTypes.get(((Spinner)findViewById(R.id.spnType)).getSelectedItemPosition());

			this.strNumber = transportCompany.getPhoneNumber();
			this.strMessage = transportCompany.getMessage(transportArea, ticketType);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.OrderOptions_confirmSendTitle));
			builder.setMessage(getString(R.string.OrderOptions_confirmSendMessage).replace("%message%", strMessage).replace("%number%", strNumber));
			builder.setPositiveButton(getString(R.string.yes), dialogClickListener);
			builder.setNegativeButton(getString(R.string.no), dialogClickListener);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.show();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		String description;
		if (parent.getId() == R.id.spnArea)
		{
			description = this.lstAreas.get(pos).getDescription();
			if ("".equals(description) || description == null) {
				txtAreaDescription.setVisibility(TextView.GONE);
			}
			else {
				txtAreaDescription.setText(description);
				txtAreaDescription.setVisibility(TextView.VISIBLE);
			}
		}
		else if (parent.getId() == R.id.spnType)
		{
			description = this.lstTypes.get(pos).getDescription();
			if ("".equals(description) || description == null) {
				txtTypeDescription.setVisibility(TextView.GONE);
			}
			else {
				txtTypeDescription.setText(description);
				txtTypeDescription.setVisibility(TextView.VISIBLE);
			}
		}
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

	public void onNothingSelected(AdapterView parent) {
		// Do nothing.
	}
}
