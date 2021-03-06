/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */
package se.rebootit.android.tagbiljetter.models;

import android.os.*;

import java.util.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class TransportCompany_SL extends TransportCompany
{
	public static final Parcelable.Creator<TransportCompany_SL> CREATOR = new Parcelable.Creator<TransportCompany_SL>()
	{
		public TransportCompany_SL createFromParcel(Parcel in) {
			return new TransportCompany_SL(in);
		}

		public TransportCompany_SL[] newArray(int size) {
			return new TransportCompany_SL[size];
		}
	};

	public TransportCompany_SL() { }

	public TransportCompany_SL(String name, String phonenumber) {
		super(name, phonenumber);
	}

	public String getMessage(TransportArea area, TicketType type) {
		return type.getCode()+area.getCode();
	}


	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

	private TransportCompany_SL(Parcel in) {
		super(in);
	}
}
