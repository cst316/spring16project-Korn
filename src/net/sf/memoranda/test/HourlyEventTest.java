package net.sf.memoranda.test;

import net.sf.memoranda.Event;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.EventDialog;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HourlyEventTest {

    int repeatType, period, hour, minute;
    String text;
    CalendarDate startDate, endDate;
    EventDialog dialog;
    boolean workDays;

    @Before
    // creates a new repeatableHour test and compares the period of when the notify panel will pop up
    public void setUp() throws Exception {
        repeatType = 5;
        //dialog = new EventDialog(App.getFrame(), Local.getString("New event"));
        startDate = CalendarDate.today();
        endDate = CalendarDate.tomorrow();
        period = 1;
        hour = 5;
        minute = 12;
        text = "hello";
        workDays = false;
        System.out.println("[DEBUG] Hourly Set Up complete");
    }

    @Test
    public void testEventHourPeriod() {
        System.out.println("[DEBUG] Starting testEventHourPeriod()");
        EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour, minute, text, workDays);
        System.out.println("[DEBUG] Valid Event Created");
        //all current events
        Vector events = (Vector) EventsManager.getActiveEvents();
        //gets the new event that was just created
        Event event = (Event) events.get(events.size() - 1);
        // sends to eventMinute and gets the current time and checks when the next notify pop will pop up.
        EventsScheduler.eventHour(event);
        System.out.println("[DEBUG] " + period + " = " + event.getPeriod());
        // ensures time we set to timer is the same
        assertEquals(period, event.getPeriod());
    }

    @Test
    public void testEventHourExists() {
        System.out.println("[DEBUG] Starting testEventHourExists()");
        int beforeAdded = EventsScheduler.counter();
        System.out.println("[DEBUG] " + beforeAdded);
        EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour + 1, minute + 1, text, workDays);
        System.out.println("[DEBUG] Valid Event Created");
        Vector events = (Vector) EventsManager.getActiveEvents();
        Event event = (Event) events.get(events.size() - 1);
        EventsScheduler.eventHour(event);
        int afterAdded = 0;
        afterAdded = EventsScheduler.counter();
        System.out.println("[DEBUG] " + beforeAdded + " != " + afterAdded);
        // ensures that event was added to the timer vector
        assertFalse(beforeAdded == afterAdded);
    }
}
