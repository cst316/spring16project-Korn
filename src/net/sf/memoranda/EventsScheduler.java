/**
 * EventsScheduler.java
 * Created on 10.03.2003, 20:20:08 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Local;
//import sun.util.resources.cldr.aa.CalendarData_aa_DJ;

import java.util.*;

/**
 *
 */
/*$Id: EventsScheduler.java,v 1.4 2004/01/30 12:17:41 alexeya Exp $*/
public class EventsScheduler {

    static Vector _timers = new Vector();
    static Vector _listeners = new Vector();
    static Timer changeDateTimer = new Timer();

    static {
        addListener(new DefaultEventNotifier());
    }

    public static void init() {
        cancelAll();
        //changeDateTimer.cancel();
        Vector events = (Vector) EventsManager.getActiveEvents();
        _timers = new Vector();

        /*DEBUG*/
        System.out.println("----------");

        for (int i = 0; i < events.size(); i++) {
            Event event = (Event) events.get(i);
            Date eventTime = event.getTime();

            /*DEBUG*/
            System.out.println((Calendar.getInstance()).getTime());

            if (eventTime.after((Calendar.getInstance().getTime()))) {
/*
            	Calendar calendar = new GregorianCalendar(Local.getCurrentLocale());
                // replace with event.getStartDate() when includes minute & hour
                CalendarDate date = CalendarDate.today();
                int minutes, hours,day;
               
                
*/           
            	 EventTimer timer=null;
            	if (event.getRepeat() == EventsManager.REPEAT_MINUTELY) {
            		eventMinute(event);
            	}
            	
            	else if (event.getRepeat() == EventsManager.REPEAT_HOURLY) {
            		eventHour(event);
            	}
                else {
                    timer = new EventTimer(event);
                    timer.schedule(new NotifyTask(timer), event.getTime());
                    _timers.add(timer);
                }
/*               

                if (event.getRepeat() == EventsManager.REPEAT_MINUTELY) {
                    // get remaining minutes from now until next cycle
                    timer = new EventTimer(event);
                    int now = date.getCalendar().get(Calendar.MINUTE);
                    int difference = now % event.getPeriod() + 1;

                    minutes = calendar.get(Calendar.MINUTE) + difference;
                    hours = calendar.get(Calendar.HOUR_OF_DAY);
                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minutes);
                    calendar.set(Calendar.SECOND, 0);
                    timer.schedule(new NotifyTask(timer), calendar.getTime());
                    _timers.add(timer);

                    // then get the rest until midnight
                    while (calendar.getTime().getTime() < getMidnight().getTime()) {
                        timer = new EventTimer(event);
                        minutes += event.getPeriod();
                        if (minutes > 60) {
                            calendar.set(Calendar.HOUR_OF_DAY, ++hours);
                            minutes %= 60;
                        }
                        calendar.set(Calendar.MINUTE, minutes);
                        timer.schedule(new NotifyTask(timer), calendar.getTime());
                        _timers.add(timer);
                    }
                }

                
                else if (event.getRepeat() == EventsManager.REPEAT_HOURLY) {
                    // get remaining minutes from now until next cycle
                	timer= new EventTimer(event);
                	int now=date.getCalendar().get(Calendar.HOUR);
                	int difference =now% event.getPeriod()+1;
                	
                	hours = calendar.get(Calendar.HOUR)+difference;
                	day= calendar.get(Calendar.DAY_OF_WEEK);
                	calendar.set(Calendar.DAY_OF_WEEK, day);
                	calendar.set(Calendar.MINUTE,0);
                	timer.schedule(new NotifyTask(timer), calendar.getTime());
                	_timers.add(timer);
                    // then get hours from now until midnight
                	while(calendar.getTime().getTime()<getMidnight().getTime()){
                		timer= new EventTimer(event);
                		hours += event.getPeriod();
                		if(hours> 24){
                			calendar.set(Calendar.DAY_OF_WEEK,++day);
                			hours%=24;
                		}
                		calendar.set(Calendar.HOUR, hours);
                		timer.schedule(new NotifyTask(timer), calendar.getTime());
                	}
                }
*/


                /*DEBUG*/
                System.out.println(event.getTimeString());
            }
        }


        /*DEBUG*/
        System.out.println("----------");

        Date midnight = getMidnight();
        changeDateTimer.schedule(new TimerTask() {
            public void run() {
                init();
                this.cancel();
            }
        }, midnight);
        notifyChanged();
    }
    public static void eventMinute(Event event){
    	Calendar calendar = new GregorianCalendar(Local.getCurrentLocale());
        // replace with event.getStartDate() when includes minute & hour
        CalendarDate date = CalendarDate.today();
        int minutes, hours;
        EventTimer timer;
    	
	
        // get remaining minutes from now until next cycle
        timer = new EventTimer(event);
        int now = date.getCalendar().get(Calendar.MINUTE);
        int difference = now % event.getPeriod() + 1;

        minutes = calendar.get(Calendar.MINUTE) + difference;
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        timer.schedule(new NotifyTask(timer), calendar.getTime());
//    	System.out.println(" DEBUG top: " +_timers.size());
    	_timers.add(timer);
//    	System.out.println(" DEBUG mid: " +_timers.size());

        // then get the rest until midnight
        while (calendar.getTime().getTime() < getMidnight().getTime()) {
            timer = new EventTimer(event);
            minutes += event.getPeriod();
            if (minutes > 60) {
                calendar.set(Calendar.HOUR_OF_DAY, ++hours);
                minutes %= 60;
            }
            calendar.set(Calendar.MINUTE, minutes);
            timer.schedule(new NotifyTask(timer), calendar.getTime());
            _timers.add(timer);
        }
//    	System.out.println(" DEBUG end: " +_timers.size());
    }
    
    public static void eventHour(Event event){
    	Calendar calendar = new GregorianCalendar(Local.getCurrentLocale());
        // replace with event.getStartDate() when includes minute & hour
        CalendarDate date = CalendarDate.today();
        int hours, day;
        EventTimer timer;
    	
    	
            // get remaining minutes from now until next cycle
        	timer= new EventTimer(event);
        	int now=date.getCalendar().get(Calendar.HOUR);
        	int difference =now% event.getPeriod()+1;
        	
        	hours = calendar.get(Calendar.HOUR)+difference;
        	day= calendar.get(Calendar.DAY_OF_WEEK);
        	calendar.set(Calendar.DAY_OF_WEEK, day);
        	calendar.set(Calendar.MINUTE,0);
        	timer.schedule(new NotifyTask(timer), calendar.getTime());
//        	System.out.println(" DEBUG top: " +_timers.size());
        	_timers.add(timer);
//        	System.out.println(" DEBUG mid: " +_timers.size());
            // then get hours from now until midnight
        	while(calendar.getTime().getTime()<getMidnight().getTime()){
        		timer= new EventTimer(event);
        		hours += event.getPeriod();
        		if(hours> 24){
        			calendar.set(Calendar.DAY_OF_WEEK,++day);
        			hours%=24;
        		}
        		calendar.set(Calendar.HOUR, hours);
        		timer.schedule(new NotifyTask(timer), calendar.getTime());
                _timers.add(timer);
        	}
//        	System.out.println(" DEBUG end: " +_timers.size());
    }
    public static void cancelAll() {
        for (int i = 0; i < _timers.size(); i++) {
            EventTimer timer = (EventTimer) _timers.get(i);
            timer.cancel();
        }
    }
    
    public static Vector getScheduledEvents() {
        Vector vector = new Vector();

        for (int i = 0; i < _timers.size(); i++)
            vector.add(((EventTimer) _timers.get(i)).getEvent());

        return vector;
    }

    public static Event getFirstScheduledEvent() {
        if (!isEventScheduled())
            return null;

        Event firstEvent = ((EventTimer) _timers.get(0)).getEvent();

        for (int i = 1; i < _timers.size(); i++) {
            Event event = ((EventTimer) _timers.get(i)).getEvent();
            if (event.getTime().before(firstEvent.getTime()))
                firstEvent = event;
        }

        return firstEvent;
    }
    public static int counter(){
    	return _timers.size();
    }
    public static void addListener(EventNotificationListener enl) {
        _listeners.add(enl);
    }

    public static boolean isEventScheduled() {
        return _timers.size() > 0;
    }

    private static void notifyListeners(Event ev) {
        for (int i = 0; i < _listeners.size(); i++)
            ((EventNotificationListener) _listeners.get(i)).eventIsOccured(ev);
    }

    private static void notifyChanged() {
        for (int i = 0; i < _listeners.size(); i++)
            ((EventNotificationListener) _listeners.get(i)).eventsChanged();
    }

    private static Date getMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    static class NotifyTask extends TimerTask {
        EventTimer _timer;

        public NotifyTask(EventTimer t) {
            super();
            _timer = t;
        }

        public void run() {
            _timer.cancel();
            _timers.remove(_timer);
            notifyListeners(_timer.getEvent());
            notifyChanged();
        }
    }

    static class EventTimer extends Timer {
        Event _event;

        public EventTimer(Event event) {
            super();
            _event = event;
        }

        public Event getEvent() {
            return _event;
        }
    }

}