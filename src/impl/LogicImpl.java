package impl;

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
	DBConnectorImpl dbconncetor;

	public LogicImpl() {
		this.dbconncetor = DBConnectorImpl.valueOf(p.getHawAccName(), p.getHawAccPw());
		this.dbconncetor.connect();
	}

	@Override
	public Map<Integer, Kita> getKitas() {
		try {
			return dbconncetor.getKitas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Integer, Gruppe> getGruppen(Integer KitaId) {
		try {
			return dbconncetor.getGruppenByKitaID(KitaId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Integer, Kind> getKinder(Integer GruppeId) {
		try {
			return dbconncetor.getKinder(GruppeId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isPlatzFrei(Integer KitaId, Integer GruppeId) {
		try {
			return dbconncetor.isPlatzFrei(GruppeId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean kindEintragen(String vorname, String nachname,
			Calendar geburtsdatum, Integer GruppeId, boolean warteschlange,
			double gehalt, int familienmitglieder) {
		try (Statement st = conn.createStatement();
				Statement st2 = conn.createStatement()) {
			String query = "INSERT INTO Kind VALUES(NULL, " + vorname + ", "
					+ nachname + ", to_date(" + geburtsdatum
					+ ", 'DD.MM.YYYY'), " + gehalt + ");";
			st.executeQuery(query);
			String query2 = "INSERT INTO KindGruppe values((select max(ID) from Kind),"
					+ GruppeId + ")";
			st2.executeQuery(query2);
			return true;
		} catch (SQLException e) {
			try (Statement st = conn.createStatement()) {
				String query = "INSERT INTO Warteliste values((select max(ID) from Kind),"
						+ GruppeId + ")";
			} catch (SQLException ex) {
				conn.rollback();
				System.out.println("ERROR");
			} finally {
				return false;
			}
		}
	}

	@Override
	public void rechnungDrucken(Integer KindId) {

		
		
		//get personal data for KindId
		Kind k = null;
		Gruppe g = null;
		Kita kita = null;
		try {
			k = dbconncetor.getKindByID(KindId);
			g = dbconncetor.getGruppeByKind(k);
			kita = dbconncetor.getKitaByKind(k);
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
			out.write("Kita: " + k.getNachname());
			out.newLine();
			out.write("Gruppe: "+g.getName()+"("+g.getId()+")");
			out.newLine();
			out.write("Rechnungsdatum: " + c.getTime());
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
			preis = dbconncetor.getPriceByKindID(KindId);
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
			return dbconncetor.getPriceByValues(FamMitglieder, gehalt, dauerBetreueung);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Double.NaN;
	}

	@Override
	public Map<Gruppe,Integer> getWartelistePosition(int KindID) {
		try {
			return dbconncetor.getWartelistePosition(KindID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
