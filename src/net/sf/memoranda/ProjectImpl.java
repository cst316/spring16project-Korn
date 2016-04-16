/**
 * ProjectImpl.java
 * Created on 11.02.2003, 23:06:22 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Default implementation of Project interface
 */
/*$Id: ProjectImpl.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class ProjectImpl implements Project {

    private Element _root = null;

    /**
     * Constructor for ProjectImpl.
     */
    public ProjectImpl(Element root) {
        _root = root;
    }

    /**
     * @see net.sf.memoranda.Project#getID()
     */
    public String getID() {
        return _root.getAttribute("id").getValue();
    }

    /**
     * @see net.sf.memoranda.Project#getStartDate()
     */
    public CalendarDate getStartDate() {
        Attribute d = _root.getAttribute("startDate");
        if (d == null) {
            return null;
        }
        return new CalendarDate(d.getValue());
    }

    /**
     * @see net.sf.memoranda.Project#setStartDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setStartDate(CalendarDate date) {
        if (date != null) {
            setAttr("startDate", date.toString());
        }
    }

    /**
     * @see net.sf.memoranda.Project#getEndDate()
     */
    public CalendarDate getEndDate() {
        Attribute d = _root.getAttribute("endDate");
        if (d == null) {
            return null;
        }
        return new CalendarDate(d.getValue());
    }

    /**
     * @see net.sf.memoranda.Project#setEndDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setEndDate(CalendarDate date) {
        CalendarDate start = (getStartDate() == null ? CalendarDate.tomorrow() : getStartDate());

        if (date != null) {
            if (date.before(start)) {
                setAttr("endDate", start.toString());
            } else {
                setAttr("endDate", date.toString());
            }
        } else if (_root.getAttribute("endDate") != null) {
            setAttr("endDate", null);
        }
    }

    /**
     * @see net.sf.memoranda.Project#getStatus()
     */
    public int getStatus() {
        if (isFrozen()) {
            return Project.FROZEN;
        }
        CalendarDate today = CurrentDate.get();
        CalendarDate prStart = getStartDate();
        CalendarDate prEnd = getEndDate();
        if (prEnd == null) {
            if (today.before(prStart)) {
                return Project.SCHEDULED;
            } else {
                return Project.ACTIVE;
            }
        }
        if (today.inPeriod(prStart, prEnd)) {
            return Project.ACTIVE;
        } else if (today.after(prEnd)) {
            //if (getProgress() == 100)
            return Project.COMPLETED;
            /*else
                return Project.FAILED;*/
        } else {
            return Project.SCHEDULED;
        }
    }

    private boolean isFrozen() {
        return _root.getAttribute("frozen") != null;
    }


    /**
     * @see net.sf.memoranda.Project#freeze()
     */
    public void freeze() {
        _root.addAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see net.sf.memoranda.Project#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen()) {
            _root.removeAttribute(new Attribute("frozen", "yes"));
        }
    }

    /**
     * @see net.sf.memoranda.Project#getTitle()
     */
    public String getTitle() {
        Attribute ta = _root.getAttribute("title");
        if (ta != null) {
            return ta.getValue();
        }
        return "";
    }

    /**
     * @see net.sf.memoranda.Project#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        setAttr("title", title);
    }

    private void setAttr(String name, String value) {
        Attribute a = _root.getAttribute(name);
        if (a == null) {
            if (value != null) {
                _root.addAttribute(new Attribute(name, value));
            }
        } else if (value != null) {
            a.setValue(value);
        } else {
            _root.removeAttribute(a);
        }
    }

    public String getDescription() {
        Element thisElement = _root.getFirstChildElement("description");
        if (thisElement == null) {
            return null;
        } else {
            return thisElement.getValue();
        }
    }

    public void setDescription(String s) {
        Element desc = _root.getFirstChildElement("description");
        if (desc == null) {
            desc = new Element("description");
            desc.appendChild(s);
            _root.appendChild(desc);
        } else {
            desc.removeChildren();
            desc.appendChild(s);
        }
    }
    
    public boolean equals(Project prj) {
        return (this.getID().equals(prj.getID()));
    }
}
