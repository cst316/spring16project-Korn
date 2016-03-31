/**
 * TaskListImpl.java
 * Created on 21.02.2003, 12:29:54 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import net.sf.memoranda.EventsManager.Day;
import net.sf.memoranda.EventsManager.Year;
import net.sf.memoranda.EventsManager.Month;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
//import nu.xom.converters.*;
//import org.apache.xerces.dom.*;
//import nux.xom.xquery.XQueryUtil;

/** TaskListImp the implementation of TaskList.
 * 
 */
/*$Id: TaskListImpl.java,v 1.14 2006/07/03 11:59:19 alexeya Exp $*/
public class TaskListImpl implements TaskList {
	
	
  public static final int NO_REPEAT = 0;
  public static final int REPEAT_DAILY = 1;
  public static final int REPEAT_WEEKLY = 2;
  public static final int REPEAT_MONTHLY = 3;
  public static final int REPEAT_YEARLY = 4;
  
  
  private Project _project = null;
  private nu.xom.Document _document = null;
  static nu.xom.Element _root = null;
  
  /*
   * Hastable of "task" XOM elements for quick searching them by ID's
   * (ID => element) 
   */
  private Hashtable<String, nu.xom.Element> elements = new Hashtable<String, Element>();
    
  /**
   * Constructor for TaskListImpl.
   */
  public TaskListImpl(Document doc, Project prj) {
    _document = doc;
    _root = _document.getRootElement();
    _project = prj;
    buildElements(_root);
  }
  
  /**
   * Constructs Task list for Project prj.
   * @param prj
   *     given project task list is associated with.
   */
    
  public TaskListImpl(Project prj) {            
    _root = new Element("tasklist");
    _document = new Document(_root);
    _project = prj;
  }
  
  /**
   * Returns project.
   * @return project
   */
  
  public Project getProject() {
    return _project;
  }
  
  /*
   * Build the hashtable recursively
   */
  
  private void buildElements(Element parent) {
    Elements els = parent.getChildElements("task");
    for (int i = 0; i < els.size(); i++) {
      Element el = els.get(i);
      elements.put(el.getAttribute("id").getValue(), el);
      buildElements(el);
    }
  }

  /**
    * All methods to obtain list of tasks are consolidated under
    *  getAllSubTasks and getActiveSubTasks.
    * If a root task is required, just send a null taskId
    */
  public Collection<Task> getAllSubTasks(String taskId) {
    if ((taskId == null) || (taskId.length() == 0)) {
      return getAllRootTasks();
    } else {
      Element task = (Element) getTaskElement(taskId);
      if (task == null) {
        return new Vector<Task>();
      }
      Elements subTasks = task.getChildElements("task");
      return convertToTaskObjects(subTasks);
    }
  }
    
  public Collection<Task> getTopLevelTasks() {
    return getAllRootTasks();
  }

  /**
    * All methods to obtain list of tasks are consolidated under
    *  getAllSubTasks and getActiveSubTasks.
    * If a root task is required, just send a null taskId
    */
  public Collection<Task> getActiveSubTasks(String taskId,CalendarDate date) {
    Collection<Task> allTasks = getAllSubTasks(taskId);        
    return filterActiveTasks(allTasks,date);
  }

  /*    public Task createTask(
       CalendarDate startDate, 
       CalendarDate endDate, 
       String text, 
       int priority, 
       long effort, 
       String description, 
       String parentTaskId) {
        Element el = new Element("task");
        el.addAttribute(new Attribute("startDate", startDate.toString()));
        if (endDate != null)
        el.addAttribute(new Attribute("endDate", endDate.toString()));
        String id = Util.generateId();
        el.addAttribute(new Attribute("id", id));
        el.addAttribute(new Attribute("progress", "0"));
        el.addAttribute(new Attribute("effort", String.valueOf(effort)));
        el.addAttribute(new Attribute("priority", String.valueOf(priority)));
                
        Element txt = new Element("text");
        txt.appendChild(text);
        el.appendChild(txt);

        Element desc = new Element("description");
        desc.appendChild(description);
        el.appendChild(desc);

        if (parentTaskId == null) {
            _root.appendChild(el);
        }
        else {
            Element parent = getTaskElement(parentTaskId);
            parent.appendChild(el);
        }
        
        elements.put(id, el);
        Util.debug("Created task with parent " + parentTaskId);
        TaskImpl ret = new TaskImpl(el, this);
        AgendaPanel.refresh(ret.getStartDate());
        return ret;
    }*/
 
  /*
   * (non-Javadoc)
   * @see net.sf.memoranda.TaskList#createTask(net.sf.memoranda.date.CalendarDate,
   *  net.sf.memoranda.date.CalendarDate, java.lang.String,
   *   int, long, java.lang.String, java.lang.String, boolean, int, int)
   */
  /**
   *  Create task adds a task to the task list.
   *  @param startDate
   *    The date the task begins
   *  @param endDate
   *   the date the task ends
   */
  public Task createTask(
      CalendarDate startDate,
      CalendarDate endDate,
      String text,
      int priority,    
      long effort, 
      String description, 
      String parentTaskId,
      boolean workDays,
      int progress,
      int repeatType,
      boolean repeatHasEnd,
      CalendarDate endRepeat) {
      assert (Task.REPEAT_FREQUENCIES_INDEX [repeatType] == repeatType);
    Element taskElem = new Element("task");
    String id = Util.generateId();
    taskElem.addAttribute(new Attribute("id", id));
    Task task = new TaskImpl(taskElem, this);
    task.setStartDate(startDate);
    task.setEndDate(endDate);
    task.setText(text);
    task.setPriority(priority);
    task.setEffort(effort);
    task.setDescription(description);
    task.setParentTask(parentTaskId, _root);
    task.setWorkingDaysOnly(workDays);
    task.setProgress(progress);
    task.setRepeatType(repeatType); // 0-none, 1-Daily, 2-Weekly, 3-Monthly, 4-Yearly
    
    if(repeatHasEnd) {
    	task.setEndRepeat(endRepeat);
    }

	elements.put(id, task.getContent());
	
    return new TaskImpl(task.getContent(), this);
//      Element el = new Element("task");
//      String id = Util.generateId();
//      el.addAttribute(new Attribute("startDate", startDate.toString()));
//      if (endDate != null) {
//    	  el.addAttribute(new Attribute("endDate", endDate.toString()));          }
//      el.addAttribute(new Attribute("progress", String.valueOf(progress)));
//      el.addAttribute(new Attribute("effort", String.valueOf(effort)));
//      el.addAttribute(new Attribute("priority", String.valueOf(priority)));
//      el.addAttribute(new Attribute("frequency", String.valueOf(frequency)));
//      //new attribute for wrkin days - ivanrise
//      el.addAttribute(new Attribute("workingDays",String.valueOf(workDays)));
//
//      Element txt = new Element("text");
//      txt.appendChild(text);
//      el.appendChild(txt);
//
//      Element desc = new Element("description");
//      desc.appendChild(description);
//      el.appendChild(desc);
//
//      if (parentTaskId == null) {
//          root.appendChild(el);
//      }
//      else {
//          Element parent = getTaskElement(parentTaskId);
//          parent.appendChild(el);
//      }
//    
//      elements.put(id, el);
//   
//      Util.debug("Created task with parent " + parentTaskId + 
//    		  " and recurrence " + Task.REPEAT_FREQUENCIES_LIST[frequency]);
//      return new TaskImpl(el, this);
  }
    
  /**
   * @see net.sf.memoranda.TaskList#removeTask(import net.sf.memoranda.Task)
   */

  public void removeTask(Task task) {
    String parentTaskId = task.getParentId();
    if (parentTaskId == null) {
      _root.removeChild(task.getContent());            
    } else {
      Element parentNode = (Element) getTaskElement(parentTaskId);
      parentNode.removeChild(task.getContent());
    }
    elements.remove(task.getId());
  }

  
  /**
   * check for subtasks.
   * @param id
   *   id of the item being checked.
   */
  public boolean hasSubTasks(String id) {
    Element task = (Element) getTaskElement(id);
    if (task == null) {
      return false; 
    }
    if (task.getChildElements("task").size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public Task getTask(String id) {
    Util.debug("Getting task " + id);          
    return new TaskImpl((Element) getTaskElement(id), this);          
  }
    
  /**
   * Checks for parent task.
   * @param id
   *   id of the item being checked.
   */
  public boolean hasParentTask(String id) {
    Element element = getTaskElement(id);

    Node parentNode = element.getParent();
    if (parentNode instanceof Element) {
      Element parent = (Element) parentNode;
      if (parent.getLocalName().equalsIgnoreCase("task")) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * @see net.sf.memoranda.TaskList#getXmlContent()
   */
  
  public Document getXmlContent() {
	  return _document;
  }
    
  /**
    * Recursively calculate total effort based on subtasks for every node in the task tree
    * The values are saved as they are calculated as well.
    * 
    * @param task
    *  task to calculate effort for.
    * @return
    *   total effort from given task
    */  
  public long calculateTotalEffortFromSubTasks(Task task) {
    long totalEffort = 0;
    if (hasSubTasks(task.getId())) {
      Collection<Task> subTasks = getAllSubTasks(task.getId());
      for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
        Task iteratedTask = iter.next();
        totalEffort = totalEffort + calculateTotalEffortFromSubTasks(iteratedTask);
      }
      task.setEffort(totalEffort);
      return totalEffort;            
    } else {
      return task.getEffort();
    }
  }

  /**
    * Looks through the entire sub task tree and corrects any inconsistencies in start dates.
    * 
    * @param task
    *   parent task used to check subtasks. 
    * @return
    *    earliest start date.
    */
  public CalendarDate getEarliestStartDateFromSubTasks(Task task) {
    CalendarDate startDate = task.getStartDate();
    if (hasSubTasks(task.getId())) {
      Collection<Task> subTasks = getAllSubTasks(task.getId());
      for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
        Task iteratedTask = iter.next();
        CalendarDate dd = getEarliestStartDateFromSubTasks(iteratedTask);
        if (dd.before(startDate)) {
          startDate = dd;        
        }
      }
      task.setStartDate(startDate);
      return startDate;
    } else {
      return task.getStartDate();
    }
  }

  /**
   * Looks through the entire sub task tree and corrects any inconsistencies in start dates.
   * 
   * @param task
   *   task to be checked for latest End date.
   * @return
   *   latest end date.
   */
  public CalendarDate getLatestEndDateFromSubTasks(Task task) {
    CalendarDate date = task.getEndDate();
    if (hasSubTasks(task.getId())) {
      Collection<Task> subTasks = getAllSubTasks(task.getId());
      for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
        Task iteratorTask = iter.next();
        CalendarDate dd = getLatestEndDateFromSubTasks(iteratorTask);
        if (dd.after(date)) {
          date = dd;
        }
      }
      task.setEndDate(date);
      return date;
    } else {
      return task.getEndDate();
    }
  }
    
  /**
   * Looks through the entire sub task tree and calculates progress on all parent task nodes
   * 
   * @param task
   *  task used to complete subtasks.
   * @return long[] 
   *     of size 2. First long is expended effort in milliseconds,
   *       2nd long is total effort in milliseconds
   */
  public long[] calculateCompletionFromSubTasks(Task task) {
    /* Util.debug("Task " + t.getText()); */
        
    long[] res = new long[2];
    long expendedEffort = 0; // milliseconds
    long totalEffort = 0; // milliseconds
    if (hasSubTasks(task.getId())) {
      Collection<Task> subTasks = getAllSubTasks(task.getId());
      for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
        Task iterTask = iter.next();
        long[] subTaskCompletion = calculateCompletionFromSubTasks(iterTask);
        expendedEffort = expendedEffort + subTaskCompletion[0];
        totalEffort = totalEffort + subTaskCompletion[1];
      }
            
      int thisProgress = (int) Math.round((((double)expendedEffort 
          / (double)totalEffort) * 100));
      task.setProgress(thisProgress);

      /*          Util.debug("Expended Effort: "+ expendedEffort);
                  Util.debug("Total Effort: "+ totalEffort);
                  Util.debug("Progress: "+ t.getProgress());   */

      res[0] = expendedEffort;
      res[1] = totalEffort;
      return res;            
    } else {
      long eff = task.getEffort();
      // if effort was not filled in, it is assumed to be "1 hr" for the purpose of calculation
      if (eff == 0) {
        eff = 1;
      }
      res[0] = Math.round((double)(task.getProgress() * eff) / 100d); 
      res[1] = eff;
      return res;
    }
  }    

  /**
   *  get Task element method.
   *  @param id - id of the task
   *  @return taskElement the contents of the task
   */
  public nu.xom.Element getTaskElement(String id) {
               
    /*Nodes nodes = XQueryUtil.xquery(_doc, "//task[@id='" + id + "']");
        if (nodes.size() > 0) {
            Element el = (Element) nodes.get(0);
            return el;            
        }
        else {
            Util.debug("Task " + id + " cannot be found in project " + _project.getTitle());
            return null;
        } */
    Element el = elements.get(id);
    if (el == null) {
      Util.debug("Task " + id + " cannot be found in project " + _project.getTitle());
    }
    return el;
  }
    
  private Collection<Task> getAllRootTasks() {
    Elements tasks = _root.getChildElements("task");
    return convertToTaskObjects(tasks);
  }
    
  private Collection<Task> convertToTaskObjects(Elements tasks) {
    Vector<Task> vector = new Vector<Task>();

    for (int i = 0; i < tasks.size(); i++) {
      Task task = new TaskImpl(tasks.get(i), this);
      vector.add(task);
    }
    return vector;
  }

  private Collection<Task> filterActiveTasks(Collection<Task> tasks,CalendarDate date) {
    Vector<Task> vector = new Vector<Task>();
    for (Iterator<Task> iter = tasks.iterator(); iter.hasNext();) {
      Task iteratorTask = iter.next();
      if (isActive(iteratorTask,date)) {
        vector.add(iteratorTask);
      }
    }
    return vector;
  }

  private boolean isActive(Task task,CalendarDate date) {
    if ((task.getStatus(date) == Task.ACTIVE) || (task.getStatus(date) == Task.DEADLINE)
        || (task.getStatus(date) == Task.FAILED)) {
      return true;
    } else {
      return false;
    }
  }
  /**
   * Returns a collection of repeatable Tasks.
   * @return repeatableTasks
   */
  
  public  Collection<Task> getRepeatableTasks() {
  	Vector<Task> vector = new Vector<Task>();
	nu.xom.Elements elements = _root.getChildElements("task");
	Task t;
	
	for(int i = 0; i< elements.size(); i++){
		t = getTask(elements.get(i).getAttribute("id").getValue());
		if(t.isRepeatable()) {
			vector.add(t);
		}
	}
  	return vector;
  }
  
  
  public Collection<Task> getRepeatableTaskforDate(CalendarDate date) {
  	Vector<Task> repeatableTasks = (Vector<Task>) getRepeatableTasks();
  	Vector<Task> tasksForDate = new Vector<Task>();
  	boolean duplicate = false;
  	Task task;
  	CalendarDate endRepeat; 
  	
  	for(int i = 0; i < repeatableTasks.size(); i++) {
  		task = (Task) repeatableTasks.get(i);
  		endRepeat = task.getEndRepeat();

  		for(int j = 0; j < tasksForDate.size(); j++) {
  			if(task.getText().equals(tasksForDate.get(j).getText()) &&
  					task.getStartDate().equals(tasksForDate.get(j).getStartDate())) {
  				duplicate = true;
  			}
  		}
  		
  		if(!duplicate) {
	  		if ((date.after(task.getStartDate()) && endRepeat == null) 
	  				|| (date.inPeriod(task.getStartDate(), endRepeat))) {
	    		if(!task.getWorkingDaysOnly() || 
	    				(task.getWorkingDaysOnly() && 
	    						!((date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
	    						|| (date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)))) {
	    			if(task.getRepeatType() == REPEAT_DAILY) {
	    				int n= date.getCalendar().get(Calendar.DAY_OF_YEAR);
	    				int ns= task.getStartDate().getCalendar().get(Calendar.DAY_OF_YEAR);
	    				if((n-ns) % task.getPeriod() == 0){
	    					tasksForDate.add(task);
	    				}
		    		} else if (task.getRepeatType() == REPEAT_WEEKLY) {
		  				if(date.getCalendar().get(Calendar.DAY_OF_WEEK) == task.getStartDate().getCalendar().get(Calendar.DAY_OF_WEEK))
		  					tasksForDate.add(task);
		  			} else if(task.getRepeatType() == REPEAT_MONTHLY) {
		  				if(date.getCalendar().get(Calendar.DAY_OF_MONTH) == task.getStartDate().getCalendar().get(Calendar.DAY_OF_MONTH))
		  					tasksForDate.add(task);
		  			} else if(task.getRepeatType() == REPEAT_YEARLY) {
		  				if(date.getCalendar().get(Calendar.DAY_OF_YEAR) == task.getStartDate().getCalendar().get(Calendar.DAY_OF_YEAR))
		  					tasksForDate.add(task);
		  			}
	    		}
	  		}
  		} else {
	  		duplicate = false;
  		}
  	}
    return tasksForDate;
  }


  

/*
 * deprecated methods below
 * 
 */
                    
/*    public void adjustParentTasks(Task t) { 
    if ((t.getParent() == null) || (t.getParent().equals(""))){
      return;
    } else {
       Task p = getTask(t.getParent());

       long totalEffort = calculateTotalEffortFromSubTasks(p);

       if(totalEffort > p.getEffort()) {
         p.setEffort(totalEffort);
       }
       if(t.getStartDate().before(p.getStartDate())) {
          p.setStartDate(t.getStartDate());
       }
       if (t.getEndDate().after(p.getEndDate())) {
         p.setEndDate(t.getEndDate());
       }

       if (!((p.getParent() == null) || (p.getParent().equals("")))){
       // still has parent, go up the tree
         adjustParentTasks(p);
         }
    }
    }
} */
}