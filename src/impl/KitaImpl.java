package impl;

import interfaces.Kita;

public class KitaImpl implements Kita {

	private String name;
	private Integer id;
	

	public KitaImpl(String name, int id) {
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
