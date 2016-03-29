/**
 * 
 */
package net.sf.memoranda.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nu.xom.Attribute;
import nu.xom.Element;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectImpl;
import net.sf.memoranda.Task;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.TaskListImpl;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

/**
 * @author Daniel McEvoy
 *
 */
public class TaskOperationsTest {

	TaskList testTL;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		String id = Util.generateId();
		Element elem = new Element("project");
        elem.addAttribute(new Attribute("id", id));
		Project testPrj = new ProjectImpl(elem);
		testTL = new TaskListImpl(testPrj);
	}

	@Test
	public void testRegTaskCreation() {
		Task t = testTL.createTask(CalendarDate.today(), CalendarDate.tomorrow(), "text property",
				1, 2, "This is a description!", null, false, 10, 0, false, null);
		assertTrue(testTL.getTask(t.getID()).equals(t));
	}

	@Test
	public void testRecurringTaskCreation() {
		Task t1 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"ext", 1, 2, "This is a description!", null, false, 10, 1, false,
				new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(), (CalendarDate.today().getMonth() + 2) % 12, CalendarDate.today().getYear())));	
		Task t2 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"txt", 1, 2, "This is a description!", null, false, 10, 2, false,
				new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(), (CalendarDate.today().getMonth() + 2) % 12, CalendarDate.today().getYear())));
		Task t3 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"tet", 1, 2, "This is a description!", null, false, 10, 3, false,
				new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(), (CalendarDate.today().getMonth() + 2) % 12, CalendarDate.today().getYear())));
		Task t4 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"tex", 1, 2, "This is a description!", null, false, 10, 4, false,
				new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(), (CalendarDate.today().getMonth() + 2) % 12, CalendarDate.today().getYear())));
		// TODO Need to first determine how I'm going to make recurring tasks.
		assertTrue(true);
	}
	
	@Test
	public void testNoEndDateCreation() {
		Task t = testTL.createTask(CalendarDate.today(), null, "text property",
				1, 2, "This is a description!", null, false, 10, 0, false, null);
		assertTrue(t.equals(testTL.getTask(t.getID())));
	}
	
	@Test
	public void testInvalidStartCreation() {
		Task t = null;
		try {
			t = testTL.createTask(null, null, "text property",
					1, 2, "This is a description!", null, false, 10, 0, false, null);
		} catch (Exception e) {
			assertTrue(t == null);
		}
	}
	
	@Test
	public void testWorkingDaysOnlyRecurCreation() {
		Task t4 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"tex", 1, 2, "This is a description!", null, false, 10, 4, true, null);
		// TODO Need to determine how to create recurring tasks first.
		assertTrue(true);
	}
}
