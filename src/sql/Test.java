package sql;

import interfaces.Elternteil;
import interfaces.KLeiter;
import interfaces.Kind;
import interfaces.Rechnung;

import java.sql.SQLException;
import java.util.Calendar;
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
//		d.addRechnung(1, 1);
//		System.out.println(d.getKindByID(1)); // {1=Kita Bauerberg}
//		System.out.println(d.getPriceByKindID(1)); // 153.0
//		System.out.println(d.getGruppeByKindID(1)); // Katzen
//		System.out.println(d.getKindByID(1).getVorname());
//		System.out.println(d.getWartelistePosition(5));
//		System.out.println(d.getWartelisteLaenge(1));
		
//		KLeiter k = d.addKLeiter("Andrew", "Tannenbaum", "atb", "12345");
//		System.out.println(d.getKLeiterByID(k.getId()).getVorname());
//		System.out.println(d.getRechungByKindID(1).get(0).getBetrag());
		//System.out.println(d.getElternteilByKindId(1).getNachname());
//		Elternteil e = d.addElternteil("Maria", "Musterman", "w", 800, "mmuster", "mpass");
//		Kind k = d.addKind("Max", "Musterman", Calendar.getInstance(), 2, e);
//		d.addKindToGruppe(k, d.getGruppeByID(1));
//		System.out.println(d.getBundeslandById(1).getKrzl());
		System.out.println(d.getPriceByKindID(1,2));
		d.disconnect();
		

	}

}
