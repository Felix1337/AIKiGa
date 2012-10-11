package sql;
import impl.GruppeImpl;
import impl.KitaImpl;
import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 *	@author Anton Romanov
 *	@date 11.10.2012
 *	@version 1.0
 */

public class DBConnectorImpl {
	
	private String username;
	private String password;
	private Connection con;
	private String url="jdbc:oracle:thin:@ora.informatik.haw-hamburg.de:1521:inf09";
	
	private DBConnectorImpl(String user, String password){
		this.password = password;
		this.username = user;
	}
	
	public static DBConnectorImpl valueOf(String user, String password){
		return new DBConnectorImpl(user, password);
	}
	
	public boolean connect(){
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			setConnection(DriverManager.getConnection(url, getUser(), getPassword()));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	private String getUser(){
		return this.username;
	}
	
	private String getPassword(){
		return this.password;
	}
	
	private synchronized Connection getConn(){
		return this.con;
	}
	
	private synchronized void setConnection(Connection con){
		this.con=con;
	}
	
	public boolean disconnect(){
		try {
			getConn().close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Gruppe getGruppeByKindID(int id) throws SQLException{
		String query_gruppeID = "select g.ID, g.Bezeichnung as Gbez, t.Bezeichnung as Tbez from KindGruppe, Gruppe g, Tageszeit t where Kind=? and Gruppe=g.ID and g.tageszeit = t.ID;";
		PreparedStatement ps = getConn().prepareStatement(query_gruppeID);
		ps.setString(1, String.valueOf(id));
		ResultSet rs = ps.executeQuery();
		int gruppe_id = -1;
		String bezeichnung = "";
		String tageszeit = "";
		while(rs.next()){
			gruppe_id = rs.getInt("ID");
			bezeichnung = rs.getString("Gbez");
			tageszeit = rs.getString("Tbez");
		}
		return new GruppeImpl(bezeichnung, gruppe_id, tageszeit);
	}
	
	public Gruppe getGruppeByKind(Kind k) throws SQLException{
		return getGruppeByKindID(k.getId());
	}
	
	public Kita getKitaByKindID(int id) throws SQLException{
		Gruppe g = getGruppeByKindID(id);
		String query = "select k.ID as KID, k.Bezeichnung as Kbez from Gruppe, Kita k where Gruppe.ID=?";
		PreparedStatement ps = getConn().prepareStatement(query);
		ps.setString(1, String.valueOf(g.getId()));
		ResultSet rs = ps.executeQuery();
		String bezeichnung = "";
		int kita_id = -1;
		while(rs.next()){
			kita_id = rs.getInt("KID");
			bezeichnung = rs.getString("Kbez");
		}
		return new KitaImpl(bezeichnung, kita_id);
	}
	
	public Kita getKitaByKind(Kind k) throws SQLException{
		return getKitaByKindID(k.getId());
	}
	
	private ResultSet executeStatement(String s) throws SQLException{
		Statement st = getConn().createStatement();
		return st.executeQuery(s);
	}

}