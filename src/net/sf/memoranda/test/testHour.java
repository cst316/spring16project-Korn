package net.sf.memoranda.test;

import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.App;
import net.sf.memoranda.ui.EventDialog;
import net.sf.memoranda.util.Local;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Duke on 3/2/2016.
 */
public class testHour {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInit() {
        fail("Not yet implemented");
    }

    @Test
    public void testEventHour() {
        int repeatType = 5;
        EventDialog dialog = new EventDialog(App.getFrame(), Local.getString("New event"));
        CalendarDate startDate = CalendarDate.today();
        CalendarDate endDate = CalendarDate.tomorrow();
        int period=  (Integer) dialog.dayOfMonthSpin.getModel().getValue();
        int hour = 10;
        int minute = 0;
        String text = "hello";
        boolean workDays = dialog.workingDaysOnlyCB.isSelected();
        EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour, minute, text, workDays);
        assertNotNull(EventsScheduler.getScheduledEvents());

    }
}
