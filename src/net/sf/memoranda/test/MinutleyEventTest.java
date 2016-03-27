package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.util.Vector;

import net.sf.memoranda.Event;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.App;
import net.sf.memoranda.ui.EventDialog;
import net.sf.memoranda.util.Local;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MinutleyEventTest {

	int repeatType, period, hour, minute;
	String text;
	CalendarDate startDate, endDate;
	EventDialog dialog;
	boolean workDays;

// sets up the variable to create a new repeatable event
	@Before
	public void setUp() throws Exception {
		repeatType = 6;
		dialog = new EventDialog(App.getFrame(), Local.getString("New event"));
		startDate = CalendarDate.today();
		endDate = CalendarDate.tomorrow();
		period = 1;
		hour = 7;
		minute = 14;
		text = "hello";
		workDays = dialog.workingDaysOnlyCB.isSelected();
	}
// creates a new repeatableMinute test and compares the period of when the notify panel will pop up
	@Test
	public void testEventMinutePeriod() {
		EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour, minute, text, workDays);
		// all current events
		Vector events= (Vector)EventsManager.getActiveEvents();
		//gets the event we created
		Event event= (Event) events.get(events.size() - 1);
		// sends to eventMinute and gets the current time and checks when the next notify pop will pop up.
		EventsScheduler.eventMinute(event);

		// ensures time we set to timer is the same
		assertEquals(1, event.getPeriod());
	}

	@Test
	public void testEventMinuteExists() {
		int beforeAdded = EventsScheduler.counter();
		EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour+1, minute+1, text, workDays);
		Vector events= (Vector)EventsManager.getActiveEvents();
		Event event= (Event) events.get(events.size() - 1);
		EventsScheduler.eventMinute(event);
		int afterAdded = EventsScheduler.counter();

		// ensures that event was added to the timer vector
		assertFalse(beforeAdded == afterAdded);
	}

}
