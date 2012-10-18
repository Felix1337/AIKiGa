package impl;

import java.util.Calendar;
import java.util.List;

import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Rechnung;
import interfaces.Sonderleistung;
public class RechnungImpl implements Rechnung {
	
	private double betrag;
	private Kind kind;
	private Gruppe gruppe;
	private Kita kita;
	private Calendar datum;
	private int id;
	private List<Sonderleistung> sonderleistungen;
	
	public RechnungImpl(int id, double betrag, Kind kind, Gruppe gruppe, Kita kita, List<Sonderleistung> sonderleistungen){
		this.id = id;
		this.betrag = betrag;
		this.kind = kind;
		this.kita = kita;
		this.datum = Calendar.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getBetrag()
	 */
	@Override
	public double getBetrag(){
		double result = this.betrag;
//		for(Sonderleistung s : getSonderleistungen()){
//			result+=s.getPreis();
//		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getKind()
	 */
	@Override
	public Kind getKind(){
		return this.kind;
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getKita()
	 */
	@Override
	public Kita getKita(){
		return this.kita;
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getGruppe()
	 */
	@Override
	public Gruppe getGruppe(){
		return this.gruppe;
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getDatum()
	 */
	@Override
	public Calendar getDatum(){
		return this.datum;
	}
	
	/* (non-Javadoc)
	 * @see impl.Rechnung#getSonderleistungen()
	 */
	@Override
	public List<Sonderleistung> getSonderleistungen(){
		return this.sonderleistungen;
	}

}
