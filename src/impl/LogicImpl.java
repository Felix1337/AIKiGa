package impl;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Logic;

public class LogicImpl implements Logic {

	@Override
	public ArrayList<Kita> getKitas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gruppe> getGruppen(Integer KitaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Kind> getKinder(Integer GruppeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPlatzFrei(Integer KitaId, Integer GruppeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean kindEintragen(String vorname,String nachname,Integer kitaId,Integer GruppeId, boolean warteschlange, double gehalt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rechnungDrucken(Integer KindId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double preisErmitteln(Integer KindId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
