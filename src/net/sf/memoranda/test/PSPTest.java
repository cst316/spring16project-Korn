package net.sf.memoranda.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.memoranda.PSP;
import net.sf.memoranda.PSPList;
import net.sf.memoranda.PSPListImpl;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectImpl;
import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;


public class PSPTest {

	PSPList testPSP;


	@Before
	public void setUp() throws Exception {
		String id = Util.generateId();
		Element elem = new Element("project");
        elem.addAttribute(new Attribute("id", id));
		Project testPrj = new ProjectImpl(elem);
		testPSP = new PSPListImpl(testPrj);
	}

	@Test
	public void testPSPCreaton(){
		PSP t= testPSP.createPSPTask(12.12, 9.0, 16.0);
		assertTrue(testPSP.getPSPTask(t.getId())!=null);
		
	}

}
