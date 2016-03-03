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

public class TestMinute {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEventMinute() {
		int repeatType = 6;
		EventDialog dialog = new EventDialog(App.getFrame(), Local.getString("New event"));
		CalendarDate startDate = CalendarDate.today();
		CalendarDate endDate = CalendarDate.tomorrow();
		int period = (Integer) dialog.dayOfMonthSpin.getModel().getValue();
		int hour = 5;
		int minute = 12;
		String text = "hello";
		boolean workDays = dialog.workingDaysOnlyCB.isSelected();
		Vector events= (Vector)EventsManager.getActiveEvents();
		Event event= (Event) events.get(0);
		EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour, minute, text, workDays);
		assertNotNull(EventsScheduler.getScheduledEvents());
		
	}

}
