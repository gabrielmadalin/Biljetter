/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */
package se.rebootit.android.tagbiljetter.models;

import java.io.*;

import android.os.*;

/**
 * @author Erik Fredriksen <erik@fredriksen.se>
 */
public class TicketType implements Parcelable, Serializable
{
	private static final long serialVersionUID = 1L;

	private String code = "";
	private String name = "";
	private String description = "";

	public static final Parcelable.Creator<TicketType> CREATOR = new Parcelable.Creator<TicketType>() {
		public TicketType createFromParcel(Parcel in) {
			return new TicketType(in);
		}

		public TicketType[] newArray(int size) {
			return new TicketType[size];
		}
	};

	public TicketType(String code, String name, String description)
	{
		this.code = code;
		this.name = name;
		this.description = description;
	}

	public void setCode(String code) { this.code = code; }
	public String getCode() { return this.code; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setDescription(String description) { this.description = description; }
	public String getDescription() { return this.description; }

	@Override
	public int hashCode()
	{
		int code = 17;
		code = 31*code + this.code.hashCode();
		code = 31*code + this.name.hashCode();

		return code;
	}

	private TicketType(Parcel in) {
		this.code = in.readString();
		this.name = in.readString();
		this.description = in.readString();
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.code);
		out.writeString(this.name);
		out.writeString(this.description);
	}

	public int describeContents() {
		return 0;
	}
}
