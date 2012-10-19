package impl;

import interfaces.Gruppe;

public class GruppeImpl implements Gruppe{

	private String name;
	private String zeit;
	private Integer id;
	private Integer stunden;
	

	public GruppeImpl(String name, int id, String zeit, int stunden) {
		this.name = name;
		this.id = id;
		this.zeit = zeit;
		this.stunden = stunden;
	}
	
	public GruppeImpl( int id) {
	
		this.id = id;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public String getZeit() {
		return zeit;
	}

	@Override
	public int getStunden() {
		return stunden;
	}

}
