package net.sf.memoranda;

public interface PSPList {
	Project getProject();
	
	PSP getPSPTask(String id);
	
	PSP createPSPTask(double estimated, double actual, double toDate);
	
	void removePSPTask(PSP pspTask);
	
	public double calculateToDatePercentage();
	
	nu.xom.Element getPSPElement(String Id);

}
