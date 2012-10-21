package impl;

import interfaces.Elternteil;

public class ElternteilImpl implements Elternteil {
	
	private int id;
	private String vorname;
	private String nachname;
	private double gehalt;
	private String geschlecht;
	
	public ElternteilImpl(int id, String vorname, String nachname, double gehalt, String geschlecht){
		this.id= id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.gehalt = gehalt;
		this.geschlecht = geschlecht;
	}

	/* (non-Javadoc)
	 * @see impl.Elternteil#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see impl.Elternteil#getVorname()
	 */
	@Override
	public String getVorname() {
		return vorname;
	}

	/* (non-Javadoc)
	 * @see impl.Elternteil#getNachname()
	 */
	@Override
	public String getNachname() {
		return nachname;
	}

	/* (non-Javadoc)
	 * @see impl.Elternteil#getGehalt()
	 */
	@Override
	public double getGehalt() {
		return gehalt;
	}

	/* (non-Javadoc)
	 * @see impl.Elternteil#getGeschlecht()
	 */
	@Override
	public String getGeschlecht() {
		return geschlecht;
	}
}
