package impl;

import interfaces.Kind;

public class KindImpl implements Kind{


	private String vorname;
	private String nachname;
	private double gehalt;
	private Integer id;
	

	public KindImpl(String vorname,String nachname,double gehaltEltern, int id) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.gehalt = gehaltEltern;
		this.id = id;
	}
	@Override
	public String getVorname() {
		return vorname;
	}
	@Override
	public String getNachname() {
		return nachname;
	}

	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public double getGehalt() {
		return gehalt;
	}
	
	@Override
	public String toString() {
		return vorname + " " + nachname;
	}
	@Override
	public Object[] getStats() {
		return new Object[]{"ID: " + id,"Vorname: " + vorname,"Nachname: " + nachname};
	}
}