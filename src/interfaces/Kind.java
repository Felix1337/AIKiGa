package interfaces;

import java.util.Calendar;

public interface Kind {

	public String getVorname();

	public String getNachname();

	public Integer getId();
        
        public Calendar getGeburtsDatum();
        
        public Elternteil getElternteil();
	
	public String toString();
	
	public Object[] getStats();

	public int getFamilie();
}
