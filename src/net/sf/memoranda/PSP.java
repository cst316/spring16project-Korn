package net.sf.memoranda;

import java.util.Collections;

public interface PSP {
	
	void setID();
	
	String getId();
	
	void setEstimated(double estimated);
	
	double getEstimated();
	
	void setActual(double actual);
	
	double getActual();
	
	void setToDate(double toDate);
	
	double getToDate();
	
	nu.xom.Element getContent();
	
	String getParentId();
	
	
}
