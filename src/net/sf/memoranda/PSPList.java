package net.sf.memoranda;

public interface PSPList {
	Project getProject();
	
	PSP getPSPTask(String id);
	
	PSP createPSPTask(double estimated, double actual, double toDate);
	
	PSP removePSPTask(String id);
	
	public double calculateToDatePercentage();
	
	nu.xom.Element getTaskElement(String Id);

}
