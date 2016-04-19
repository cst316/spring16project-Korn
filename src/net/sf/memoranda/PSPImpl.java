package net.sf.memoranda;

import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;

public class PSPImpl implements PSP {
	
	private nu.xom.Element element = null;
	private PSPList pspList=null;
	
	public PSPImpl(Element PSPElement, PSPList pspL){
		element = PSPElement;
		pspList= pspL;
	}


	@Override
	public void setEstimated(double estimated) {
		setAttr("estimated", Double.toString(estimated));
		
	}

	@Override
	public String getEstimated() {
		return element.getFirstChildElement("estimated").getValue();
	}

	@Override
	public void setActual(double actual) {
		setAttr("actual", Double.toString(actual));
		
	}

	@Override
	public String getActual() {
		Util.debug(element.getFirstChildElement("actual").getValue());
		return "found";
	}

	@Override
	public void setToDate(double toDate) {
		setAttr("toDate",Double.toString(toDate));
		
	}

	@Override
	public String getToDate() {
		return element.getFirstChildElement("toDate").getValue();
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
