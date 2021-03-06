/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import se.rebootit.android.tagbiljetter.models.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class TicketView extends CustomActivity
{
	Ticket ticket;
	DataParser dataParser = Biljetter.getDataParser();
	SharedPreferences sharedPreferences = Biljetter.getSharedPreferences();
	DataBaseHelper dbHelper = Biljetter.getDataBaseHelper();
	boolean fromNotification = false;

	NotificationManager mNotificationManager;

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketview);

		// Set the correct header
		getSupportActionBar().setTitle(getString(R.string.TicketView_header));

		Intent intent = getIntent();
		this.ticket = intent.getParcelableExtra("ticket");
		this.fromNotification = intent.getBooleanExtra("fromNotification", false);

		// Should we keep the Screen on?
		if (sharedPreferences.getBoolean("keepscreenon", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		LinearLayout layoutHeader = (LinearLayout)findViewById(R.id.header);
		TextView txtCompanyname = (TextView)findViewById(R.id.companyname);
		ImageView imgCompanyLogo = (ImageView)findViewById(R.id.companylogo);

		TransportCompany transportCompany = dataParser.getCompany(ticket.getProvider());

		if (transportCompany.getLogo() == null) {
			imgCompanyLogo.setVisibility(ImageView.GONE);
		}
		else
		{
			int logo = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo(), "drawable","se.rebootit.android.tagbiljetter");
			int logobg = Biljetter.getContext().getResources().getIdentifier(transportCompany.getLogo()+"_bg", "drawable","se.rebootit.android.tagbiljetter");
			imgCompanyLogo.setImageResource(logo);
			layoutHeader.setBackgroundResource(logobg == 0 ? R.drawable.header_background : logobg);
		}

		txtCompanyname.setTextColor(Color.parseColor(transportCompany.getTextColor()));
		txtCompanyname.setText(transportCompany.getName());

		((TextView)findViewById(R.id.sender)).setText(ticket.getAddress());

		if (!"".equals(ticket.getTicketTimestampFormatted())) {
			((TextView)findViewById(R.id.validtoHeader)).setVisibility(TextView.VISIBLE);
			((TextView)findViewById(R.id.validto)).setVisibility(TextView.VISIBLE);
			((TextView)findViewById(R.id.validto)).setText(ticket.getTicketTimestampFormatted());
		}

		((TextView)findViewById(R.id.received)).setText(ticket.getTimestampFormatted());
		((TextView)findViewById(R.id.message)).setText(ticket.getMessage());

		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.ticketview, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menuNotification:
				if (fromNotification)
				{
					mNotificationManager.cancel(ticket.hashCode());
					fromNotification = false;
				}
				else
				{
					Notification notification = new Notification(R.drawable.icon, null, System.currentTimeMillis());

					CharSequence contentTitle = DataParser.getCompanyName(ticket.getProvider());
					CharSequence contentText = getString(R.string.SmsReceiver_description).replace("%date%", ticket.getTicketTimestampFormatted());

					Intent notificationIntent = new Intent(this, TicketView.class);
					notificationIntent.putExtra("ticket", (Parcelable)ticket);
					notificationIntent.putExtra("fromNotification", true);
					notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
					notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					notificationIntent.setAction(this.getClass().getName() + System.currentTimeMillis());

					PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

					notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
					notification.flags = Notification.FLAG_ONGOING_EVENT;

					mNotificationManager.notify(ticket.hashCode(), notification);

					fromNotification = true;
				}
				return true;

			case R.id.menuRemove:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.TicketView_confirmRemoveTitle));
				builder.setMessage(getString(R.string.TicketView_confirmRemoveMessage));
				builder.setPositiveButton(getString(R.string.yes), dialogClickListener);
				builder.setNegativeButton(getString(R.string.no), dialogClickListener);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.show();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (which == DialogInterface.BUTTON_POSITIVE)
			{
				dbHelper.removeTicket(ticket.getAddress(), ticket.getTimestamp(), ticket.getMessage());

				Biljetter.getContext().sendBroadcast(new Intent("se.rebootit.android.tagbiljett.TicketList.UPDATE_LIST"));

				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	};
}
