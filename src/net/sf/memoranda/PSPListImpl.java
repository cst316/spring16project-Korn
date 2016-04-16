package net.sf.memoranda;

import java.util.Hashtable;
import java.util.Stack;

import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class PSPListImpl implements PSPList {
	
	
	 private Project _project = null;
	  private nu.xom.Document _document = null;
	  private static Stack<PSP> tempPSP = new Stack<PSP>();
	  static nu.xom.Element _root = null;
	  
	  private Hashtable<String, nu.xom.Element> elements = new Hashtable<String, Element>();
	  
	  public PSPListImpl(Document doc, Project prj){
		  _document = doc;
		    _root = _document.getRootElement();
		    _project = prj;
		    buildElements(_root);
	  }

	@Override
	public Project getProject() {
		return _project;
	}
	
	
	private void buildElements(Element parent) {
	    Elements els = parent.getChildElements("task");
	    for (int i = 0; i < els.size(); i++) {
	      Element el = els.get(i);
	      elements.put(el.getAttribute("id").getValue(), el);
	      buildElements(el);
	    }
	  }
	  
	  

	@Override
	public PSP getPSPTask(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public PSP createPSPTask(double estimated, double actual, double toDate) {
		Element pspElement= new Element("psp");
		String id= Util.generateId();
		pspElement.addAttribute(new Attribute("id",id));
		PSP psp= new PSPImpl(pspElement, this);
		psp.setEstimated(estimated);
		psp.setActual(actual);
		psp.setToDate(toDate);
		
		elements.put(id, psp.getContent());
		
		return new PSPImpl(psp.getContent(),this);
	}

	@Override
	public PSP removePSPTask(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double calculateToDatePercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Element getTaskElement(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

}
