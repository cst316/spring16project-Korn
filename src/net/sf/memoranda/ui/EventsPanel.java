package net.sf.memoranda.ui;

import net.sf.memoranda.EventsManager;
import net.sf.memoranda.EventsScheduler;
import net.sf.memoranda.History;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.Configuration;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*$Id: EventsPanel.java,v 1.25 2005/02/19 10:06:25 rawsushi Exp $*/
public class EventsPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JToolBar eventsToolBar = new JToolBar();
    JButton historyForwardB = new JButton();
    JButton newEventB = new JButton();
    JButton editEventB = new JButton();
    JButton removeEventB = new JButton();
    JScrollPane scrollPane = new JScrollPane();
    EventsTable eventsTable = new EventsTable();
    JPopupMenu eventPPMenu = new JPopupMenu();
    JMenuItem ppEditEvent = new JMenuItem();
    JMenuItem ppRemoveEvent = new JMenuItem();
    JMenuItem ppNewEvent = new JMenuItem();
    DailyItemsPanel parentPanel = null;

    public EventsPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {
        eventsToolBar.setFloatable(false);

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

        newEventB.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_new.png")));
        newEventB.setEnabled(true);
        newEventB.setMaximumSize(new Dimension(24, 24));
        newEventB.setMinimumSize(new Dimension(24, 24));
        newEventB.setToolTipText(Local.getString("New event"));
        newEventB.setRequestFocusEnabled(false);
        newEventB.setPreferredSize(new Dimension(24, 24));
        newEventB.setFocusable(false);
        newEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newEventB_actionPerformed(e);
            }
        });
        newEventB.setBorderPainted(false);

        editEventB.setBorderPainted(false);
        editEventB.setFocusable(false);
        editEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editEventB_actionPerformed(e);
            }
        });
        editEventB.setPreferredSize(new Dimension(24, 24));
        editEventB.setRequestFocusEnabled(false);
        editEventB.setToolTipText(Local.getString("Edit event"));
        editEventB.setMinimumSize(new Dimension(24, 24));
        editEventB.setMaximumSize(new Dimension(24, 24));
        editEventB.setEnabled(true);
        editEventB.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_edit.png")));

        removeEventB.setBorderPainted(false);
        removeEventB.setFocusable(false);
        removeEventB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeEventB_actionPerformed(e);
            }
        });
        removeEventB.setPreferredSize(new Dimension(24, 24));
        removeEventB.setRequestFocusEnabled(false);
        removeEventB.setToolTipText(Local.getString("Remove event"));
        removeEventB.setMinimumSize(new Dimension(24, 24));
        removeEventB.setMaximumSize(new Dimension(24, 24));
        removeEventB.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_remove.png")));

        this.setLayout(borderLayout1);
        scrollPane.getViewport().setBackground(Color.white);
        eventsTable.setMaximumSize(new Dimension(32767, 32767));
        eventsTable.setRowHeight(24);
        eventPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
        ppEditEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppEditEvent.setText(Local.getString("Edit event") + "...");
        ppEditEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppEditEvent_actionPerformed(e);
            }
        });
        ppEditEvent.setEnabled(false);
        ppEditEvent.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_edit.png")));
        ppRemoveEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppRemoveEvent.setText(Local.getString("Remove event"));
        ppRemoveEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveEvent_actionPerformed(e);
            }
        });
        ppRemoveEvent.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_remove.png")));
        ppRemoveEvent.setEnabled(false);
        ppNewEvent.setFont(new java.awt.Font("Dialog", 1, 11));
        ppNewEvent.setText(Local.getString("New event") + "...");
        ppNewEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewEvent_actionPerformed(e);
            }
        });
        ppNewEvent.setIcon(
                new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/event_new.png")));
        scrollPane.getViewport().add(eventsTable, null);
        this.add(scrollPane, BorderLayout.CENTER);
        eventsToolBar.add(historyBackB, null);
        eventsToolBar.add(historyForwardB, null);
        eventsToolBar.addSeparator(new Dimension(8, 24));

        eventsToolBar.add(newEventB, null);
        eventsToolBar.add(removeEventB, null);
        eventsToolBar.addSeparator(new Dimension(8, 24));
        eventsToolBar.add(editEventB, null);

        this.add(eventsToolBar, BorderLayout.NORTH);

        PopupListener ppListener = new PopupListener();
        scrollPane.addMouseListener(ppListener);
        eventsTable.addMouseListener(ppListener);

        CurrentDate.addDateListener(new DateListener() {
            public void dateChange(CalendarDate d) {
                eventsTable.initTable(d);
                boolean enbl = d.after(CalendarDate.today()) || d.equals(CalendarDate.today());
                newEventB.setEnabled(enbl);
                ppNewEvent.setEnabled(enbl);
                editEventB.setEnabled(false);
                ppEditEvent.setEnabled(false);
                removeEventB.setEnabled(false);
                ppRemoveEvent.setEnabled(false);
            }
        });

        eventsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = eventsTable.getSelectedRow() > -1;
                editEventB.setEnabled(enbl);
                ppEditEvent.setEnabled(enbl);
                removeEventB.setEnabled(enbl);
                ppRemoveEvent.setEnabled(enbl);
            }
        });
        editEventB.setEnabled(false);
        removeEventB.setEnabled(false);
        eventPPMenu.add(ppEditEvent);
        eventPPMenu.addSeparator();
        eventPPMenu.add(ppNewEvent);
        eventPPMenu.add(ppRemoveEvent);

        // remove events using the DEL key
        eventsTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (eventsTable.getSelectedRows().length > 0
                        && e.getKeyCode() == KeyEvent.VK_DELETE) {
                    ppRemoveEvent_actionPerformed(null);
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    // The event handler that opens the edit menu for editing an existing event
    void editEventB_actionPerformed(ActionEvent actionEvent) {
        EventDialog dialog = new EventDialog(App.getFrame(), Local.getString("Event"));
        net.sf.memoranda.Event event =
                (net.sf.memoranda.Event) eventsTable.getModel().getValueAt(
                        eventsTable.getSelectedRow(),
                        EventsTable.EVENT);

        dialog.timeSpin.getModel().setValue(event.getTime());
        /*if (new CalendarDate(ev.getTime()).equals(CalendarDate.today())) 
            ((SpinnerDateModel)dlg.timeSpin.getModel()).setStart(new Date());
        else
        ((SpinnerDateModel)dlg.timeSpin.getModel()).setStart(CalendarDate.today().getDate());
        ((SpinnerDateModel)dlg.timeSpin.getModel()).setEnd(CalendarDate.tomorrow().getDate());*/
        dialog.textField.setText(event.getText());
        int repeatType = event.getRepeat();

        if (repeatType > 0) {
            dialog.startDate.getModel().setValue(event.getStartDate().getDate());

            if (repeatType == EventsManager.REPEAT_MINUTELY) {
                dialog.minutelyRepeatRB.setSelected(true);
                dialog.minutelyRepeatRB_actionPerformed(null);
                dialog.minuteSpin.setValue(event.getPeriod());
            } else if (repeatType == EventsManager.REPEAT_HOURLY) {
                dialog.hourlyRepeatRB.setSelected(true);
                dialog.hourlyRepeatRB_actionPerformed(null);
                dialog.hourSpin.setValue(event.getPeriod());
            } else if (repeatType == EventsManager.REPEAT_DAILY) {
                dialog.dailyRepeatRB.setSelected(true);
                dialog.dailyRepeatRB_actionPerformed(null);
                dialog.daySpin.setValue(event.getPeriod());
            } else if (repeatType == EventsManager.REPEAT_WEEKLY) {
                dialog.weeklyRepeatRB.setSelected(true);
                dialog.weeklyRepeatRB_actionPerformed(null);
                int d = event.getPeriod() - 1;

                if (Configuration.get("FIRST_DAY_OF_WEEK").equals("mon")) {
                    d--;
                    if (d < 0) {
                        d = 6;
                    }
                }

                dialog.weekdaysCB.setSelectedIndex(d);
            } else if (repeatType == EventsManager.REPEAT_MONTHLY) {
                dialog.monthlyRepeatRB.setSelected(true);
                dialog.monthlyRepeatRB_actionPerformed(null);
                dialog.dayOfMonthSpin.setValue(event.getPeriod());
            } else if (repeatType == EventsManager.REPEAT_YEARLY) {
                dialog.yearlyRepeatRB.setSelected(true);
                dialog.yearlyRepeatRB_actionPerformed(null);
                dialog.dayOfMonthSpin.setValue(event.getPeriod());
            }

            if (event.getEndDate() != null) {
                dialog.endDate.getModel().setValue(event.getEndDate().getDate());
                dialog.enableEndDateCB.setSelected(true);
                dialog.enableEndDateCB_actionPerformed(null);
            }

            if (event.getWorkingDays()) {
                dialog.workingDaysOnlyCB.setSelected(true);
            }
        }

        Dimension frameSize = App.getFrame().getSize();
        Point location = App.getFrame().getLocation();
        dialog.setLocation((frameSize.width - dialog.getSize().width) / 2 + location.x, (frameSize.height - dialog.getSize().height) / 2 + location.y);
        dialog.setVisible(true);

        if (dialog.CANCELLED) {
            return;
        }

        EventsManager.removeEvent(event);
        Calendar calendar = new GregorianCalendar(Local.getCurrentLocale()); //Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        calendar.setTime(((Date) dialog.timeSpin.getModel().getValue()));//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        int minute = calendar.get(Calendar.MINUTE);//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        //int hh = ((Date) dlg.timeSpin.getModel().getValue()).getHours();
        //int mm = ((Date) dlg.timeSpin.getModel().getValue()).getMinutes();
        String text = dialog.textField.getText();

        if (dialog.noRepeatRB.isSelected()) {
            EventsManager.createEvent(CurrentDate.get(), hour, minute, text);
        } else {
            updateEvents(dialog, hour, minute, text);
        }

        saveEvents();
    }

    void newEventB_actionPerformed(ActionEvent actionEvent) {
        Calendar calender = CurrentDate.get().getCalendar();
        // round down to hour
        calender.set(Calendar.MINUTE, 0);
        Util.debug("Default time is " + calender);
        newEventB_actionPerformed(actionEvent, null, calender.getTime(), calender.getTime());
    }

    // The event handler that creates the "New event" Popup menu
    void newEventB_actionPerformed(ActionEvent actionEvent, String taskText, Date startDate, Date endDate) {
        EventDialog dialog = new EventDialog(App.getFrame(), Local.getString("New event"));
        Dimension formSize = App.getFrame().getSize();
        Point location = App.getFrame().getLocation();

        if (taskText != null) {
            //dlg.setFont((new Font("serif",Font.PLAIN,14)));
            dialog.textField.setText(taskText);
        }

        dialog.startDate.getModel().setValue(startDate);
        dialog.endDate.getModel().setValue(endDate);
        dialog.timeSpin.getModel().setValue(startDate);

        dialog.setLocation((formSize.width - dialog.getSize().width) / 2 + location.x, (formSize.height - dialog.getSize().height) / 2 + location.y);
        dialog.setEventDate(startDate);
        dialog.setFont((new Font("serif", Font.PLAIN, 20)));
        dialog.setVisible(true);

        if (dialog.CANCELLED) {
            return;
        }

        Calendar calendar = new GregorianCalendar(Local.getCurrentLocale()); //Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        calendar.setTime(((Date) dialog.timeSpin.getModel().getValue()));//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM
        int minute = calendar.get(Calendar.MINUTE);//Fix deprecated methods to get hours
        //by (jcscoobyrs) 14-Nov-2003 at 10:24:38 AM

        //int hh = ((Date) dlg.timeSpin.getModel().getValue()).getHours();
        //int mm = ((Date) dlg.timeSpin.getModel().getValue()).getMinutes();
        String text = dialog.textField.getText();

        CalendarDate eventCalendarDate = new CalendarDate(dialog.getEventDate());

        if (dialog.noRepeatRB.isSelected()) {
            EventsManager.createEvent(eventCalendarDate, hour, minute, text);
        } else {
            updateEvents(dialog, hour, minute, text);
        }

        saveEvents();
    }

    private void saveEvents() {
        CurrentStorage.get().storeEventsManager();
        eventsTable.refresh();
        //sets tables font size
        eventsTable.setFont((new Font("serif", Font.PLAIN, Integer.parseInt(Configuration.get("BASE_FONT_SIZE").toString()))));
        EventsScheduler.init();
        parentPanel.calendar.jnCalendar.updateUI();
        parentPanel.updateIndicators();
    }

    // The event handler that handles when you click "ok" in the new event popup window, and the event is repeatable
    private void updateEvents(EventDialog dialog, int hour, int minute, String text) {
        int repeatType;
        int period;

        CalendarDate startDate = new CalendarDate((Date) dialog.startDate.getModel().getValue());
        CalendarDate endDate = null;

        if (dialog.enableEndDateCB.isSelected()) {
            endDate = new CalendarDate((Date) dialog.endDate.getModel().getValue());
        }

        if (dialog.minutelyRepeatRB.isSelected()) {
            repeatType = EventsManager.REPEAT_MINUTELY;
            period = (Integer) dialog.minuteSpin.getModel().getValue();
        } else if (dialog.hourlyRepeatRB.isSelected()) {
            repeatType = EventsManager.REPEAT_HOURLY;
            period = (Integer) dialog.hourSpin.getModel().getValue();
        } else if (dialog.dailyRepeatRB.isSelected()) {
            repeatType = EventsManager.REPEAT_DAILY;
            period = (Integer) dialog.daySpin.getModel().getValue();
        } else if (dialog.weeklyRepeatRB.isSelected()) {
            repeatType = EventsManager.REPEAT_WEEKLY;
            period = dialog.weekdaysCB.getSelectedIndex() + 1;

            if (Configuration.get("FIRST_DAY_OF_WEEK").equals("mon")) {
                if (period == 7) {
                    period = 1;
                } else {
                    period++;
                }
            }

        } else if (dialog.yearlyRepeatRB.isSelected()) {
            repeatType = EventsManager.REPEAT_YEARLY;
            period = startDate.getCalendar().get(Calendar.DAY_OF_YEAR);

            if ((startDate.getYear() % 4) == 0 && startDate.getCalendar().get(Calendar.DAY_OF_YEAR) > 60) {
                period--;
            }

        } else {
            repeatType = EventsManager.REPEAT_MONTHLY;
            period = (Integer) dialog.dayOfMonthSpin.getModel().getValue();
        }

        EventsManager.createRepeatableEvent(repeatType, startDate, endDate, period, hour, minute, text, dialog.workingDaysOnlyCB.isSelected());
    }

    void removeEventB_actionPerformed(ActionEvent actionEvent) {
        String message;
        net.sf.memoranda.Event event;

        if (eventsTable.getSelectedRows().length > 1) {
            message = Local.getString("Remove")
                    + " "
                    + eventsTable.getSelectedRows().length
                    + " "
                    + Local.getString("events")
                    + "\n" + Local.getString("Are you sure?");
        } else {
            event = (net.sf.memoranda.Event) eventsTable.getModel().getValueAt(
                    eventsTable.getSelectedRow(),
                    EventsTable.EVENT);

            message = Local.getString("Remove event")
                    + "\n'"
                    + event.getText()
                    + "'\n"
                    + Local.getString("Are you sure?");
        }

        int confirmation = JOptionPane.showConfirmDialog(
                App.getFrame(), message, Local.getString("Remove event"),
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        for (int i = 0; i < eventsTable.getSelectedRows().length; i++) {
            event = (net.sf.memoranda.Event) eventsTable.getModel().getValueAt(
                    eventsTable.getSelectedRows()[i], EventsTable.EVENT);
            EventsManager.removeEvent(event);
        }

        eventsTable.getSelectionModel().clearSelection();
        /*
        CurrentStorage.get().storeEventsManager();
        eventsTable.refresh();
        EventsScheduler.init();
        parentPanel.calendar.jnCalendar.updateUI();
        parentPanel.updateIndicators();
        */
        saveEvents();
    }

    void ppEditEvent_actionPerformed(ActionEvent actionEvent) {
        editEventB_actionPerformed(actionEvent);
    }

    void ppRemoveEvent_actionPerformed(ActionEvent actionEvent) {
        removeEventB_actionPerformed(actionEvent);
    }

    void ppNewEvent_actionPerformed(ActionEvent actionEvent) {
        newEventB_actionPerformed(actionEvent);
    }

    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent mouseEvent) {
            if ((mouseEvent.getClickCount() == 2) && (eventsTable.getSelectedRow() > -1)) {
                editEventB_actionPerformed(null);
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {
            maybeShowPopup(mouseEvent);
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            maybeShowPopup(mouseEvent);
        }

        private void maybeShowPopup(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                eventPPMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }
}