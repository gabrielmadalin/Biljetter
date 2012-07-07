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
import android.util.*;
import android.view.View;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.telephony.*;

import se.rebootit.android.tagbiljetter.models.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */

public class OrderOptions extends CustomActivity implements OnClickListener, OnItemSelectedListener
{
	DataParser dataParser = Biljetter.getDataParser();

	TransportCompany transportCompany;
	TransportArea transportArea;
	TicketType ticketType;

	List<TransportArea> areas;
	List<TicketType> types;

	String number;
	String message;

	TextView txtAreaDescription, txtTypeDescription;
	CheckBox chkFavorite;

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (which == DialogInterface.BUTTON_POSITIVE)
			{
				Toast.makeText(Biljetter.getContext(), getString(R.string.OrderOptions_sending), Toast.LENGTH_LONG).show();

				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(number, null, message, null, null);

				setResult(RESULT_OK, getIntent());

				finish();
			}
			else if (which == DialogInterface.BUTTON_NEGATIVE) {
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
		this.transportCompany = (TransportCompany)intent.getParcelableExtra("transportcompany");
		this.transportArea = (TransportArea)intent.getParcelableExtra("transportarea");
		this.ticketType = (TicketType)intent.getParcelableExtra("tickettype");

		LinearLayout layoutHeader = (LinearLayout)findViewById(R.id.header);
		TextView txtCompanyname = (TextView)findViewById(R.id.companyname);
		ImageView imgCompanyLogo = (ImageView)findViewById(R.id.companylogo);
		txtAreaDescription = (TextView)findViewById(R.id.txtAreaDescription);
		txtTypeDescription = (TextView)findViewById(R.id.txtTypeDescription);
		chkFavorite = (CheckBox)findViewById(R.id.chkFavorite);

		int logo = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo() == null ? "nologo" : transportCompany.getLogo(), "drawable","se.rebootit.android.tagbiljetter");
		imgCompanyLogo.setImageResource(logo);

		int logobg = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo()+"_bg", "drawable","se.rebootit.android.tagbiljetter");
		layoutHeader.setBackgroundResource(logobg == 0 ? R.drawable.header_background : logobg);

		txtCompanyname.setTextColor(Color.parseColor(transportCompany.getTextColor()));
		txtCompanyname.setText(transportCompany.getName());

		Spinner spnArea = (Spinner)findViewById(R.id.spnArea);
		ArrayAdapter<CharSequence> adapterArea = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		areas = transportCompany.getTransportAreas();
		for (TransportArea area : areas) {
			adapterArea.add(area.getName());
		}
		spnArea.setAdapter(adapterArea);

		Spinner spnType = (Spinner)findViewById(R.id.spnType);
		ArrayAdapter<CharSequence> adapterType = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		types = transportCompany.getTicketTypes();
		for (TicketType type : types) {
			adapterType.add(type.getName());
		}
		spnType.setAdapter(adapterType);

		((Button)findViewById(R.id.btnSend)).setOnClickListener(this);

		spnArea.setOnItemSelectedListener(this);
		spnType.setOnItemSelectedListener(this);

		if (this.transportArea != null && this.ticketType != null) {
			spnArea.setSelection(adapterArea.getPosition(this.transportArea.getName()));
			spnType.setSelection(adapterType.getPosition(this.ticketType.getName()));

			chkFavorite.setVisibility(CheckBox.GONE);
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		String description;
		if (parent.getId() == R.id.spnArea)
		{
			description = areas.get(pos).getDescription();
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
			description = types.get(pos).getDescription();
			if ("".equals(description) || description == null) {
				txtTypeDescription.setVisibility(TextView.GONE);
			}
			else {
				txtTypeDescription.setText(description);
				txtTypeDescription.setVisibility(TextView.VISIBLE);
			}
		}
	}

	public void onNothingSelected(AdapterView parent) {
		// Do nothing.
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

	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSend)
		{
			TransportArea transportArea = areas.get(((Spinner)findViewById(R.id.spnArea)).getSelectedItemPosition());
			TicketType ticketType = types.get(((Spinner)findViewById(R.id.spnType)).getSelectedItemPosition());

			if (chkFavorite.isChecked()) {
				FavoriteItem item = new FavoriteItem(this.transportCompany, transportArea, ticketType);
				dataParser.addFavorite(item);
			}

			this.number = transportCompany.getPhoneNumber();
			this.message = transportCompany.getMessage(transportArea, ticketType);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.OrderOptions_confirmSendTitle));
			builder.setMessage(getString(R.string.OrderOptions_confirmSendMessage).replace("%message%", message).replace("%number%", number));
			builder.setPositiveButton(getString(R.string.yes), dialogClickListener);
			builder.setNegativeButton(getString(R.string.no), dialogClickListener);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.show();
		}
	}
}
