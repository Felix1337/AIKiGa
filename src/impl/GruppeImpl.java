package impl;

import interfaces.Gruppe;

public class GruppeImpl implements Gruppe{

	private String name;
	private String zeit;
	private Integer id;
	

	public GruppeImpl(String name, int id, String zeit) {
		this.name = name;
		this.id = id;
		this.zeit = zeit;
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

}
