/**
 * This file is licensed under the GNU General Public License Version 3
 * For more information, please visit http://www.gnu.org/licenses/gpl.txt
 */

package se.rebootit.android.tagbiljetter;

import java.io.*;
import java.text.*;

import se.rebootit.android.tagbiljetter.models.*;

public class FavoriteItem implements Serializable, Comparable<FavoriteItem>
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
	
	public int compare(FavoriteItem p1, FavoriteItem p2) {
		return p1.getTransportCompany().getName().compareTo(p2.getTransportCompany().getName());
	}

	public int compareTo(FavoriteItem p) {
		return getTransportCompany().getName().compareTo(p.getTransportCompany().getName());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof FavoriteItem))
			return false;

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
}
