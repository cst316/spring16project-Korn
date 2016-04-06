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

        assertTrue(t1.isRepeatable() && t2.isRepeatable() && t3.isRepeatable() && t4.isRepeatable());
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
