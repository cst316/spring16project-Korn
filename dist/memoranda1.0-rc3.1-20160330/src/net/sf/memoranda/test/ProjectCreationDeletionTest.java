package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.AgendaPanel;

public class ProjectCreationDeletionTest {
	
	int allProj;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ProjectManager.createProject("1", "Test Project", CalendarDate.today(), null);
	}

	@Before
	public void setUp() throws Exception {
		allProj = ProjectManager.getAllProjectsNumber();
	}

	/**
	 * Test creation with no end date
	 */
	@Test
	public void testNoEndNewProject() {
		ProjectManager.createProject("2", "Test Project", CalendarDate.today(), null);
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
	}
	
	/**
	 * Test start and end date
	 */
	@Test
	public void testNormalNewProject() {
		ProjectManager.createProject("3", "Testing", CalendarDate.today(), CalendarDate.tomorrow());
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
	}
	
	/**
	 * Test no start date
	 */
	@Test
	public void testNoStartNewProject() {
		ProjectManager.createProject("4", "Another Test", null, CalendarDate.tomorrow());
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
	}

	/**
	 * Test creating a project with an empty name.
	 */
	@Test
	public void testNoNamedProject() {
		ProjectManager.createProject("5", "", null, CalendarDate.tomorrow());
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
	}
	
	/**
	 * Test End date is before start date.
	 */
	@Test
	public void testEndAfterStart() {
		ProjectManager.createProject("6", "Basic Test", CalendarDate.tomorrow(), CalendarDate.today());
		assertTrue(allProj+1 == ProjectManager.getAllProjectsNumber());
		Project testPrj = ProjectManager.getProject("6");
		assertTrue(testPrj.getEndDate().equals(testPrj.getStartDate()));
	}
	
	/**
	 * Tests an appropriate project deletion.
	 */
	@Test
	public void testNormalDeleteProject() {
		if(allProj < 1) {
			ProjectManager.createProject("Default", CalendarDate.today(), null);
		}
		ProjectManager.removeProject("1");
		assertTrue(allProj-1 == ProjectManager.getAllProjectsNumber());
	}
	
	/**
	 * Test removing a nonexistant project.
	 */
	@Test
	public void testDeleteFakeProject() {
			ProjectManager.removeProject("PLM22");
			assertTrue(allProj == ProjectManager.getAllProjectsNumber());
	}
}
