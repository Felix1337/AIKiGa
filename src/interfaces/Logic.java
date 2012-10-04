package interfaces;

import java.util.HashMap;

public interface Logic {

	//haw account und passwort m�ssen in der Klasse Passwort eingetragen werden
	//ich werd das in git auf ignore setzen, trotzdem bitte alle vorher testen ob das nicht doch mit gepusht wird
	//Bei den Variablennamen bitte an die vorgaben aus dem ER Model halten
	
	
	//Gibt eine HashMap<IDderKita,KitaValueClass> aller Kitas zur�ck
	public HashMap<Integer, Kita> getKitas();

	//Gibt eine HashMap<IDderGruppe,GruppeValueClass> aller Gruppen zur�ck
	public HashMap<Integer, Gruppe> getGruppen(Integer KitaId);

	//Gibt eine HashMap<IDdesKindes,KindValueClass> aller Kinder zur�ck
	public HashMap<Integer, Kind> getKinder(Integer GruppeId);
	
	//Pr�ft ob in der gew�nschten Gruppe noch ein platz frei ist
	public boolean isPlatzFrei(Integer GruppeId);

	//tr�gt ein Kind in eine Gruppe ein. warteschlange gibt an, ob das kind in die warteschlange der jeweiligen gruppe kommt
	//gehalt ist das gehalt der beiden eltern(darf nur 2 nachkommastellen haben)
	public boolean kindEintragen(Kind k, Gruppe gr, boolean warteschlange, double gehalt);
	
	//eine Logdatei mit dem ermittelten Preis wird als Rechnung.txt angelegt
	public void rechnungDrucken(Integer KindId);
	
	//berechenet anhand der storedProcedure den noch zu zahlenden beitrag
	//(eventuell muss man hier noch ein eintrittsdatum des kindes mit in der db f�hren?)
	double preisErmitteln(Integer KindId);
	
}
