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

import java.util.Collection;
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

  private Project project = null;
  private Document document = null;
  static Element root = null;
  
  /*
   * Hastable of "task" XOM elements for quick searching them by ID's
   * (ID => element) 
   */
  private Hashtable<String, nu.xom.Element> elements = new Hashtable<String, Element>();
    
  /**
   * Constructor for TaskListImpl.
   */
  public TaskListImpl(Document doc, Project prj) {
    document = doc;
    root = document.getRootElement();
    project = prj;
    buildElements(root);
  }
  
  /**
   * Constructs Task list for Project prj.
   * @param prj
   *     given project task list is associated with.
   */
    
  public TaskListImpl(Project prj) {            
    root = new Element("tasklist");
    document = new Document(root);
    project = prj;
  }
  
  /**
   * Returns project.
   * @return project
   */
  
  public Project getProject() {
    return project;
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
      int frequency) {
    assert (Task.REPEAT_FREQUENCIES_INDEX [frequency] == frequency);
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
    if (parentTaskId == null) {
        root.appendChild(taskElem);
    }
    else {
        Element parent = getTaskElement(parentTaskId);
        parent.appendChild(taskElem);
    }
    //task.setParentTask(parentTaskId, root);
    task.setWorkingDaysOnly(workDays);
    task.setProgress(progress);
    task.setFrequency(frequency);
    elements.put(id, task.getContent());
    
    return task;
    
    /* Element el = new Element("task");
         el.addAttribute(new Attribute("startDate", startDate.toString()));
            if (endDate != null) {
              el.addAttribute(new Attribute("endDate", endDate.toString()));
             else
             }
               el.addAttribute(new Attribute("endDate", startDate.toString()));

            el.addAttribute(new Attribute("progress", String.valueOf(progress)));
            el.addAttribute(new Attribute("effort", String.valueOf(effort)));
            el.addAttribute(new Attribute("priority", String.valueOf(priority)));
            el.addAttribute(new Attribute("frequency", String.valueOf(frequency)));
            new attribute for wrkin days - ivanrise
            el.addAttribute(new Attribute("workingDays",String.valueOf(workDays)));

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
           
            Util.debug("Created task with parent " + parentTaskId + 
            " and recurance " + Task.REPEAT_FREQUENCIES_LIST[frequency]);
            return new TaskImpl(el, this);*/ 
  }
    
  /**
   * @see net.sf.memoranda.TaskList#removeTask(import net.sf.memoranda.Task)
   */

  public void removeTask(Task task) {
    String parentTaskId = task.getParentId();
    if (parentTaskId == null) {
      root.removeChild(task.getContent());            
    } else {
      Element parentNode = (Element) getTaskElement(parentTaskId);
      parentNode.removeChild(task.getContent());
    }
    elements.remove(task.getID());
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
    Element element = (Element) getTaskElement(id);

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
   * @see net.sf.memoranda.TaskList#getXMLContent()
   */
  public Document getXMLContent() {
    return document;
  }
    
    /**
     * Recursively calculate total effort based on subtasks for every node in the task tree
     * The values are saved as they are calculated as well.
     * 
     * @param t
     * @return
     */
    public long calculateTotalEffortFromSubTasks(Task t) {
        long totalEffort = 0;
        if (hasSubTasks(t.getID())) {
            Collection<Task> subTasks = getAllSubTasks(t.getID());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
            	Task e = iter.next();
            	totalEffort = totalEffort + calculateTotalEffortFromSubTasks(e);
            }
            t.setEffort(totalEffort);
            return totalEffort;            
        }
        else {
            return t.getEffort();
        }
    }

    /**
     * Looks through the entire sub task tree and corrects any inconsistencies in start dates
     * 
     * @param t
     * @return
     */
    public CalendarDate getEarliestStartDateFromSubTasks(Task t) {
        CalendarDate d = t.getStartDate();
        if (hasSubTasks(t.getID())) {
	        Collection<Task> subTasks = getAllSubTasks(t.getID());
	        for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
	        	Task e = iter.next();
	        	CalendarDate dd = getEarliestStartDateFromSubTasks(e);
	        	if(dd.before(d)) {
	        	    d = dd;
	        	}
	        }
	        t.setStartDate(d);
	        return d;
        }
        else {
            return t.getStartDate();
        }
    }

    /**
     * Looks through the entire sub task tree and corrects any inconsistencies in start dates
     * 
     * @param t
     * @return
     */
    public CalendarDate getLatestEndDateFromSubTasks(Task t) {
        CalendarDate d = t.getEndDate();
        if (hasSubTasks(t.getID())) {
	        Collection<Task> subTasks = getAllSubTasks(t.getID());
	        for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
	        	Task e = iter.next();
	        	CalendarDate dd = getLatestEndDateFromSubTasks(e);
	        	if(dd.after(d)) {
	        	    d = dd;
	        	}
	        }
	        t.setEndDate(d);
	        return d;
        }
        else {
            return t.getEndDate();
        }
    }
    
    /**
     * Looks through the entire sub task tree and calculates progress on all parent task nodes
     * 
     * @param t
     * @return long[] of size 2. First long is expended effort in milliseconds, 2nd long is total effort in milliseconds
     */
    public long[] calculateCompletionFromSubTasks(Task t) {
//        Util.debug("Task " + t.getText());
        
        long[] res = new long[2];
        long expendedEffort = 0; // milliseconds
        long totalEffort = 0; // milliseconds
        if (hasSubTasks(t.getID())) {
            Collection<Task> subTasks = getAllSubTasks(t.getID());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext();) {
            	Task e = iter.next();
            	long[] subTaskCompletion = calculateCompletionFromSubTasks(e);
            	expendedEffort = expendedEffort + subTaskCompletion[0];
            	totalEffort = totalEffort + subTaskCompletion[1];
            }
            
            int thisProgress = (int) Math.round((((double)expendedEffort / (double)totalEffort) * 100));
            t.setProgress(thisProgress);

//            Util.debug("Expended Effort: "+ expendedEffort);
//            Util.debug("Total Effort: "+ totalEffort);
//            Util.debug("Progress: "+ t.getProgress());

            res[0] = expendedEffort;
            res[1] = totalEffort;
            return res;            
        }
        else {
            long eff = t.getEffort();
            // if effort was not filled in, it is assumed to be "1 hr" for the purpose of calculation
            if (eff == 0) {
                eff = 1;
            }
            res[0] = Math.round((double)(t.getProgress()* eff) / 100d); 
            res[1] = eff;
            return res;
        }
    }    
    /*
     * private methods below this line
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
			Util.debug("Task " + id + " cannot be found in project " + project.getTitle());
		}
		return el;
    }
    
    private Collection<Task> getAllRootTasks() {
        Elements tasks = root.getChildElements("task");
        return convertToTaskObjects(tasks);    	    		
    }
    
    private Collection<Task> convertToTaskObjects(Elements tasks) {
        Vector<Task> v = new Vector<Task>();

        for (int i = 0; i < tasks.size(); i++) {
            Task t = new TaskImpl(tasks.get(i), this);
            v.add(t);
        }
        return v;
    }

    private Collection<Task> filterActiveTasks(Collection<Task> tasks,CalendarDate date) {
        Vector<Task> v = new Vector<Task>();
        for (Iterator<Task> iter = tasks.iterator(); iter.hasNext();) {
            Task t = iter.next();
            if(isActive(t,date)) {
                v.add(t);
            }
        }
        return v;
    }

    private boolean isActive(Task t,CalendarDate date) {
    	if ((t.getStatus(date) == Task.ACTIVE) || (t.getStatus(date) == Task.DEADLINE) || (t.getStatus(date) == Task.FAILED)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    public static Collection getRepeatableTasks(){
    	Vector vector= new Vector();
    	Element repeatable= root.getFirstChildElement("repeatable");
    	
    	if (repeatable==null){
    		vector=null;
    	}
    	else{
    	Elements elements= repeatable.getChildElements("task");
    	}
    	
    	return vector;
    }

    /*
     * deprecated methods below
     * 
     */
                    
//    public void adjustParentTasks(Task t) {
//    	if ((t.getParent() == null) || (t.getParent().equals(""))){
//    		return;
//    	}
//    	else {
//    		Task p = getTask(t.getParent());
//    		
//    		long totalEffort = calculateTotalEffortFromSubTasks(p);
//    		
//    		if(totalEffort > p.getEffort()) {
//    			p.setEffort(totalEffort);
//    		}
//    		if(t.getStartDate().before(p.getStartDate())) {
//    			p.setStartDate(t.getStartDate());
//    		}
//    		if(t.getEndDate().after(p.getEndDate())) {
//    			p.setEndDate(t.getEndDate());
//    		}
//    		
//        	if (!((p.getParent() == null) || (p.getParent().equals("")))){
//        		// still has parent, go up the tree
//        		adjustParentTasks(p);
//        	}    		
//    	}
//    }
}