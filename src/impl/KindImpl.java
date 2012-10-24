package impl;

import interfaces.Elternteil;
import interfaces.Kind;
import java.util.Calendar;

public class KindImpl implements Kind{


	private String vorname;
	private String nachname;
	private Integer id;
	private Integer familie;
        private Calendar geburtsdatum;
        private Elternteil elternteil;
	

	public KindImpl(String vorname,String nachname,Calendar geburtsdatum, int id, int familie, Elternteil e) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.id = id;
		this.familie = familie;
                this.geburtsdatum = geburtsdatum;
                elternteil = e;
	}

	@Override
	public String getVorname() {
		return vorname;
	}
	@Override
	public String getNachname() {
		return nachname;
	}

	@Override
	public Integer getId() {
		return id;
	}
        
        @Override
        public Calendar getGeburtsDatum() {
            return geburtsdatum;
        }
        
        @Override
        public Elternteil getElternteil() {
            return elternteil;
        }
	
	@Override
	public String toString() {
		return vorname + " " + nachname;
	}
	@Override
	public Object[] getStats() {
		return new Object[]{"ID: " + id,"Vorname: " + vorname,"Nachname: " + nachname};
	}
	@Override
	public int getFamilie() {
		return familie;
	}
}