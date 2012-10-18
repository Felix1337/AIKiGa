package interfaces;


import java.util.Calendar;
import java.util.List;

public interface Rechnung {

	public abstract double getBetrag();

	public abstract Kind getKind();

	public abstract Kita getKita();

	public abstract Gruppe getGruppe();

	public abstract Calendar getDatum();

	public abstract List<Sonderleistung> getSonderleistungen();

}