package sql;

import java.sql.SQLException;

import utility.Password;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException{
		Password p = new Password();
		DBConnectorImpl d = DBConnectorImpl.valueOf(p.getHawAccName(), p.getHawAccPw());
		d.connect();
		
		System.out.println(d.getKitas()); // {1=Kita Bauerberg}
		System.out.println(d.getPriceByKindID(1)); // 153.0
		System.out.println(d.getGruppeByKindID(1)); // Katzen
		
		d.disconnect();

	}

}