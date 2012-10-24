package impl;

import interfaces.Bundesland;

public class BundeslandImpl implements Bundesland {

	private int id;
	private String krzl;
	private String bezeichnung;
	
	public BundeslandImpl(int id, String krzl, String bezeichnung) {
		this.id = id;
		this.krzl = krzl;
		this.bezeichnung = bezeichnung;
	}

	/* (non-Javadoc)
	 * @see impl.Bundesland#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see impl.Bundesland#getKrzl()
	 */
	@Override
	public String getKrzl() {
		return krzl;
	}

	/* (non-Javadoc)
	 * @see impl.Bundesland#getBezeichnung()
	 */
	@Override
	public String getBezeichnung() {
		return bezeichnung;
	}
	
}
