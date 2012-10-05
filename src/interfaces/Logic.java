package interfaces;

import java.util.ArrayList;

public interface Logic {

	//haw account und passwort müssen in der Klasse Passwort eingetragen werden
	//ich werd das in git auf ignore setzen, trotzdem bitte alle vorher testen ob das nicht doch mit gepusht wird
	
	//Hab ein bisschen was geändert, listen sind doch besser
	
	
	//Gibt eine ArrayListKitaValueClass> aller Kitas zurück
	public ArrayList<Kita> getKitas();

	//Gibt eine ArrayList<GruppeValueClass> aller Gruppen zurück
	public ArrayList<Gruppe> getGruppen(Integer KitaId);

	//Gibt eine ArrayListKindValueClass> aller Kinder zurück
	public ArrayList<Kind> getKinder(Integer GruppeId);
	
	//Prüft ob in der gewünschten Gruppe noch ein platz frei ist
	public boolean isPlatzFrei(Integer kitaId,Integer GruppeId);

	//trägt ein Kind in eine Gruppe ein. warteschlange gibt an, ob das kind in die warteschlange der jeweiligen gruppe kommt
	//gehalt ist das gehalt der beiden eltern(darf nur 2 nachkommastellen haben)
	public boolean kindEintragen(String vorname,String nachname,Integer kitaId,Integer GruppeId, boolean warteschlange, double gehalt);
	
	//eine Logdatei mit dem ermittelten Preis wird als Rechnung.txt angelegt
	public void rechnungDrucken(Integer KindId);
	
	//berechenet anhand der storedProcedure den noch zu zahlenden beitrag
	//(eventuell muss man hier noch ein eintrittsdatum des kindes mit in der db führen?)
	double preisErmitteln(Integer KindId);
	
}
