package impl;

import interfaces.KLeiter;

public class KLeiterImpl implements KLeiter {
	
	private int id;
	private String vorname;
	private String nachname;
	
	
	public KLeiterImpl(int id, String vorname, String nachname) {
		super();
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
	}


	/* (non-Javadoc)
	 * @see impl.KLeiter#getId()
	 */
	@Override
	public int getId() {
		return id;
	}


	/* (non-Javadoc)
	 * @see impl.KLeiter#getVorname()
	 */
	@Override
	public String getVorname() {
		return vorname;
	}


	/* (non-Javadoc)
	 * @see impl.KLeiter#getNachname()
	 */
	@Override
	public String getNachname() {
		return nachname;
	}
	
	
}
