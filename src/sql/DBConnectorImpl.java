import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	
	public synchronized Connection getConn(){
		return this.con;
	}
	
	public synchronized void setConnection(Connection con){
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

}
