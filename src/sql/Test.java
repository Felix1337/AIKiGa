package sql;

import interfaces.Rechnung;

import java.sql.SQLException;
import java.util.List;

import utility.Password;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException{
		Password p = new Password();
		DBConnectorImpl d = DBConnectorImpl.valueOf(p.getHawAccName(), p.getHawAccPw());
		d.connect();
//		List<Rechnung> l = d.getRechungByKindID(1);
//		for(Rechnung r : l){
//			System.out.println(r.getBetrag());
//		}
//		System.out.println(d.addKindToGruppe(d.getKindByID(2), d.getGruppeByID(1)));
		d.addRechnung(1, 1);
//		System.out.println(d.getKitas()); // {1=Kita Bauerberg}
//		System.out.println(d.getPriceByKindID(1)); // 153.0
//		System.out.println(d.getGruppeByKindID(1)); // Katzen
//		System.out.println(d.getKindByID(1).getVorname());
//		System.out.println(d.getWartelistePosition(5));
//		System.out.println(d.getWartelisteLaenge(1));
		d.disconnect();

	}

}
