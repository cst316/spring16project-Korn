/**
 *
 */
package net.sf.memoranda.test;

import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.AgendaPanel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Daniel McEvoy; Jordan Partridge
 */
public class TestAgendaRefresh {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        AgendaPanel.setRefreshCount(0);

    }

    @Test
    public void testNewProjectRefresh() {
        CalendarDate startDate = new CalendarDate(1, 2, 4);
        CalendarDate endDate = new CalendarDate(3, 4, 4);
        ProjectManager.createProject("My Project", startDate, endDate);

        ProjectManager.createProject("My Project2", startDate, endDate);

        ProjectManager.createProject("My Project3", startDate, endDate);
        ProjectManager.createProject("My Project4", startDate, endDate);

        ProjectManager.createProject("My Project5", startDate, endDate);

        ProjectManager.createProject("My Project6", startDate, endDate);
        assert AgendaPanel.getRefreshCount() == 6;


    }

    @Test
    public void testDeleteProjectRefresh() {
        AgendaPanel.setRefreshCount(0);
        ProjectManager.removeProject("My Project6");
        ProjectManager.removeProject("My Project5");
        ProjectManager.removeProject("My Project4");
        ProjectManager.removeProject("My Project3");
        ProjectManager.removeProject("My Project2");
        assert (AgendaPanel.getRefreshCount() == 5);
    }

    //    @Test
    //    public void testNewTaskRefresh() {
    //        AgendaPanel.setRefreshCount(0);
    //        CalendarDate startDate = new CalendarDate(1, 2, 4);
    //        CalendarDate endDate = new CalendarDate(3,4,4);
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task",4,4, "", "1");
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task1",4,4, "", "1");
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task2",4,4, "", "1");
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task3",4,4, "", "1");
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task4",4,4, "", "1");
    //        CurrentProject.getTaskList().createTask(startDate, endDate, "task5",4,4, "", "1");
    //        assert(AgendaPanel.getRefreshCount() == 6);
    //
    //    }

    @Test
    public void testDateChangeRefresh() {
        //fail("Not yet implemented");
    }

}
