package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.AgendaPanel;

public class ProjectButtonListenersTest {
	
	int allProj;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ProjectManager.createProject("Test Project", CalendarDate.today(), null);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNewProjectListener() {
		allProj = ProjectManager.getAllProjectsNumber();
		ActionEvent e = new ActionEvent(new Object(), 0, null);
		AgendaPanel.newProjAction.actionPerformed(e);
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
	}

	@Test
	public void testDeleteProjectListener() {
		allProj = ProjectManager.getAllProjectsNumber();
		ActionEvent e = new ActionEvent(new Object(), 0, null);
		AgendaPanel.newProjAction.actionPerformed(e);
		assertTrue(allProj-1 == ProjectManager.getAllProjectsNumber());
	}	
}
