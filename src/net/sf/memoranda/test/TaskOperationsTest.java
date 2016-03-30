/**
 * 
 */
package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nu.xom.Attribute;
import nu.xom.Element;
import net.sf.memoranda.CurrentProject;
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
		assertTrue(testTL.getTask(t.getId()).equals(t));
	}

	@Test
	public void testRecurringTaskCreation() {
		Calendar temp = CalendarDate.toCalendar(CalendarDate.today().getDay(), CalendarDate.today().getMonth(), CalendarDate.today().getYear());
		CalendarDate dateDay = new CalendarDate(temp).tomorrow();
		temp.add(temp.DATE, 7);
		CalendarDate dateWeek = new CalendarDate(temp);
		//TODO
		CalendarDate dateMonth = new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(), 
				(CalendarDate.today().getMonth() + 1) % 12, CalendarDate.today().getYear()));
		temp.add(temp.DATE, -7);
		temp.add(temp.DATE, 365);
		CalendarDate dateYear = new CalendarDate(temp);
		Task t1 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"ext", 1, 2, "This is a description!", null, false, 10, 1, false, dateDay);	
		Task t2 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"txt", 1, 2, "This is a description!", null, false, 10, 2, false, dateWeek);
		Task t3 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"tet", 1, 2, "This is a description!", null, false, 10, 3, false, dateMonth);
		Task t4 = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"tex", 1, 2, "This is a description!", null, false, 10, 4, false, dateYear);
		// TODO Need to first determine how I'm going to make recurring tasks.
		assertTrue(t1.isRepeatable() && t2.isRepeatable() && t3.isRepeatable() && t4.isRepeatable());
		Vector<Task> vTask = (Vector<Task>) CurrentProject.getTaskList().getRepeatableTaskforDate(dateDay);
		//assertTrue(vTask.get(0).getText() == t1.getText());
		vTask = (Vector<Task>) CurrentProject.getTaskList().getRepeatableTaskforDate(dateWeek);
		//assertTrue(vTask.get(0).getText() == t2.getText());
		vTask = (Vector<Task>) CurrentProject.getTaskList().getRepeatableTaskforDate(dateMonth);
		//assertTrue(vTask.get(0).getText() == t3.getText());
		vTask = (Vector<Task>) CurrentProject.getTaskList().getRepeatableTaskforDate(dateYear);
		//assertTrue(vTask.get(0).getText() == t4.getText());
	}
	
	@Test
	public void testNoEndDateCreation() {
		Task t = testTL.createTask(CalendarDate.today(), null, "text property",
				1, 2, "This is a description!", null, false, 10, 0, false, null);
		assertTrue(t.equals(testTL.getTask(t.getId())));
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
		Task t = testTL.createTask(CalendarDate.today(), CalendarDate.today(),
				"text", 1, 2, "This is a description!", null, true, 10, 4, false, null);
		assertTrue(t.getWorkingDaysOnly());
	}
}
