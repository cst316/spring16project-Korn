/**
 *
 */
package net.sf.memoranda.test;

import net.sf.memoranda.*;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel McEvoy
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
        Task t;
        Stack<Object> taskCreationParams = new Stack<Object>();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("text property");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(0);
        taskCreationParams.add(false);
        taskCreationParams.add(null);
        taskCreationParams.add("");
        t = testTL.createTask(taskCreationParams);
        
        assertTrue(testTL.getTask(t.getId()).equals(t));
    }

    @Test
    public void testRecurringTaskCreation() {
        Calendar temp = CalendarDate.toCalendar(CalendarDate.today().getDay(), CalendarDate.today().getMonth(), CalendarDate.today().getYear());
        CalendarDate dateDay = new CalendarDate(temp).tomorrow();
        temp.add(temp.DATE, 7);
        CalendarDate dateWeek = new CalendarDate(temp);
        CalendarDate dateMonth = new CalendarDate(CalendarDate.toDate(CalendarDate.today().getDay(),
                (CalendarDate.today().getMonth() + 1) % 12, CalendarDate.today().getYear()));
        temp.add(temp.DATE, -7);
        temp.add(temp.DATE, 365);
        CalendarDate dateYear = new CalendarDate(temp);

        Task t1;
        Stack<Object> taskCreationParams = new Stack<Object>();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("t1");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(1);
        taskCreationParams.add(false);
        taskCreationParams.add(dateDay);
        taskCreationParams.add("testing");
        t1 = testTL.createTask(taskCreationParams);
        
        Task t2;
        taskCreationParams.clear();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("t2");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(2);
        taskCreationParams.add(false);
        taskCreationParams.add(dateWeek);
        taskCreationParams.add("coding");
        t2 = testTL.createTask(taskCreationParams);
      
        Task t3;
        taskCreationParams.clear();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("t3");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(3);
        taskCreationParams.add(false);
        taskCreationParams.add(dateMonth);
        taskCreationParams.add("design");
        t3 = testTL.createTask(taskCreationParams);
        
        Task t4;
        taskCreationParams.clear();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("t4");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(4);
        taskCreationParams.add(false);
        taskCreationParams.add(dateYear);
        taskCreationParams.add("planning");
        t4 = testTL.createTask(taskCreationParams);       

        assertTrue(t1.isRepeatable() && t2.isRepeatable() && t3.isRepeatable() && t4.isRepeatable());
    }

    @Test
    public void testNoEndDateCreation() {
        Task t;
        Stack<Object> taskCreationParams = new Stack<Object>();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(null);
        taskCreationParams.add("text property");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(false);
        taskCreationParams.add(10);
        taskCreationParams.add(0);
        taskCreationParams.add(false);
        taskCreationParams.add(null);
        taskCreationParams.add("testing");
        t = testTL.createTask(taskCreationParams);
        
        assertTrue(t.equals(testTL.getTask(t.getId())));
    }

    @Test
    public void testInvalidStartCreation() {
        Task t = null;
        try {
            Stack<Object> taskCreationParams = new Stack<Object>();
            taskCreationParams.add(null);
            taskCreationParams.add(null);
            taskCreationParams.add("text property");
            taskCreationParams.add(1);
            taskCreationParams.add(2);
            taskCreationParams.add("This is a description!");
            taskCreationParams.add(null);
            taskCreationParams.add(false);
            taskCreationParams.add(10);
            taskCreationParams.add(0);
            taskCreationParams.add(false);
            taskCreationParams.add(null);
            taskCreationParams.add("testing");
            t = testTL.createTask(taskCreationParams);
        } catch (Exception e) {
            assertTrue(t == null);
        }
    }

    @Test
    public void testWorkingDaysOnlyRecurCreation() {

        Task t;
        Stack<Object> taskCreationParams = new Stack<Object>();
        taskCreationParams.add(CalendarDate.today());
        taskCreationParams.add(CalendarDate.tomorrow());
        taskCreationParams.add("text property");
        taskCreationParams.add(1);
        taskCreationParams.add(2);
        taskCreationParams.add("This is a description!");
        taskCreationParams.add(null);
        taskCreationParams.add(true);
        taskCreationParams.add(10);
        taskCreationParams.add(4);
        taskCreationParams.add(false);
        taskCreationParams.add(null);
        taskCreationParams.add("testing");
        t = testTL.createTask(taskCreationParams);
        assertTrue(t.getWorkingDaysOnly());
    }
}
