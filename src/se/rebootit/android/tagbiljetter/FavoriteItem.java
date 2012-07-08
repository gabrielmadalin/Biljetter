/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.io.*;
import java.text.*;

import android.os.*;
import android.util.*;

import se.rebootit.android.tagbiljetter.models.*;

public class FavoriteItem implements Parcelable, Serializable, Comparable<FavoriteItem>
{
	private static final long serialVersionUID = 1L;

	private TransportCompany transportCompany;
	private TransportArea transportArea;
	private TicketType ticketType;

	public FavoriteItem(TransportCompany transportCompany, TransportArea transportArea, TicketType ticketType)
	{
		this.transportCompany = transportCompany;
		this.transportArea = transportArea;
		this.ticketType = ticketType;
	}

	public TransportCompany getTransportCompany() { return this.transportCompany; }
	public TransportArea getTransportArea() { return this.transportArea; }
	public TicketType getTicketType() { return this.ticketType; }

	public int compareTo(FavoriteItem p)
	{
		int res = this.getTransportCompany().getName().compareTo(p.getTransportCompany().getName());
		if (res != 0) {
			return res;
		}
		res = this.getTransportArea().getName().compareTo(p.getTransportArea().getName());
		if (res != 0) {
			return res;
		}

		return this.getTicketType().getName().compareTo(p.getTicketType().getName());
	}

	@Override
	public boolean equals(Object obj) {
		return (((FavoriteItem)obj).hashCode() == hashCode() ? true : false);
	}

	@Override
	public int hashCode()
	{
		int code = 17;
		code = 31*code + this.transportCompany.hashCode();
		code = 31*code + this.transportArea.hashCode();
		code = 31*code + this.ticketType.hashCode();

		return code;
	}

	protected FavoriteItem(Parcel in) {
		this.transportCompany = in.readParcelable(TransportCompany.class.getClassLoader());
		this.transportArea = in.readParcelable(TransportArea.class.getClassLoader());
		this.ticketType = in.readParcelable(TicketType.class.getClassLoader());
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(this.transportCompany, flags);
		out.writeParcelable(this.transportArea, flags);
		out.writeParcelable(this.ticketType, flags);
	}

	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>()
	{
		public FavoriteItem createFromParcel(Parcel in) {
			return new FavoriteItem(in);
		}

		public FavoriteItem[] newArray(int size) {
			return new FavoriteItem[size];
		}
	};
}
