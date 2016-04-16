package net.sf.memoranda;

import java.util.Collections;

public interface PSP {
	
	String setID();
	
	void setEstimated(double estimated);
	
	double getEstimated();
	
	void setActual(double actual);
	
	double getActual();
	
	void setToDate(double toDate);
	
	double getToDate();
	
	nu.xom.Element getContent();
	
	
}
