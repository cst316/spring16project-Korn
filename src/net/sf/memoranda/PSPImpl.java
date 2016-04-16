package net.sf.memoranda;

import nu.xom.Element;

public class PSPImpl implements PSP, Comparable {
	
	private nu.xom.Element element = null;
	private PSPList pspList=null;
	
	public PSPImpl(Element PSPElement, PSPList pspL){
		element = PSPElement;
		pspList= pspL;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String setID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEstimated(double estimated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getEstimated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setActual(double actual) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getActual() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setToDate(double toDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getToDate() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Element getContent(){
		return element;
	}



}
