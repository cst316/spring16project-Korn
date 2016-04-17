package net.sf.memoranda;

import nu.xom.Attribute;
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
	public void setEstimated(double estimated) {
		setAttr("Estimated", Double.toString(estimated));
		
	}

	@Override
	public String getEstimated() {
		return element.getFirstChildElement("Estimated").getValue();
	}

	@Override
	public void setActual(double actual) {
		setAttr("Actual",Double.toString(actual));
		
	}

	@Override
	public String getActual() {
		return element.getFirstChildElement("Actual").getValue();
	}

	@Override
	public void setToDate(double toDate) {
		setAttr("To Date",Double.toString(toDate));
		
	}

	@Override
	public String getToDate() {
		return element.getFirstChildElement("To Date").getValue();
	}
	
	public Element getContent(){
		return element;
	}
	
	public String getParentId(){
		return null;
	}
	public String getId(){
		return element.getAttribute("id").getValue();
	}
	
	private void setAttr(String a, String value) {
        Attribute attr = element.getAttribute(a);
        if (attr == null) {
           element.addAttribute(new Attribute(a, value));
        } else {
            attr.setValue(value);
        }
    }



}
