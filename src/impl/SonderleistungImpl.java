package impl;

import interfaces.Sonderleistung;

public class SonderleistungImpl implements Sonderleistung {

	private String bezeichnung;
	private int id;
	private double preis;
	
	public SonderleistungImpl(String bez, int id, double preis){
		this.id = id;
		this.bezeichnung = bez;
		this.preis = preis;
	}
	
	/* (non-Javadoc)
	 * @see impl.Sonderleistung#getBezeichung()
	 */
	@Override
	public String getBezeichung(){
		return this.bezeichnung;
	}
	
	/* (non-Javadoc)
	 * @see impl.Sonderleistung#getId()
	 */
	@Override
	public int getId(){
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see impl.Sonderleistung#getPreis()
	 */
	@Override
	public double getPreis(){
		return this.preis;
	}
}
