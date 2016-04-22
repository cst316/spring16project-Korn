package net.sf.memoranda.ui;

import net.sf.memoranda.*;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.AgendaGenerator;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.util.Util;
import nu.xom.Element;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

//import net.sf.memoranda.History.HistoryForwardAction;

/*$Id: AgendaPanel.java,v 1.11 2005/02/15 16:58:02 rawsushi Exp $*/
public class AgendaPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar toolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    static JButton removeProjB = new JButton();
    static JButton newProjB = new JButton();
    JButton newTaskB = new JButton();
    JButton subTaskB = new JButton();
    JButton export = new JButton();
    static JEditorPane viewer = new JEditorPane("text/html", "");
    String[] priorities = {"Highest", "High", "Medium", "Low", "Lowest"};
    static JScrollPane scrollPane = new JScrollPane();
    DailyItemsPanel parentPanel = null;
    public static RemoveProjAction removeProjAction = new RemoveProjAction();
    public static NewProjAction newProjAction = new NewProjAction();

    //	JPopupMenu agendaPPMenu = new JPopupMenu();
    //  JCheckBoxMenuItem ppShowActiveOnlyChB = new JCheckBoxMenuItem();

    static Collection expandedTasks;
    static String gotoTask = null;

    boolean isActive = true;
    private static int refreshCount = 0;

    public AgendaPanel(DailyItemsPanel _parentPanel) {
        this.setRefreshCount(0);
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
            ex.printStackTrace();
        }
    }

    public static void setRefreshCount(int i) {
        refreshCount = i;

    }

    public static int getRefreshCount() {
        return refreshCount;
    }

    void jbInit() throws Exception {
        expandedTasks = new ArrayList();
        toolBar.setFloatable(false);
        viewer.setEditable(false);
        viewer.setOpaque(false);
        viewer.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String d = e.getDescription();
                    if (d.equalsIgnoreCase("memoranda:events")) {
                        parentPanel.alarmB_actionPerformed(null);
                    } else if (d.startsWith("memoranda:tasks")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                        parentPanel.taskB_actionPerformed(null);
                    } else if (d.startsWith("memoranda:project")) {
                        String id = d.split("#")[1];
                        CurrentProject.set(ProjectManager.getProject(id));
                    } else if (d.startsWith("memoranda:removesticker")) {
                        String id = d.split("#")[1];
                        StickerConfirmation stc = new StickerConfirmation(App.getFrame());
                        Dimension frmSize = App.getFrame().getSize();
                        stc.setSize(new Dimension(300, 180));
                        Point loc = App.getFrame().getLocation();
                        stc.setLocation(
                                (frmSize.width - stc.getSize().width) / 2 + loc.x,
                                (frmSize.height - stc.getSize().height) / 2
                                        + loc.y);
                        stc.setVisible(true);
                        if (!stc.CANCELLED) {
                            EventsManager.removeSticker(id);
                            CurrentStorage.get().storeEventsManager();
                        }
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:addsticker")) {
                        StickerDialog dlg = new StickerDialog(App.getFrame());
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300, 380));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation(
                                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                (frmSize.height - dlg.getSize().height) / 2
                                        + loc.y);
                        dlg.setVisible(true);
                        if (!dlg.CANCELLED) {
                            String txt = dlg.getStickerText();
                            int sP = dlg.getPriority();
                            txt = txt.replaceAll("\\n", "<br>");
                            txt = "<div style=\"background-color:" + dlg.getStickerColor() + ";font-size:" + dlg.getStickerTextSize() + ";color:" + dlg.getStickerTextColor() + "; \">" + txt + "</div>";
                            EventsManager.createSticker(txt, sP);
                            CurrentStorage.get().storeEventsManager();
                        }
                        refresh(CurrentDate.get());
                        System.out.println("add a sticker");
                    } else if (d.startsWith("memoranda:expandsubtasks")) {
                        String id = d.split("#")[1];
                        gotoTask = id;
                        expandedTasks.add(id);
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:closesubtasks")) {
                        String id = d.split("#")[1];
                        gotoTask = id;
                        expandedTasks.remove(id);
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:expandsticker")) {
                        String id = d.split("#")[1];
                        Element pre_sticker = (Element) ((Map) EventsManager.getStickers()).get(id);
                        String sticker = pre_sticker.getValue();
                        int first = sticker.indexOf(">");
                        int last = sticker.lastIndexOf("<");
                        int backcolor = sticker.indexOf("#");
                        int fontcolor = sticker.indexOf("#", backcolor + 1);
                        int sP = Integer.parseInt(pre_sticker.getAttributeValue("priority"));
                        String backGroundColor = sticker.substring(backcolor, sticker.indexOf(';', backcolor));
                        String foreGroundColor = sticker.substring(fontcolor, sticker.indexOf(';', fontcolor));
                        sticker = "<html>" + sticker.substring(first + 1, last) + "</html>";
                        StickerExpand dlg = new StickerExpand(App.getFrame(), sticker, backGroundColor, foreGroundColor, Local.getString("priority") + ": " + Local.getString(priorities[sP]));
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300, 200));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation(
                                (frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                (frmSize.height - dlg.getSize().height) / 2
                                        + loc.y);
                        dlg.stickerText.setText(sticker);
                        dlg.setVisible(true);
                    } else if (d.startsWith("memoranda:editsticker")) {
                        String id = d.split("#")[1];
                        Element pre_sticker = (Element) ((Map) EventsManager.getStickers()).get(id);
                        String sticker = pre_sticker.getValue();
                        sticker = sticker.replaceAll("<br>", "\n");
                        int first = sticker.indexOf(">");
                        int last = sticker.lastIndexOf("<");
                        int backcolor = sticker.indexOf("#");
                        int fontcolor = sticker.indexOf("#", backcolor + 1);
                        int sizeposition = sticker.indexOf("font-size") + 10;
                        int size = Integer.parseInt(sticker.substring(sizeposition, sizeposition + 2));
                        System.out.println(size + " " + sizeposition);
                        int sP = Integer.parseInt(pre_sticker.getAttributeValue("priority"));
                        String backGroundColor = sticker.substring(backcolor, sticker.indexOf(';', backcolor));
                        String foreGroundColor = sticker.substring(fontcolor, sticker.indexOf(';', fontcolor));
                        StickerDialog dlg = new StickerDialog(App.getFrame(), sticker.substring(first + 1, last), backGroundColor, foreGroundColor, sP, size);
                        Dimension frmSize = App.getFrame().getSize();
                        dlg.setSize(new Dimension(300, 380));
                        Point loc = App.getFrame().getLocation();
                        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
                                (frmSize.height - dlg.getSize().height) / 2 + loc.y);
                        dlg.setVisible(true);
                        if (!dlg.CANCELLED) {
                            String txt = dlg.getStickerText();
                            sP = dlg.getPriority();
                            txt = txt.replaceAll("\\n", "<br>");
                            txt = "<div style=\"background-color:" + dlg.getStickerColor() + ";font-size:" + dlg.getStickerTextSize() + ";color:" + dlg.getStickerTextColor() + ";\">" + txt + "</div>";
                            EventsManager.removeSticker(id);
                            EventsManager.createSticker(txt, sP);
                            CurrentStorage.get().storeEventsManager();
                        }
                        refresh(CurrentDate.get());
                    } else if (d.startsWith("memoranda:exportstickerst")) {
                        /* Falta agregar el exportar sticker mientras tanto..*/
                        final JFrame parent = new JFrame();
                        String name = JOptionPane.showInputDialog(parent, Local.getString("Enter filename to export"), null);
                        new ExportSticker(name).export("txt");
                    } else if (d.startsWith("memoranda:exportstickersh")) {
                        /*  Falta agregar el exportar sticker mientras tanto..*/
                        final JFrame parent = new JFrame();
                        String name = JOptionPane.showInputDialog(parent, Local.getString("Enter filename to export"), null);
                        new ExportSticker(name).export("html");
                    } else if (d.startsWith("memoranda:importstickers")) {
                        final JFrame parent = new JFrame();
                        String name = JOptionPane.showInputDialog(parent, Local.getString("Enter filename to import"), null);
                        new ImportSticker(name).import_file();
                    } else if (d.startsWith("memoranda:newtask")) {
                        TaskDialog dlg = new TaskDialog(App.getFrame(), Local.getString("New task"));
                        Dimension frmSize = App.getFrame().getSize();
                        Point loc = App.getFrame().getLocation();
                        dlg.startDate.getModel().setValue(CurrentDate.get().getDate());
                        dlg.endDate.getModel().setValue(CurrentDate.get().getDate());
                        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
                        dlg.setVisible(true);
                        if (dlg.CANCELLED) {
                            return;
                        }
                        CalendarDate sd = new CalendarDate((Date) dlg.startDate.getModel().getValue());
                        CalendarDate ed;
                        if (dlg.chkEndDate.isSelected()) {
                            ed = new CalendarDate((Date) dlg.endDate.getModel().getValue());
                        } else {
                            ed = null;
                        }
                        CalendarDate repEnd;
                        if (dlg.chkEndDateRpt.isSelected()) {
                            repEnd = new CalendarDate((Date) dlg.endDateRpt.getModel().getValue());
                        } else {
                            repEnd = null;
                        }
                        long effort = Util.getMillisFromHours(dlg.txtEffort.getText());

                        Task newTask;
                        Stack<Object> taskCreationParams = new Stack<Object>();
                        taskCreationParams.add(sd);
                        taskCreationParams.add(ed);
                        taskCreationParams.add(dlg.todoField.getText());
                        taskCreationParams.add(dlg.priorityCB.getSelectedIndex());
                        taskCreationParams.add(effort);
                        taskCreationParams.add(dlg.descriptionField.getText());
                        taskCreationParams.add(null);
                        taskCreationParams.add(dlg.chkWorkingDays.isSelected());
                        taskCreationParams.add(((Integer) dlg.progress.getValue()).intValue());
                        taskCreationParams.add(dlg.cbRepeatType.getSelectedIndex());
                        taskCreationParams.add(dlg.chkEndDateRpt.isSelected());
                        taskCreationParams.add(repEnd);
                        taskCreationParams.add(dlg.txtTag.getText());
                        newTask = CurrentProject.getTaskList().createTask(taskCreationParams);

                        newTask.setProgress(((Integer) dlg.progress.getValue()).intValue());
                        CurrentStorage.get().storeTaskList(CurrentProject.getTaskList(), CurrentProject.get());
                        TaskTable.tableChanged();
                        parentPanel.updateIndicators();
                        //taskTable.updateUI();
                    }
                }
            }
        });
        historyBackB.setAction(History.historyBackAction);
        historyBackB.setFocusable(false);
        historyBackB.setBorderPainted(false);
        historyBackB.setToolTipText(Local.getString("History back"));
        historyBackB.setRequestFocusEnabled(false);
        historyBackB.setPreferredSize(new Dimension(24, 24));
        historyBackB.setMinimumSize(new Dimension(24, 24));
        historyBackB.setMaximumSize(new Dimension(24, 24));
        historyBackB.setText("");

        historyForwardB.setAction(History.historyForwardAction);
        historyForwardB.setBorderPainted(false);
        historyForwardB.setFocusable(false);
        historyForwardB.setPreferredSize(new Dimension(24, 24));
        historyForwardB.setRequestFocusEnabled(false);
        historyForwardB.setToolTipText(Local.getString("History forward"));
        historyForwardB.setMinimumSize(new Dimension(24, 24));
        historyForwardB.setMaximumSize(new Dimension(24, 24));
        historyForwardB.setText("");

        newProjB.setAction(AgendaPanel.newProjAction);
        newProjB.setPreferredSize(new Dimension(24, 24));
        newProjB.setRequestFocusEnabled(false);
        newProjB.setToolTipText(Local.getString("Removes the currently active project"));
        newProjB.setMinimumSize(new Dimension(24, 24));
        newProjB.setMaximumSize(new Dimension(24, 24));
        newProjB.setText("");
        newProjB.setBorderPainted(false);
        newProjB.setFocusable(false);

        removeProjB.setAction(AgendaPanel.removeProjAction);
        removeProjB.setPreferredSize(new Dimension(24, 24));
        removeProjB.setRequestFocusEnabled(false);
        removeProjB.setToolTipText(Local.getString("Removes the currently active project"));
        removeProjB.setMinimumSize(new Dimension(24, 24));
        removeProjB.setMaximumSize(new Dimension(24, 24));
        removeProjB.setText("");
        removeProjB.setBorderPainted(false);
        removeProjB.setFocusable(false);

        newTaskB.setIcon(
                new ImageIcon(AppFrame.class.getResource("resources/icons/todo_new.png")));
        newTaskB.setEnabled(true);
        newTaskB.setMaximumSize(new Dimension(24, 24));
        newTaskB.setMinimumSize(new Dimension(24, 24));
        newTaskB.setToolTipText(Local.getString("Create new task"));
        newTaskB.setRequestFocusEnabled(false);
        newTaskB.setPreferredSize(new Dimension(24, 24));
        newTaskB.setFocusable(false);
        newTaskB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentPanel.tasksPanel.newTaskB_actionPerformed(e);
                refresh(CurrentDate.get());
            }
        });
        newTaskB.setBorderPainted(false);


        this.setLayout(borderLayout1);
        scrollPane.getViewport().setBackground(Color.white);

        scrollPane.getViewport().add(viewer, null);
        this.add(scrollPane, BorderLayout.CENTER);
        toolBar.add(historyBackB, null);
        toolBar.add(historyForwardB, null);
        toolBar.addSeparator(new Dimension(8, 24));
        toolBar.add(newProjB, null);
        toolBar.add(removeProjB, null);
        toolBar.addSeparator(new Dimension(8, 24));
        toolBar.add(newTaskB, null);

        this.add(toolBar, BorderLayout.NORTH);

        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                if (isActive) {
                    refresh(d);
                }
            }
        });
        CurrentProject.addProjectListener(new ProjectListener() {

            public void projectChange(
                    Project prj,
                    NoteList nl,
                    TaskList tl,
                    ResourcesList rl) {
            }

            public void projectWasChanged() {
                if (isActive) {
                    refresh(CurrentDate.get());
                }
            }
        });
        EventsScheduler.addListener(new EventNotificationListener() {
            public void eventIsOccured(net.sf.memoranda.Event ev) {
                if (isActive) {
                    refresh(CurrentDate.get());
                }
            }

            public void eventsChanged() {
                if (isActive) {
                    refresh(CurrentDate.get());
                }
            }
        });
        refresh(CurrentDate.get());
    }

    //Event listener class adding a new project.
    public static class NewProjAction extends AbstractAction {

        public NewProjAction() {
            super(Local.getString("Create Project"),
                    new ImageIcon(AppFrame.class.getResource("resources/icons/newproject.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK));
            setEnabled(true);
            refresh(CurrentDate.get());

        }

        public void actionPerformed(ActionEvent e) {
            App.getFrame().projectsPanel.ppNewProject_actionPerformed(e);

        }
    }

    //Event listener class for the Delete Project button on the agenda panel.
    public static class RemoveProjAction extends AbstractAction {

        public RemoveProjAction() {
            super(Local.getString("Delete Project"),
                    new ImageIcon(AppFrame.class.getResource("resources/icons/removeproject.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK));
            if (!CurrentProject.get().getTitle().equals("Default Project") ||
                    ProjectManager.getActiveProjectsNumber() > 1) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
            refresh(CurrentDate.get());

        }

        public void actionPerformed(ActionEvent e) {
            App.getFrame().projectsPanel.BDeleteProject_actionPerformed(e);

        }
    }

    public static void refresh(CalendarDate date) {
        setRefreshCount(getRefreshCount() + 1);
        viewer.setText(AgendaGenerator.getAgenda(date, expandedTasks));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                refreshProjButtons();
                if (gotoTask != null) {
                    viewer.scrollToReference(gotoTask);
                    scrollPane.setViewportView(viewer);
                    Util.debug("Set view port to " + gotoTask);
                }
            }
        });

        Util.debug("Summary updated.");
    }

    static void refreshProjButtons() {
        // Refreshes delete project button.
        if (!CurrentProject.get().getTitle().equals("Default Project") ||
                ProjectManager.getActiveProjectsNumber() > 1) {
            removeProjB.setEnabled(true);
        } else {
            removeProjB.setEnabled(false);
        }
    }

    public void setActive(boolean isa) {
        isActive = isa;
    }

}
