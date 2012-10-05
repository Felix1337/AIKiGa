package impl;

import interfaces.Gruppe;

public class GruppeImpl implements Gruppe{

	private String name;
	private Integer id;
	

	public GruppeImpl(String name, int id) {
		this.name = name;
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

}
