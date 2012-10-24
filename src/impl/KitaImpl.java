package impl;

import interfaces.Bundesland;
import interfaces.KLeiter;
import interfaces.Kita;

public class KitaImpl implements Kita {

	private String name;
	private Integer id;
	private KLeiter kleiter;
	private Bundesland bundesland;
	

	public KitaImpl(String name, int id, KLeiter kleiter, Bundesland bundesland) {
		this.name = name;
		this.id = id;
		this.kleiter = kleiter;
		this.bundesland = bundesland;
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
	public KLeiter getKLeiter(){
		return this.kleiter;
	}
	@Override
	public Bundesland getBundesland() {
		return this.bundesland;
	}

}
