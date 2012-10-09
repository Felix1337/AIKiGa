package impl;

import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.Date;
import java.util.*;


public class LogicImpl implements Logic {
	Connection conn;

	public LogicImpl() {
		try {
			conn = DriverManager.getConnection(null, Passwort.getUsername(),
					Passwort.getPassword());
		} catch (SQLException e) {

		}
	}

	@Override
	public Map<Integer, Kita> getKitas() {
		Map<Integer, Kita> kitas = new HashMap<Integer, Kita>();
		try (Statement s = conn.createStatement()) {
			String query = "SELECT ID, Bezeichnung FROM Kita;";
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				Integer id = rs.getInt("ID");
				String name = rs.getString("Bezeichnung");
				kitas.put(id, new KitaImpl(name, id));
			}
		} catch (SQLException e) {
			// TODO
		} finally {
			return kitas;
		}
	}

	@Override
	public Map<Integer, Gruppe> getGruppen(Integer KitaId) {
		Map<Integer, Gruppe> gruppen = new HashMap<Integer, Gruppe>();
		try (Statement s = conn.createStatement()) {
			String query = "SELECT Gruppe.ID, Gruppe.Bezeichnung, Tageszeit.Bezeichnung FROM Gruppe JOIN Tageszeit ON Gruppe.Tageszeit = Tageszeit.ID WHERE Kita = "
					+ KitaId + ";";
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				Integer id = rs.getInt(1);
				String name = rs.getString(2);
				String zeit = rs.getString(3);
				gruppen.put(id, new GruppeImpl(name, id, zeit));
			}
		} catch (SQLException e) {
			// TODO
		} finally {
			return gruppen;
		}
	}

	@Override
	public Map<Integer, Kind> getKinder(Integer GruppeId) {
		Map<Integer, Kind> kinder = new HashMap<Integer, Kind>();
		try (Statement s = conn.createStatement()) {
			String query = "SELECT Vorname, Nachname, Gehalt, ID FROM Kind JOIN (SELECT * FROM Gruppe JOIN KindGruppe ON Gruppe.ID = KindGruppe.Gruppe) AS X ON Kind.ID = X.Kind WHERE Gruppe = "
					+ GruppeId/* +";" */;
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				String vname = rs.getString("Vorname");
				String nname = rs.getString("Nachname");
				double gehalt = rs.getDouble("Gehalt");
				Integer id = rs.getInt("ID");
				kinder.put(id, new KindImpl(vname, nname, gehalt, id));
			}
		} catch (SQLException e) {
			// TODO
		} finally {
			return kinder;
		}
	}

	@Override
	public boolean isPlatzFrei(Integer KitaId, Integer GruppeId) {
		try (Statement st = conn.createStatement()) {
			String query = "SELECT COUNT(*) FROM KindGruppe where Gruppe="
					+ GruppeId;
			ResultSet rs = st.executeQuery(query);
			return rs.getInt(1) < 30;
		} catch (SQLException e) {
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
		String vname = "";
		String nname = "";
		try (Statement s = conn.createStatement()) {
			String query = "SELECT Vorname, Nachname, FROM Kind Where KindId="
					+ KindId;
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				vname = rs.getString("Vorname");
				nname = rs.getString("Nachname");
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}

		//get current date
		Date dt = new Date();

		//get rechnungsbetrag for current child
		double rechnungsbetrag = preisErmitteln(KindId);

		try {			
			// Create file
			FileWriter fstream = new FileWriter("out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Rechnung für " + vname + " " + nname);
			out.newLine();
			out.write("Rechnungsdatum: " + dt);
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
		double res = Double.NaN;
		try (Statement st = conn.createStatement()) {
			String s = "select getPriceByID(" + KindId + ") as Price from dual";
			ResultSet rs = st.executeQuery(s);
			res = rs.getDouble(1);
		} catch (SQLException e) {

		} finally {
			return res;
		}
	}

}
