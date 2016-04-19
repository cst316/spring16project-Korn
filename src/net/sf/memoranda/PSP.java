package net.sf.memoranda;

import java.util.Collections;

public interface PSP {
	
	
	String getId();
	
	void setEstimated(double estimated);
	
	String getEstimated();
	
	void setActual(double actual);
	
	String getActual();
	
	void setToDate(double toDate);
	
	String getToDate();
	
	nu.xom.Element getContent();
	
	String getParentId();
	
	
}
