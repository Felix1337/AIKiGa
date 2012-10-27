package sql;
import impl.BundeslandImpl;
import impl.ElternteilImpl;
import impl.GruppeImpl;
import impl.KLeiterImpl;
import impl.KindImpl;
import impl.KitaImpl;
import impl.RechnungImpl;
import impl.SonderleistungImpl;
import interfaces.Bundesland;
import interfaces.Elternteil;
import interfaces.Gruppe;
import interfaces.KLeiter;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Rechnung;
import interfaces.Sonderleistung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die Klasse ist für die Verbindung mit der Oracle-DB zuständig
 * Denkt bitte an {@link #connect()} UND {@link #disconnect()} !!!
 *	@author Anton Romanov (anton.romanov@haw-hamburg.de)
 *	@version 1.3 <br>Stand 23.10.2012
 */

public class DBConnectorImpl {
	
	private final String USERNAME;
	private final String PASSWORD;
	private Connection con;
	private final String URL="jdbc:oracle:thin:@ora.informatik.haw-hamburg.de:1521:inf09";
	
	private DBConnectorImpl(String user, String password){
		this.PASSWORD = password;
		this.USERNAME = user;
	}
	
	public static DBConnectorImpl valueOf(String user, String password){
		return new DBConnectorImpl(user, password);
	}
	
	/**
	 * Baut die Vebindung mit der DB auf
	 * @return true wenn erfolgreich. false sonst
	 * @see #disconnect()
	 */
	public boolean connect(){
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			setConnection(DriverManager.getConnection(URL, getUser(), getPassword()));
			getConn().setAutoCommit(false);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	private String getUser(){
		return this.USERNAME;
	}
	
	private String getPassword(){
		return this.PASSWORD;
	}
	
	private synchronized Connection getConn(){
		return this.con;
	}
	
	private synchronized void setConnection(Connection con){
		this.con=con;
	}
	
	/**
	 * Baut die Verbindung mit der DB ab
	 * @return true wenn erfolgreich. false sonst
	 * @see #connect()
	 */
	public boolean disconnect(){
		try {
			getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private ResultSet executeStatement(String s) throws SQLException{
		Statement st = getConn().createStatement();
		return st.executeQuery(s);
	}

	/*
	 * API
	 */
	
	/*
	 * Gruppe
	 */
	public Gruppe getGruppeByKindID(int id) throws SQLException{
		String query_gruppeID = "select g.ID, g.Bezeichnung as Gbez, g.Stunden, t.Bezeichnung as Tbez from KindGruppe, Gruppe g, Tageszeit t where Kind=? and Gruppe=g.ID and g.tageszeit = t.ID";
		PreparedStatement ps = getConn().prepareStatement(query_gruppeID);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		int gruppe_id = -1;
		String bezeichnung = "";
		String tageszeit = "";
		int stunden = -1;
		while(rs.next()){
			gruppe_id = rs.getInt("ID");
			bezeichnung = rs.getString("Gbez");
			tageszeit = rs.getString("Tbez");
			stunden = rs.getInt("Stunden");
		}
		return getGruppeByID(gruppe_id);
	}
	
	public Gruppe getGruppeByKind(Kind k) throws SQLException{
		return getGruppeByKindID(k.getId());
	}
	
	public Map<Integer, Gruppe> getGruppenByKitaID(int kitaID) throws SQLException {
		Map<Integer, Gruppe> gruppen = new HashMap<Integer, Gruppe>();
		String query = "SELECT Gruppe.ID as GID, Gruppe.Stunden as std, Gruppe.Bezeichnung as GBez, Tageszeit.Bezeichnung as Tbez FROM Gruppe JOIN Tageszeit ON Gruppe.Tageszeit = Tageszeit.ID WHERE Kita = ?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kitaID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("GID");
//			String name = rs.getString("GBez");
//			String zeit = rs.getString("Tbez");
//			int stunden = rs.getInt("std");
			gruppen.put(id, getGruppeByID(id));
		}
		return gruppen;
	}
	
	public Map<Integer, Gruppe> getGruppenByKita(Kita k) throws SQLException {
		return getGruppenByKitaID(k.getId());
	}
	
	/**
	 * Die Methode prüft, ob ein Platz in der Gruppe gruppeID frei ist. Die Anzahl
	 * der Plätze ist auf 30 begrenzt.
	 * @param gruppeID
	 * @return true wenn Platz frei ist. false wenn nicht
	 * @throws SQLException
	 */
	public boolean isPlatzFrei(int gruppeID) throws SQLException{
		String query = "select count(*) as Anzahl from KindGruppe where Gruppe=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, gruppeID);
		ResultSet rs = ps.executeQuery();
		int anzahl = 30;
		while (rs.next()) {
			anzahl = rs.getInt("Anzahl");
		}
		return anzahl<30;
	}
	
	public boolean isPlatzFrei(Gruppe g) throws SQLException{
		return isPlatzFrei(g.getId());
	}
	
	public Gruppe getGruppeByID(int gruppe_id) throws SQLException{
		String query = "SELECT Gruppe.Bezeichnung as GBez, Gruppe.Stunden as std, Tageszeit.Bezeichnung as TBez FROM Gruppe JOIN Tageszeit ON Gruppe.Tageszeit = Tageszeit.ID WHERE Gruppe.ID = ?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, gruppe_id);
		ResultSet rs = ps.executeQuery();
		String bezeichnung = "";
		String tageszeit = "";
		int stunden = -1;
		while(rs.next()){
			bezeichnung = rs.getString("GBez");
			tageszeit = rs.getString("TBez");
			stunden = rs.getInt("std");
		}
		return new GruppeImpl(bezeichnung, gruppe_id, tageszeit,stunden);
	}
	
	/*
	 * Kita
	 */
	public Kita getKitaByID(int id) throws SQLException{
		String query = "Select Bezeichnung, Kleiter, Bundesland from Kita where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		Kita kita = null;
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			kita = new KitaImpl(rs.getString("Bezeichnung"), id, getKLeiterByID(rs.getInt("KLeiter")), getBundeslandById(rs.getInt("Bundesland")));
		}
		return kita;
	}
	
	public Kita getKitaByKindID(int id) throws SQLException{
		Gruppe g = getGruppeByKindID(id);
		String query = "select k.ID as KID from Gruppe, Kita k where Gruppe.ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setString(1, String.valueOf(g.getId()));
		ResultSet rs = ps.executeQuery();
		int kita_id = -1;
		while(rs.next()){
			kita_id = rs.getInt("KID");
		}
		return getKitaByID(kita_id);
	}
	
	public Kita getKitaByKind(Kind k) throws SQLException{
		return getKitaByKindID(k.getId());
	}
	
	public Map<Integer, Kita> getKitas() throws SQLException {
		Map<Integer, Kita> kitas = new HashMap<Integer, Kita>();
		String query = "SELECT ID FROM Kita";
		ResultSet rs = executeStatement(query);
		while (rs.next()) {
			Integer id = rs.getInt("ID");
			kitas.put(id, getKitaByID(id));
		}
		return kitas;
	}
	
	
	/*
	 * Kind
	 */
	public Map<Integer, Kind> getKinderByGruppeID(int gruppeID) throws SQLException {
		Map<Integer, Kind> kinder = new HashMap<Integer, Kind>();
		String query = "SELECT ID FROM Kind k, KindGruppe kg where k.ID = kg.Kind and kg.Gruppe=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, gruppeID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("ID");
			kinder.put(id, getKindByID(id));
		}
		return kinder;
	}
	
	public double getPriceByKindID(int id, int gruppe_id) throws SQLException{
		String query = "select getPriceByID(?,?) as Preis from dual";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		ps.setInt(2, gruppe_id);
		ResultSet rs = ps.executeQuery();
		double preis = Double.NaN;
		while(rs.next()){
			preis = rs.getDouble("Preis");
		}
		return preis;
	}
	
	public double getPriceByKind(Kind k, Gruppe g) throws SQLException {
		return getPriceByKindID(k.getId(), g.getId());
	}
	
	public double getPriceByValues(int famMitglieder, double gehalt, int gruppe_id) throws SQLException{
		String query = "select getPriceByValues(?,?,?) as Preis from dual";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setDouble(1, gehalt);
		ps.setInt(2, famMitglieder);
		ps.setInt(3, gruppe_id);
		ResultSet rs = ps.executeQuery();
		double preis = -1.0;
		while(rs.next()){
			preis = rs.getDouble("Preis");
		}
		return preis;
	}
	
	public List<Kind> getKinderByKita(Kita k) throws SQLException{
		List<Kind> kinder = new ArrayList<Kind>();
		String query = "select kg.Kind from KindGruppe kg, Kita k, Gruppe g where kg.Gruppe=g.id and g.Kita= k.ID and k.ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, k.getId());
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			kinder.add(getKindByID(rs.getInt("Kind")));
		}
		return kinder;
	}
	
	/**
	 * Diese Methode ist für NEUE Kinder, die in die existierende Gruppe eingetragen werden müssen
	 * 
	 * @param vorame Vorname des Kindes
	 * @param nachname Nachname des Kindes
	 * @param gDatum Geburtsdatum des Kindes
	 * @param gehalt Gehalt der Eltern. (wird bald in Elternteil verschoben)
	 * @param anzahlFamMit Anzahl der Familienmitglieder. Wichtig für die Preisermittlung
	 * @param e Elternteil - wer zahlt für das Kind
	 * @return Das Kind-Object für das eingetragene Kind
	 * @throws SQLException
	 */
	public Kind addKind(String vorame, String nachname, Calendar gDatum, int anzahlFamMit, Elternteil e) throws SQLException{
		String query = "insert into kind(id,vorname,nachname,Geburtsdatum, Familie,Elternteil) values(NULL,?,?,?,?,?)";
		String date = gDatum == null ? "12.10.1987" : String.valueOf(gDatum.getTime().getDay())+"."+String.valueOf(gDatum.getTime().getMonth())+"."+String.valueOf(gDatum.getTime().getYear());
		//String date = String.valueOf(gDatum.get(Calendar.DAY_OF_MONTH))+"."+String.valueOf(gDatum.get(Calendar.MONTH))+"."+String.valueOf(gDatum.get(Calendar.YEAR));
		//String date = ;
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setString(1, vorame);
		ps.setString(2, nachname);
		ps.setString(3, date);
		ps.setInt(4, anzahlFamMit);
		ps.setInt(5, e.getId());
		ps.executeQuery();
		getConn().commit();
		String query_kind_id = "select max(id) as ID from Kind";
		int id = -1;
		ResultSet rs =executeStatement(query_kind_id);
		while(rs.next()){
			id = rs.getInt("ID");
		}
		return getKindByID(id);
	}
	
	public Kind getKindByID(int kindID) throws SQLException{
		String query = "select ID, Vorname, Nachname, Familie, Geburtsdatum FROM Kind where id=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kindID);
		ResultSet rs = ps.executeQuery();
		String vorname = "";
		String nachname ="";
		double gehalt = Double.NaN;
		int familie = -1;
                Calendar gebDatum = Calendar.getInstance();
                Elternteil e = new ElternteilImpl(-1, "", "", -1.0, ""); 
		while(rs.next()){
			vorname = rs.getString("Vorname");
			nachname = rs.getString("Nachname");
			familie = rs.getInt("Familie");
                        gebDatum.setTime(rs.getDate("GeburtsDatum"));
                        e = getElternteilByKindID(rs.getInt("ID"));
		}
		return new KindImpl(vorname, nachname, gebDatum, kindID,familie, e);
	}
	
	public void eintragenInWarteliste(Kind k, Gruppe g) throws SQLException{
		String query = "insert into Warteliste(Kind,Gruppe) values(?,?)";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, k.getId());
		ps.setInt(2, g.getId());
		ps.execute();
		getConn().commit();
	}
	
	/**
	 * Diese Methode ist für EXISTIERENDE Kinder, die in die existierende Gruppe eingetragen werden müssen
	 * 
	 * @param k Kind, das eingetragen werden muss
	 * @param g Gruppe, in die das Kind eingetragen werden muss
	 * @return true wenn das Hinzufügen erfolgreich war. false wenn nicht
	 */
	public boolean addKindToGruppe(Kind k, Gruppe g){
		
		PreparedStatement ps;
		try {
			int rechnung_id = -1;
			String query_rechnung_id = "select rechnung_id_seq.nextval as ID from dual";
			ResultSet rs = executeStatement(query_rechnung_id);
			while(rs.next()){
				rechnung_id = rs.getInt("ID");
			}
			Calendar now = Calendar.getInstance();
			String query = "insert into KindGruppe values(?,?,rechnung_nested_type(rechnung_type("+rechnung_id+",to_date('"+DateFormat.getDateInstance(DateFormat.MEDIUM).format(now.getTime())+"','DD.MM.YYYY'),"+getPriceByValues(k.getFamilie(), k.getElternteil().getGehalt(), g.getId())+")),NULL)";
			System.out.println(query);
			ps = getConn().prepareStatement(query);
			ps.setInt(1, k.getId());
			ps.setInt(2, g.getId());
			ps.execute();
			getConn().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Diese Methode ist für EXISTIERENDE Kinder, die in die existierende Gruppe eingetragen werden müssen
	 * Sie ruft addKindToGruppe(Kind k, Gruppe g) auf
	 * @param kind_id
	 * @param gruppe_id
	 * @return true wenn erfolgreich. false sonst
	 * @see #addKindToGruppe(Kind k, Gruppe g)
	 */
	public boolean addKindToGruppe(int kind_id, int gruppe_id){
		try {
			executeStatement("SAVEPOINT anfang");
		} catch (SQLException e2) {
			System.out.println("nein");
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try{
			return addKindToGruppe(getKindByID(kind_id), getGruppeByID(gruppe_id));
		} catch(SQLException e){
			try {
				executeStatement("rollback to anfang");
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * Die Methode meldet das Kind aus der Gruppe ab. Das Kind bleibt in der DB existieren
	 * 
	 * @param kind_id ID des Kindes
	 * @param gruppe_id ID der Gruppe
	 * @return true wenn erfolgreich. false sonst
	 */
	private boolean kindAbmelden(int kind_id, int gruppe_id){
		Savepoint svp = null;
		try {
			svp = getConn().setSavepoint("KindAbmeldenAnfang");
			String query_kg ="update KindGruppe set Gruppe=NULL where Kind=? and Gruppe=?";
			PreparedStatement ps = getConn().prepareStatement(query_kg);
			ps.setInt(1, kind_id);
			ps.setInt(2, gruppe_id);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				getConn().rollback(svp);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * Die Methode liefert das Kind, zu dem die Rechung erstellt wurde
	 * 
	 * @param rechnung_id ID der Rechnung
	 * @return Das Kind-Objekt, zu dem die Rechnung gehört
	 * @throws SQLException
	 */
	public Kind getKindByRechnungId(int rechnung_id) throws SQLException{
		String query = "select getkindidbyrechnungid(?) as ID from dual";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, rechnung_id);
		ResultSet rs = ps.executeQuery();
		Kind kind = null;
		while(rs.next()){
			kind = getKindByID(rs.getInt("ID"));
		}
		return kind;
	}
	
	
	
	/*
	 * Warteliste
	 */
	/**
	 * 
	 * @param kindID ID des Kindes
	 * @return Abbildung von Gruppe auf Platz in der Warteliste für diese Gruppe
	 * @throws SQLException
	 */
	public Map<Gruppe,Integer> getWartelistePosition(int kindID) throws SQLException{
		Map<Gruppe,Integer> result = new HashMap<Gruppe,Integer>();
		String query_gruppen = "select Gruppe from Warteliste where Kind=?";
		List<Integer> gruppen = new ArrayList<Integer>();
		PreparedStatement ps = getConn().prepareStatement(query_gruppen);
		ps.setInt(1, kindID);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			gruppen.add(rs.getInt("Gruppe"));
		}
		for(Integer gruppe: gruppen){
			String query = "select count(*) as Position from Warteliste where Gruppe=? and ID<=(select ID from Warteliste where Kind=? and Gruppe=?)";
			PreparedStatement ps2 = getConn().prepareStatement(query);
			ps2.setInt(1, gruppe);
			ps2.setInt(2, kindID);
			ps2.setInt(3, gruppe);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()){
				result.put(getGruppeByID(gruppe), rs2.getInt("Position"));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param gruppe_id ID der Gruppe
	 * @return Anzahl der Kinder, die auf einen Platz in der Gruppe mit der ID gruppe_id warten
	 * @throws SQLException
	 */
	public int getWartelisteLaenge(int gruppe_id) throws SQLException{
		int result = -1;
		String query = "select count(*) as Anzahl from Warteliste where Gruppe=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, gruppe_id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			result = rs.getInt("Anzahl");
		}
		return result;
	}
	
	/*
	 * Rechnung
	 */
	/**
	 * Rechnungen sind nur für Gruppen, für die das Kind angemeldet ist!
	 * 
	 * @param kind_id ID des Kindes
	 * @return Eine Liste von Rechnungen, die zu dem Kind gehören
	 * @throws SQLException
	 */
	public List<Rechnung> getRechungByKindID(int kind_id) throws SQLException{
		List<Rechnung> result = new ArrayList<Rechnung>();
		String query = "select ID from the(select Rechnungen from KindGruppe where Kind=?)";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kind_id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			result.add(getRechnungByID(rs.getInt("ID")));
		}
		return result;
	}
	
	@Deprecated
	private Rechnung getRechnungByID(int rechnung_id) throws SQLException{
		String query = "select * from the(select Rechnungen from KindGruppe) where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, rechnung_id);
		ResultSet rs = ps.executeQuery();
		int id = -1;
		Calendar c = Calendar.getInstance();
		double betrag = Double.NaN;
		while(rs.next()){
			id = rs.getInt("ID");
			c.setTime(rs.getDate("Datum"));
			betrag = rs.getDouble("Betrag");
		}
		Kind kind = getKindByRechnungId(rechnung_id);
		Gruppe gruppe = getGruppeByKind(kind);
		Kita kita = getKitaByKindID(kind.getId());
		List<Sonderleistung> sonderleistungen = getSonderleistungenByKindId(kind.getId());
		return new RechnungImpl(rechnung_id,betrag,kind,gruppe,kita,sonderleistungen);
	}
	
	public void addRechnung(int kind_id, int group_id) throws SQLException{
		int rechung_id = -3;
		String query_rechnung_id = "select rechnung_id_seq.nextval as ID from dual";
		ResultSet rs = executeStatement(query_rechnung_id);
		while(rs.next()){
			rechung_id = rs.getInt("ID");
		}
		System.out.println(rechung_id);
		Calendar now = Calendar.getInstance();
		String query = "insert into the(select Rechnungen from KindGruppe where Kind=? and Gruppe=?) values("+rechung_id+",to_date('"+DateFormat.getDateInstance(DateFormat.MEDIUM).format(now.getTime())+"','DD.MM.YYYY'),"+getPriceByKindID(kind_id,group_id)+")";
		System.out.println(query);
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kind_id);
		ps.setInt(2, group_id);
		ps.execute();
		getConn().commit();
		//return getRechnungByID(rechung_id);
	}
	
	/*
	 * Sonderleistung
	 */
	private List<Sonderleistung> getSonderleistungenByKindId(Integer kind_id) throws SQLException {
		List<Sonderleistung> result = new ArrayList<Sonderleistung>();
		String query = "select s.Id as ID, s.Bezeichnung as Bez, s.Preis as Preis from Sonderleistung s, KindGruppe kg where kg.Sonderleistung=s.ID and kg.Kind=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kind_id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			result.add(new SonderleistungImpl(rs.getString("Bez"),rs.getInt("ID"),rs.getDouble("Preis")));
		}
		return result;
	}
	
	
	
	/*
	 * Kindergartenleiter
	 */
	public KLeiter getKLeiterByID(int id) throws SQLException{
		String query = "select Vorname, Nachname from KLeiter where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		String vorname = "";
		String nachname = "";
		while(rs.next()){
			vorname = rs.getString("Vorname");
			nachname = rs.getString("Nachname");
		}
		return new KLeiterImpl(id, vorname, nachname);
	}
	
	public KLeiter addKLeiter(String vorname, String nachname, String benutzername, String passwort) throws SQLException{
		String query = "insert into KLeiter(ID,Vorname,Nachname,Benutzername,Passwort) values(NULL,?,?,?,?)";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setString(1, vorname);
		ps.setString(2, nachname);
		ps.setString(3, benutzername);
		ps.setString(4, passwort);
		ps.execute();
		getConn().commit();
		String select_query = "select max(id) as ID from KLeiter";
		ResultSet rs = executeStatement(select_query);
		int id = -1;
		while(rs.next()){
			id = rs.getInt("ID");
		}
		return getKLeiterByID(id);
	}
	
	public KLeiter getKLeiterByKitaID(int kita_id) throws SQLException{
		String query = "select KLeiter from Kita where KLeiter=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, kita_id);
		ResultSet rs = ps.executeQuery();
		int kl_id = -1;
		while(rs.next()){
			kl_id = rs.getInt("KLeiter");
		}
		return getKLeiterByID(kl_id);
	}
	
	/*
	 * Elternteil
	 */
	public Elternteil getElternteilById(int id) throws SQLException{
		String query = "select Vorname, Nachname, Gehalt, Geschlecht from Elternteil where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		String vorname = "";
		String nachname = "";
		double gehalt = Double.NaN;
		String geschlecht = "";
		while(rs.next()){
			vorname = rs.getString("Vorname");
			nachname = rs.getString("Nachname");
			gehalt = rs.getDouble("Gehalt");
			geschlecht = rs.getString("Geschlecht");
		}
		return new ElternteilImpl(id, vorname, nachname, gehalt, geschlecht);
	}
	
	public Elternteil getElternteilByKindID(int id) throws SQLException{
		String query = "select Elternteil from Kind where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		int e_id = -1;
		while(rs.next()){
			e_id = rs.getInt("Elternteil");
		}
		return getElternteilById(e_id);
	}
	
	/**
	 * Als Geschlecht bitte nur "m" oder "w" übergeben! Alles andere wird nicht angenommen!!! <br>
	 * Als Gehalt bitte das Netto-Einkommen der beiden Erziehungsberechtigten
	 * 
	 * @param vorname Vorname der Mutter / des Vaters
	 * @param nachname Nachname der Mutter / des Vaters
	 * @param geschlecht Geschlecht der Mutter / des Vaters. "m" für männlich, "w" für weiblich 
	 * @param gehalt Gehalt der beiden Elternteile!
	 * @param benutzername lokale Benutzername
	 * @param passwort lokales Passwort
	 * @return Das Elternteil-Object
	 * @throws SQLException
	 */
	public Elternteil addElternteil(String vorname, String nachname, String geschlecht, double gehalt, String benutzername, String passwort) throws SQLException{
		String query = "insert into Elternteil(ID,Vorname,Nachname,Geschlecht,Gehalt,Benutzername,Passwort) values(NULL,?,?,?,?,?,?)";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setString(1,vorname);
		ps.setString(2, nachname);
		ps.setString(3, geschlecht);
		ps.setDouble(4, gehalt);
		ps.setString(5, benutzername);
		ps.setString(6, passwort);
		ps.execute();
		getConn().commit();
		String query_id = "select max(id) as ID from Elternteil";
		ResultSet rs = executeStatement(query_id);
		int id = -1;
		while(rs.next()){
			id = rs.getInt("ID");
		}
		return getElternteilById(id);
	}
	
	/*
	 * Bundesland
	 */
	
	public Bundesland getBundeslandById(int id) throws SQLException{
		String query = "select Bezeichnung, Krzl from Bundesland where ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		Bundesland bundesland = null;
		while(rs.next()){
			bundesland = new BundeslandImpl(id, rs.getString("Krzl"), rs.getString("Bezeichnung"));
		}
		return bundesland;
	}
	
}
