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
	  public PSPListImpl(Project prj){
		  _root = new Element("psplist");
		    _document = new Document(_root);
		    _project = prj;
	  }

	@Override
	public Project getProject() {
		return _project;
	}
	
	
	private void buildElements(Element parent) {
	    Elements els = parent.getChildElements("PSP");
	    for (int i = 0; i < els.size(); i++) {
	      Element el = els.get(i);
	      elements.put(el.getAttribute("id").getValue(), el);
	      buildElements(el);
	    }
	  }
	  
	  

	@Override
	public PSP getPSPTask(String id) {
		Util.debug("getting PSP "+ id);
		Util.debug(getPSPElement(id).toString());
		return new PSPImpl ((Element) getPSPElement(id), this);
		
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
	public void removePSPTask(PSP pspTask) {
		String parentPSPId= pspTask.getParentId();
		if(parentPSPId==null){
			_root.removeChild(pspTask.getContent());
		}
		else{
			Element parentNode = (Element) getPSPElement(parentPSPId);
			parentNode.removeChild(pspTask.getContent());
		}
		elements.remove(pspTask.getId());
		
	}

	@Override
	public double calculateToDatePercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public nu.xom.Element getPSPElement(String id) {
		Element el= elements.get(id);
		if(el== null){
			Util.debug("PSP " + id + " cannot be found in project " + _project.getTitle());
		}
		return el;
	}

}
