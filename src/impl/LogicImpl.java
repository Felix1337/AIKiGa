package impl;

import interfaces.Elternteil;
import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Logic;

import sql.DBConnectorImpl;
import utility.Password;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.Date;
import java.util.*;


public class LogicImpl implements Logic {
	Connection conn;
	Password p = new Password();
	DBConnectorImpl dbconnector;

	public LogicImpl() {
		this.dbconnector = DBConnectorImpl.valueOf(p.getHawAccName(), p.getHawAccPw());
		this.dbconnector.connect();
	}

	@Override
	public Map<Integer, Kita> getKitas() {
		try {
			return dbconnector.getKitas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Integer, Gruppe> getGruppen(Integer KitaId) {
		try {
			return dbconnector.getGruppenByKitaID(KitaId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Integer, Kind> getKinder(Integer GruppeId) {
		try {
			return dbconnector.getKinderByGruppeID(GruppeId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isPlatzFrei(Integer KitaId, Integer GruppeId) {
		try {
			return dbconnector.isPlatzFrei(GruppeId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean kindEintragen(String vorname, String nachname,
			Calendar geburtsdatum, Integer GruppeId, boolean warteschlange, int familienmitglieder, Elternteil elternteil) {
		try {
			if(dbconnector.isPlatzFrei(GruppeId) && !warteschlange){
				dbconnector.addKindToGruppe(dbconnector.addKind(vorname, nachname, geburtsdatum,familienmitglieder, elternteil).getId(), GruppeId);
			} else if(warteschlange){
				Kind k = dbconnector.addKind(vorname, nachname, geburtsdatum, familienmitglieder, elternteil);
				Gruppe g = dbconnector.getGruppeByID(GruppeId);
				dbconnector.eintragenInWarteliste(k, g);
			} else return false;
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	@Override
	public void rechnungDrucken(Integer KindId) {

		
		
		//get personal data for KindId
		Kind k = null;
		Gruppe g = null;
		Kita kita = null;
		try {
			k = dbconnector.getKindByID(KindId);
			g = dbconnector.getGruppeByKind(k);
			kita = dbconnector.getKitaByKind(k);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//get current date
		Calendar c = Calendar.getInstance();

		//get rechnungsbetrag for current child
		double rechnungsbetrag = preisErmitteln(KindId);

		try {			
			// Create file
			FileWriter fstream = new FileWriter(k.getVorname()+k.getNachname()+".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Rechnung für " + k.getVorname() + " " + k.getNachname());
			out.newLine();
			out.write("Kita: " + kita.getName());
			out.newLine();
			out.write("Gruppe: "+g.getName()+"("+g.getId()+")");
			out.newLine();
			out.write("Rechnungsdatum: " +  String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(c.get(Calendar.MONTH))+"."+String.valueOf(c.get(Calendar.YEAR)));
			out.newLine();
			out.write("Rechnungsbetrag: " + rechnungsbetrag);

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	@Override
	public double preisErmitteln(Integer KindId) {
		double preis = Double.NaN;
		try {
			//Statt 1 muss ID der Gruppe sein!
			preis = dbconnector.getPriceByKindID(KindId,1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return preis;
	}

	@Override
	public double preisErmitteln(int FamMitglieder, double gehalt,
			int dauerBetreueung) {
		
		try {
			return dbconnector.getPriceByValues(FamMitglieder, gehalt, dauerBetreueung);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Double.NaN;
	}

	@Override
	public Map<Gruppe,Integer> getWartelistePosition(int KindID) {
		try {
			return dbconnector.getWartelistePosition(KindID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}