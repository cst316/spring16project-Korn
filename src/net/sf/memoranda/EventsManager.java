/**
 * EventsManager.java Created on 08.03.2003, 12:35:19 Alex Package:
 * net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003
 * Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Util;
import nu.xom.*;

import java.util.*;

//import nu.xom.Comment;

/**
 *
 */
/*$Id: EventsManager.java,v 1.11 2004/10/06 16:00:11 ivanrise Exp $*/
public class EventsManager {
  /*    public static final String NS_JNEVENTS =
            "http://www.openmechanics.org/2003/jnotes-events-file";
   */
  public static final int NO_REPEAT = 0;
  public static final int REPEAT_DAILY = 1;
  public static final int REPEAT_WEEKLY = 2;
  public static final int REPEAT_MONTHLY = 3;
  public static final int REPEAT_YEARLY = 4;
  public static final int REPEAT_HOURLY = 5;
  public static final int REPEAT_MINUTELY = 6;

  public static Document _doc = null;
  static Element _root = null;

  static {
    CurrentStorage.get().openEventsManager();

    if (_doc == null) {
      _root = new Element("eventslist");
      _doc = new Document(_root);
    } else {
      _root = _doc.getRootElement();
    }
  }

  public static void createSticker(String text, int prior) {
    Element sticker = new Element("sticker");
    sticker.addAttribute(new Attribute("id", Util.generateId()));
    sticker.addAttribute(new Attribute("priority", prior + ""));
    sticker.appendChild(text);
    _root.appendChild(sticker);
  }

  @SuppressWarnings("unchecked")
  public static Map getStickers() {
    Map map = new HashMap();
    Elements elements = _root.getChildElements("sticker");

    for (int i = 0; i < elements.size(); i++) {
      Element sticker = elements.get(i);
      map.put(sticker.getAttribute("id").getValue(), sticker);
    }

    return map;
  }

  public static void removeSticker(String stickerId) {
    Elements elements = _root.getChildElements("sticker");

    for (int i = 0; i < elements.size(); i++) {
      Element sticker = elements.get(i);

      if (sticker.getAttribute("id").getValue().equals(stickerId)) {
        _root.removeChild(sticker);
        break;
      }
    }
  }

  public static boolean isNREventsForDate(CalendarDate date) {
    Day day = getDay(date);

    if (day == null) {
      return false;
    }

    if (day.getElement().getChildElements("event").size() > 0) {
      return true;
    }

    return false;
  }

  public static Collection getEventsForDate(CalendarDate date) {
    Vector vector = new Vector();
    Day day = getDay(date);

    if (day != null) {
      Elements elements = day.getElement().getChildElements("event");
      for (int i = 0; i < elements.size(); i++) {
        vector.add(new EventImpl(elements.get(i)));
      }
    }

    Collection r = getRepeatableEventsForDate(date);

    if (r.size() > 0) {
      vector.addAll(r);
    }

    //EventsVectorSorter.sort(v);
    Collections.sort(vector);

    return vector;
  }

  public static Event createEvent(CalendarDate date, int hour, int minute, String text) {
    Element event = new Element("event");
    event.addAttribute(new Attribute("id", Util.generateId()));
    event.addAttribute(new Attribute("hour", String.valueOf(hour)));
    event.addAttribute(new Attribute("min", String.valueOf(minute)));
    event.appendChild(text);
    Day day = getDay(date);

    if (day == null) {
      day = createDay(date);
    }

    day.getElement().appendChild(event);

    return new EventImpl(event);
  }

  public static Event createRepeatableEvent(
      int repeatType,
      CalendarDate startDate,
      CalendarDate endDate,
      int period,
      int hour,
      int minute,
      String text,
      boolean workDays) {
    Element event = new Element("event");
    Element repeatable = _root.getFirstChildElement("repeatable");

    if (repeatable == null) {
      repeatable = new Element("repeatable");
      _root.appendChild(repeatable);
    }

    event.addAttribute(new Attribute("repeat-type", String.valueOf(repeatType)));
    event.addAttribute(new Attribute("id", Util.generateId()));
    event.addAttribute(new Attribute("hour", String.valueOf(hour)));
    event.addAttribute(new Attribute("min", String.valueOf(minute)));
    event.addAttribute(new Attribute("startDate", startDate.toString()));

    if (endDate != null) {
      event.addAttribute(new Attribute("endDate", endDate.toString()));
    }

    event.addAttribute(new Attribute("period", String.valueOf(period)));
    // new attribute for wrkin days - ivanrise
    event.addAttribute(new Attribute("workingDays", String.valueOf(workDays)));
    event.appendChild(text);
    repeatable.appendChild(event);

    return new EventImpl(event);
  }

  public static Collection getRepeatableEvents() {
    Vector vector = new Vector();
    Element repeatable = _root.getFirstChildElement("repeatable");

    if (repeatable == null) {
      return vector;
    }

    Elements elements = repeatable.getChildElements("event");

    for (int i = 0; i < elements.size(); i++) {
      vector.add(new EventImpl(elements.get(i)));
    }

    return vector;
  }

  public static Collection getRepeatableEventsForDate(CalendarDate date) {
    Vector repeatableEvents = (Vector) getRepeatableEvents();
    Vector vector = new Vector();

    for (int i = 0; i < repeatableEvents.size(); i++) {
      Event event = (Event) repeatableEvents.get(i);

      // --- ivanrise
      // ignore this event if it's a 'only working days' event and today is weekend.
      if (event.getWorkingDays() && (
          (date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
          (date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)))) {
        continue;
      }

      if (date.inPeriod(event.getStartDate(), event.getEndDate())) {
        if (event.getRepeat() == REPEAT_MINUTELY) {
          int n = date.getCalendar().get(Calendar.MINUTE);
          int ns = event.getStartDate().getCalendar().get(Calendar.MINUTE);
          if ((n - ns) % event.getHour() == 0) {
            vector.add(event);
          }
        } else if (event.getRepeat() == REPEAT_HOURLY) {
          int n = date.getCalendar().get(Calendar.HOUR_OF_DAY);
          int ns = event.getStartDate().getCalendar().get(Calendar.HOUR_OF_DAY);
          if ((n - ns) % event.getPeriod() == 0) {
            vector.add(event);
          }
        } else if (event.getRepeat() == REPEAT_DAILY) {
          int n = date.getCalendar().get(Calendar.DAY_OF_YEAR);
          int ns = event.getStartDate().getCalendar().get(Calendar.DAY_OF_YEAR);
          if ((n - ns) % event.getPeriod() == 0) {
            vector.add(event);
          }
        } else if (event.getRepeat() == REPEAT_WEEKLY) {
          if (date.getCalendar().get(Calendar.DAY_OF_WEEK) == event.getPeriod()) {
            vector.add(event);
          }
        } else if (event.getRepeat() == REPEAT_MONTHLY) {
          if (date.getCalendar().get(Calendar.DAY_OF_MONTH) == event.getPeriod()) {
            vector.add(event);
          }
        } else if (event.getRepeat() == REPEAT_YEARLY) {
          int period = event.getPeriod();

          if ((date.getYear() % 4) == 0
              && date.getCalendar().get(Calendar.DAY_OF_YEAR) > 60) {
            period++;
          }

          if (date.getCalendar().get(Calendar.DAY_OF_YEAR) == period) {
            vector.add(event);
          }
        }
      }
    }

    return vector;
  }

  public static Collection getActiveEvents() {
    return getEventsForDate(CalendarDate.today());
  }

  public static Event getEvent(CalendarDate date, int hour, int minute) {
    Day day = getDay(date);

    if (day == null) {
      return null;
    }

    Elements elements = day.getElement().getChildElements("event");

    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);
      if ((new Integer(element.getAttribute("hour").getValue()) == hour)
          && (new Integer(element.getAttribute("min").getValue()) == minute)) {
        return new EventImpl(element);
      }
    }

    return null;
  }

  public static void removeEvent(CalendarDate date, int hour, int minute) {
    Day day = getDay(date);

    if (day != null) {
      day.getElement().removeChild(getEvent(date, hour, minute).getContent());
    }
  }

  public static void removeEvent(Event event) {
    ParentNode parent = event.getContent().getParent();
    parent.removeChild(event.getContent());
  }

  private static Day createDay(CalendarDate date) {
    Year year = getYear(date.getYear());

    if (year == null) {
      year = createYear(date.getYear());
    }

    Month month = year.getMonth(date.getMonth());

    if (month == null) {
      month = year.createMonth(date.getMonth());
    }

    Day day = month.getDay(date.getDay());

    if (day == null) {
      day = month.createDay(date.getDay());
    }

    return day;
  }

  private static Year createYear(int year) {
    Element element = new Element("year");
    element.addAttribute(new Attribute("year", new Integer(year).toString()));
    _root.appendChild(element);

    return new Year(element);
  }

  private static Year getYear(int intYear) {
    Elements years = _root.getChildElements("year");
    String year = Integer.toString(intYear);
    for (int i = 0; i < years.size(); i++) {
      if (years.get(i).getAttribute("year").getValue().equals(year)) {
        return new Year(years.get(i));
      }
    }
    //return createYear(year);

    return null;
  }

  private static Day getDay(CalendarDate date) {
    Year y = getYear(date.getYear());
    if (y == null) {
      return null;
    }
    Month m = y.getMonth(date.getMonth());
    if (m == null) {
      return null;
    }
    return m.getDay(date.getDay());
  }

  static class Year {
    Element yearElement = null;

    public Year(Element el) {
      yearElement = el;
    }

    public int getValue() {
      return new Integer(yearElement.getAttribute("year").getValue()).intValue();
    }

    public Month getMonth(int m) {
      Elements ms = yearElement.getChildElements("month");
      String mm = new Integer(m).toString();
      for (int i = 0; i < ms.size(); i++) {
        if (ms.get(i).getAttribute("month").getValue().equals(mm)) {
          return new Month(ms.get(i));
        }
      }
      return null;
    }

    public Month createMonth(int m) {
      Element element = new Element("month");
      element.addAttribute(new Attribute("month", new Integer(m).toString()));
      yearElement.appendChild(element);

      return new Month(element);
    }

    public Vector getMonths() {
      Vector vector = new Vector();
      Elements months = yearElement.getChildElements("month");

      for (int i = 0; i < months.size(); i++) {
        vector.add(new Month(months.get(i)));
      }

      return vector;
    }

    public Element getElement() {
      return yearElement;
    }

  }

  static class Month {
    Element mElement = null;

    public Month(Element el) {
      mElement = el;
    }

    public int getValue() {
      return new Integer(mElement.getAttribute("month").getValue())
          .intValue();
    }

    public Day getDay(int d) {
      if (mElement == null) {
        return null;
      }
      Elements ds = mElement.getChildElements("day");
      String dd = new Integer(d).toString();
      for (int i = 0; i < ds.size(); i++) {
        if (ds.get(i).getAttribute("day").getValue().equals(dd)) {
          return new Day(ds.get(i));
        }
      }
      //return createDay(d);
      return null;
    }

    public Day createDay(int d) {
      Element el = new Element("day");
      el.addAttribute(new Attribute("day", new Integer(d).toString()));
      el.addAttribute(
          new Attribute(
              "date",
              new CalendarDate(
                  d,
                  getValue(),
                  new Integer(
                      ((Element) mElement.getParent())
                      .getAttribute("year")
                      .getValue())
                  .intValue())
              .toString()));

      mElement.appendChild(el);
      return new Day(el);
    }

    public Vector getDays() {
      if (mElement == null) {
        return null;
      }
      Vector v = new Vector();
      Elements ds = mElement.getChildElements("day");
      for (int i = 0; i < ds.size(); i++) {
        v.add(new Day(ds.get(i)));
      }
      return v;
    }

    public Element getElement() {
      return mElement;
    }

  }

  static class Day {
    Element dEl = null;

    public Day(Element el) {
      dEl = el;
    }

    public int getValue() {
      return new Integer(dEl.getAttribute("day").getValue()).intValue();
    }

    public Element getElement() {
      return dEl;
    }
  }

}