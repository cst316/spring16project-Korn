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
import nu.xom.*;

import java.util.*;

/**
 * TaskListImp the implementation of TaskList.
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
    private static Stack<Task> tempTasks = new Stack<Task>();
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
     *
     * @param prj given project task list is associated with.
     */

    public TaskListImpl(Project prj) {
        _root = new Element("tasklist");
        _document = new Document(_root);
        _project = prj;
    }

    /**
     * Returns project.
     *
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
     * getAllSubTasks and getActiveSubTasks.
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
     * getAllSubTasks and getActiveSubTasks.
     * If a root task is required, just send a null taskId
     */
    public Collection<Task> getActiveSubTasks(String taskId, CalendarDate date) {
        Collection<Task> allTasks = getAllSubTasks(taskId);
        return filterActiveTasks(allTasks, date);
    }
    
    private Task constructTask(Stack<Object> taskCreationParams) {
        String id = (String) taskCreationParams.pop();
        CalendarDate endRepeat = (CalendarDate) taskCreationParams.pop();
        boolean repeatHasEnd = (boolean) taskCreationParams.pop();
        int repeatType = (int) taskCreationParams.pop();
        int progress = (int) taskCreationParams.pop();
        boolean workDays = (boolean) taskCreationParams.pop();
        String parentTaskId = (String) taskCreationParams.pop();
        String description = (String) taskCreationParams.pop();
        Object effortObj = taskCreationParams.pop();
        long effort;
        	if(effortObj.getClass().equals(Long.class)){
        		effort = (long)effortObj;
        	} else if (effortObj.getClass().equals(Integer.class)){
        		effort = ((Integer)effortObj).longValue();
        	} else {
        		effort = 0;
        	}
         
        int priority = (int) taskCreationParams.pop();
        String text = (String) taskCreationParams.pop();
        CalendarDate endDate = (CalendarDate) taskCreationParams.pop();
        CalendarDate startDate = (CalendarDate) taskCreationParams.pop();

        assert (Task.REPEAT_FREQUENCIES_INDEX[repeatType] == repeatType);
        Element taskElem = new Element("task");
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
        if (repeatHasEnd) {
            task.setEndRepeat(endRepeat);
        }
        return task;
    }
 
  /*
   * (non-Javadoc)
   * @see net.sf.memoranda.TaskList#createTask(net.sf.memoranda.date.CalendarDate,
   *  net.sf.memoranda.date.CalendarDate, java.lang.String,
   *   int, long, java.lang.String, java.lang.String, boolean, int, int)
   */

    /**
     * Create task adds a task to the task list.
     *
     * @param startDate The date the task begins
     * @param endDate   the date the task ends
     */
    public Task createTask(Stack<Object> taskCreationParams) {
        String id = Util.generateId();

        taskCreationParams.add(id);
        Task task = constructTask(taskCreationParams);

        elements.put(id, task.getContent());

        return new TaskImpl(task.getContent(), this);
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

  /*
   * (non-Javadoc)
   * @see net.sf.memoranda.TaskList#createTask(net.sf.memoranda.date.CalendarDate,
   *  net.sf.memoranda.date.CalendarDate, java.lang.String,
   *   int, long, java.lang.String, java.lang.String, boolean, int, int)
   */

    /**
     * Create task adds a task to the task list.
     *
     * @param startDate The date the task begins
     * @param endDate   the date the task ends
     */
    public Task createRptInstanceTask(Stack<Object> taskCreationParams) {
        String id = Util.generateId();

        taskCreationParams.add(id);
        Task task = constructTask(taskCreationParams);

        for (int i = 0; i < elements.size(); i++) {
            if (elements.contains(task.getContent()))//This is where we will fix the duplicating tasks bug
            {
                return null;
            }
        }
        elements.put(id, task.getContent());
        //tempTasks.add(task);
        return new TaskImpl(task.getContent(), this);
    }

    public void clearTempTasks() {
        while (!tempTasks.isEmpty()) {
            try {
                this.removeTask(tempTasks.pop());
            } catch (Exception e) {
                System.out.println("[DEBUG] Temp Task Not Found");
            }
        }
    }

    /**
     * check for subtasks.
     *
     * @param id id of the item being checked.
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
     *
     * @param id id of the item being checked.
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
     * @param task task to calculate effort for.
     * @return total effort from given task
     */
    public long calculateTotalEffortFromSubTasks(Task task) {
        long totalEffort = 0;
        if (hasSubTasks(task.getId())) {
            Collection<Task> subTasks = getAllSubTasks(task.getId());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext(); ) {
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
     * @param task parent task used to check subtasks.
     * @return earliest start date.
     */
    public CalendarDate getEarliestStartDateFromSubTasks(Task task) {
        CalendarDate startDate = task.getStartDate();
        if (hasSubTasks(task.getId())) {
            Collection<Task> subTasks = getAllSubTasks(task.getId());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext(); ) {
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
     * @param task task to be checked for latest End date.
     * @return latest end date.
     */
    public CalendarDate getLatestEndDateFromSubTasks(Task task) {
        CalendarDate date = task.getEndDate();
        if (hasSubTasks(task.getId())) {
            Collection<Task> subTasks = getAllSubTasks(task.getId());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext(); ) {
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
     * @param task task used to complete subtasks.
     * @return long[]
     * of size 2. First long is expended effort in milliseconds,
     * 2nd long is total effort in milliseconds
     */
    public long[] calculateCompletionFromSubTasks(Task task) {
    /* Util.debug("Task " + t.getText()); */

        long[] res = new long[2];
        long expendedEffort = 0; // milliseconds
        long totalEffort = 0; // milliseconds
        if (hasSubTasks(task.getId())) {
            Collection<Task> subTasks = getAllSubTasks(task.getId());
            for (Iterator<Task> iter = subTasks.iterator(); iter.hasNext(); ) {
                Task iterTask = iter.next();
                long[] subTaskCompletion = calculateCompletionFromSubTasks(iterTask);
                expendedEffort = expendedEffort + subTaskCompletion[0];
                totalEffort = totalEffort + subTaskCompletion[1];
            }

            int thisProgress = (int) Math.round((((double) expendedEffort
                    / (double) totalEffort) * 100));
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
            res[0] = Math.round((double) (task.getProgress() * eff) / 100d);
            res[1] = eff;
            return res;
        }
    }

    /**
     * get Task element method.
     *
     * @param id - id of the task
     * @return taskElement the contents of the task
     */
    public nu.xom.Element getTaskElement(String id) {
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

    private Collection<Task> filterActiveTasks(Collection<Task> tasks, CalendarDate date) {
        Vector<Task> vector = new Vector<Task>();
        for (Iterator<Task> iter = tasks.iterator(); iter.hasNext(); ) {
            Task iteratorTask = iter.next();
            if (isActive(iteratorTask, date)) {
                vector.add(iteratorTask);
            }
        }
        return vector;
    }

    private boolean isActive(Task task, CalendarDate date) {
        if ((task.getStatus(date) == Task.ACTIVE) || (task.getStatus(date) == Task.DEADLINE)
                || (task.getStatus(date) == Task.FAILED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a collection of repeatable Tasks.
     *
     * @return repeatableTasks
     */
    public Collection<Task> getDuplicateTasks(String taskId, CalendarDate endDate) {
        Vector<Task> vector = new Vector<Task>();
        nu.xom.Elements elements = _root.getChildElements("task");
        Task t;
        for (int i = 0; i < elements.size(); i++) {
            t = getTask((elements.get(i).getAttribute("id").getValue()));
            if (t.getEndDate() == endDate && t.getId() == taskId) {
                vector.add(t);
            }
        }
        return vector;

    }

    public Vector<Task> getRepeatableTasks() {
        Vector<Task> vector = new Vector<Task>();
        nu.xom.Elements elements = _root.getChildElements("task");
        Task t;

        for (int i = 0; i < elements.size(); i++) {
            t = getTask(elements.get(i).getAttribute("id").getValue());
            if (t.isRepeatable()) {
                vector.add(t);
            }
        }
        return vector;
    }


    public Vector<Task> getRepeatableTaskforDate(CalendarDate date) {
        Vector<Task> repeatableTasks = getRepeatableTasks();
        Vector<Task> tasksForDate = new Vector<Task>();
        boolean duplicate = false;
        Task task;
        CalendarDate endRepeat;

        for (int i = 0; i < repeatableTasks.size(); i++) {
            task = (Task) repeatableTasks.get(i);
            endRepeat = task.getEndRepeat();

            for (int j = 0; j < tasksForDate.size(); j++) {
                if (task.getText().equals(tasksForDate.get(j).getText()) &&
                        task.getStartDate().equals(tasksForDate.get(j).getStartDate())) {
                    duplicate = true;
                }
            }

            if (!duplicate) {
                if ((date.after(task.getStartDate()) && endRepeat == null)
                        || (date.inPeriod(task.getStartDate(), endRepeat))) {
                    if (!task.getWorkingDaysOnly() ||
                            (task.getWorkingDaysOnly() &&
                                    !((date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                                            || (date.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)))) {
                        if (task.getRepeatType() == REPEAT_DAILY) {
                            //Why are we bothering to check this? If something repeats daily, its period should
                            //always be zero and it should show up every day.
                            int n = date.getCalendar().get(Calendar.DAY_OF_YEAR);
                            int ns = task.getStartDate().getCalendar().get(Calendar.DAY_OF_YEAR);
                            if (task.getPeriod() == 0 || (n - ns) % task.getPeriod() == 0) {
                                tasksForDate.add(task);
                            }
                        } else if (task.getRepeatType() == REPEAT_WEEKLY) {
                            if (date.getCalendar().get(Calendar.DAY_OF_WEEK) ==
                                    task.getStartDate().getCalendar().get(Calendar.DAY_OF_WEEK)) {
                                tasksForDate.add(task);
                            }
                        } else if (task.getRepeatType() == REPEAT_MONTHLY) {
                            if (date.getCalendar().get(Calendar.DAY_OF_MONTH) ==
                                    task.getStartDate().getCalendar().get(Calendar.DAY_OF_MONTH)) {
                                tasksForDate.add(task);
                            }
                        } else if (task.getRepeatType() == REPEAT_YEARLY) {
                            if (date.getCalendar().get(Calendar.DAY_OF_YEAR) ==
                                    task.getStartDate().getCalendar().get(Calendar.DAY_OF_YEAR)) {
                                tasksForDate.add(task);
                            }
                        }
                    }
                }
            } else {
                duplicate = false;
            }
        }
        return tasksForDate;
    }
}