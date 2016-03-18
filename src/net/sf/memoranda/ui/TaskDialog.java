package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
//import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;

import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.Task;
import javax.swing.JSplitPane;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SpinnerModel;

/*$Id: TaskDialog.java,v 1.25 2005/12/01 08:12:26 alexeya Exp $*/
public class TaskDialog extends JDialog {
    JPanel mPanel = new JPanel(new BorderLayout());
    JPanel areaPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton cancelB = new JButton();
    JButton okB = new JButton();
    Border border1;
    Border border2;
    JPanel dialogTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel header = new JLabel();
    public boolean CANCELLED = true;
    Border border3;
    Border border4;
    JPanel jPanel2 = new JPanel(new GridLayout(2, 2));
    JTextField todoField = new JTextField();
    
    JTextField effortField = new JTextField();
    JTextArea descriptionField = new JTextArea();
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
    Border border8;
    CalendarFrame startCalFrame = new CalendarFrame();///
    CalendarFrame endCalFrame = new CalendarFrame();
    CalendarFrame endCalRptFrame = new CalendarFrame();
    String[] priority = {Local.getString("Lowest"), Local.getString("Low"),
        Local.getString("Normal"), Local.getString("High"),
        Local.getString("Highest")};
    boolean ignoreStartChanged = false;
    boolean ignoreEndChanged = false;
    boolean ignoreRptEndChanged = false;
        JPanel jPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel jPanel6 = new JPanel();
    JLabel jLabel6 = new JLabel();
    JButton setStartDateB = new JButton();
    JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel jPanelRepeatToggle = new JPanel(new GridLayout(0,1));
    JLabel jLabel2 = new JLabel();
    JSpinner startDate;
    JSpinner endDate;
    JSpinner endDateRpt;
    JButton setEndDateB = new JButton();
    JPanel jPanelRepeat = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    JButton setNotifB = new JButton();
    JComboBox<?> priorityCB = new JComboBox<Object>(priority);
    JLabel jLabel7 = new JLabel();
    // added by rawsushi
    JLabel jLabelEffort = new JLabel();
    JLabel jLabelDescription = new JLabel();
	JCheckBox chkEndDate;
	

	JComboBox<?> cmboRepeatType = new JComboBox<Object>(Task.REPEAT_FREQUENCIES_LIST);
	public JCheckBox chkWorkingDays = new JCheckBox();
	JLabel jLabelProgress = new JLabel();
	JSpinner progress = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
	
	//Forbid to set dates outside the bounds
	CalendarDate startDateMin = CurrentProject.get().getStartDate();
	CalendarDate startDateMax = CurrentProject.get().getEndDate();
	CalendarDate endDateMin = startDateMin;
	CalendarDate endDateMax = startDateMax;
	private final JPanel panel = new JPanel();
	private final JCheckBox chkEndDateRpt = new JCheckBox();
	private final JLabel lblEndRepeat = new JLabel();
	private final JButton setEndDateRptB = new JButton();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_3 = new JPanel();
	private final JPanel panel_4 = new JPanel();
	private final JPanel panel_5 = new JPanel();
	private final JPanel panel_6 = new JPanel();
	private final JPanel panel_7 = new JPanel();
	private final JPanel panel_8 = new JPanel();
	private final JLabel lblTaskName = new JLabel();
	private final JPanel panel_9 = new JPanel();
	
    
    public TaskDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();            
            pack();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    void jbInit() throws Exception {
	this.setResizable(false);
	this.setSize(new Dimension(430,300));
        border1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        border2 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(142, 142, 142));
        border3 = new TitledBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), 
        Local.getString("To Do"), TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        border4 = BorderFactory.createEmptyBorder(0, 5, 0, 5);
//        border5 = BorderFactory.createEmptyBorder();
//        border6 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
//            Color.white, Color.white, new Color(178, 178, 178),
//            new Color(124, 124, 124));
//        border7 = BorderFactory.createLineBorder(Color.white, 2);
        border8 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(178, 178, 178));
        cancelB.setMaximumSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setText(Local.getString("Cancel"));
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelB_actionPerformed(e);
            }
        });
		cmboRepeatType_actionPerformed(null);
		
        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        
        this.getRootPane().setDefaultButton(okB);
        mPanel.setBorder(border1);
        areaPanel.setBorder(border2);
        dialogTitlePanel.setBackground(Color.WHITE);
        dialogTitlePanel.setBorder(border4);
        //dialogTitlePanel.setMinimumSize(new Dimension(159, 52));
        //dialogTitlePanel.setPreferredSize(new Dimension(159, 52));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(Local.getString("To do"));
        header.setIcon(new ImageIcon(net.sf.memoranda.ui.TaskDialog.class.getResource(
            "resources/icons/task48.png")));
        GridBagConstraints gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbCon.anchor = GridBagConstraints.WEST;
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 3;
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.SHORT);
        getContentPane().add(mPanel);
        mPanel.add(areaPanel, BorderLayout.CENTER);
        mPanel.add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.add(okB, null);
        buttonsPanel.add(cancelB, null);
        this.getContentPane().add(dialogTitlePanel, BorderLayout.NORTH);
        dialogTitlePanel.add(header, null);
        
        areaPanel.add(panel_4, BorderLayout.NORTH);
                panel_4.setLayout(new GridLayout(2, 1, 0, 0));
                
                panel_4.add(panel_5);
                panel_5.setLayout(new GridLayout(3, 1, 0, 0));
                FlowLayout flowLayout_2 = (FlowLayout) panel_8.getLayout();
                flowLayout_2.setVgap(10);
                flowLayout_2.setAlignment(FlowLayout.LEFT);
                
                panel_5.add(panel_8);
                lblTaskName.setVerticalAlignment(SwingConstants.BOTTOM);
                lblTaskName.setText("Task Name");
                lblTaskName.setMinimumSize(new Dimension(60, 12));
                lblTaskName.setMaximumSize(new Dimension(100, 12));
                
                panel_8.add(lblTaskName);
                
                panel_5.add(panel_6);
                panel_6.add(todoField);
                
        todoField.setBorder(border8);
        todoField.setPreferredSize(new Dimension(375, 24));
                FlowLayout flowLayout_1 = (FlowLayout) panel_7.getLayout();
                flowLayout_1.setVgap(10);
                flowLayout_1.setAlignment(FlowLayout.LEFT);
                
                panel_5.add(panel_7);
                jLabelDescription.setVerticalAlignment(SwingConstants.BOTTOM);
                panel_7.add(jLabelDescription);
                
                jLabelDescription.setMaximumSize(new Dimension(100, 12));
                jLabelDescription.setMinimumSize(new Dimension(60, 12));
                jLabelDescription.setText(Local.getString("Description"));
        
                descriptionField.setBorder(border8);
                descriptionField.setPreferredSize(new Dimension(375, 387)); // 3 additional pixels from 384 so that the last line is not cut off
                descriptionField.setLineWrap(true);
                descriptionField.setWrapStyleWord(true);
                panel_4.add(descriptionScrollPane);
                descriptionScrollPane.setPreferredSize(new Dimension(375,96));
        areaPanel.add(jPanel2, BorderLayout.CENTER);
        jPanel2.add(jPanel6, null);
        jPanel6.setLayout(new GridLayout(0, 1, 0, 0));

        
        jPanel6.add(panel_3);
                panel_3.add(jLabel6);
        
                jLabel6.setText(Local.getString("Start date"));
                //jLabel6.setPreferredSize(new Dimension(60, 16));
                jLabel6.setMinimumSize(new Dimension(60, 16));
                jLabel6.setMaximumSize(new Dimension(100, 16));
                
                        startDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
                        panel_3.add(startDate);
                        
                                startDate.setBorder(border8);
                                startDate.setPreferredSize(new Dimension(80, 24));                
                                // //Added by (jcscoobyrs) on 14-Nov-2003 at 10:45:16 PM
                                startDate.setEditor(new JSpinner.DateEditor(startDate, sdf.toPattern()));
                                panel_3.add(setStartDateB);
                                setStartDateB.setMinimumSize(new Dimension(24, 24));
                                setStartDateB.setPreferredSize(new Dimension(24, 24));
                                setStartDateB.setText("");
                                setStartDateB.setIcon(
                                    new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/calendar.png")));
                                setStartDateB.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        setStartDateB_actionPerformed(e);
                                    }
                                });
                                
                                        startDate.addChangeListener(new ChangeListener() {
                                            public void stateChanged(ChangeEvent e) {
                                            	// it's an ugly hack so that the spinner can increase day by day
                                            	SpinnerDateModel sdm = new SpinnerDateModel((Date)startDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
                                            	startDate.setModel(sdm);
                                
                                                if (ignoreStartChanged)
                                                    return;
                                                ignoreStartChanged = true;
                                                Date sd = (Date) startDate.getModel().getValue();
                                                Date ed = (Date) endDate.getModel().getValue();
                                                if (sd.after(ed) && chkEndDate.isSelected()) {
                                                    startDate.getModel().setValue(ed);
                                                    sd = ed;
                                                }
                                				if ((startDateMax != null) && sd.after(startDateMax.getDate())) {
                                					startDate.getModel().setValue(startDateMax.getDate());
                                                    sd = startDateMax.getDate();
                                				}
                                                if ((startDateMin != null) && sd.before(startDateMin.getDate())) {
                                                    startDate.getModel().setValue(startDateMin.getDate());
                                                    sd = startDateMin.getDate();
                                                }
                                                startCalFrame.cal.set(new CalendarDate(sd));
                                                ignoreStartChanged = false;
                                            }
                                        });
        endDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
        endDateRpt = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
        
        jLabel2.setMaximumSize(new Dimension(270, 16));
        //jLabel2.setPreferredSize(new Dimension(60, 16));
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setText(Local.getString("End date"));
        endDate.setBorder(border8);
        endDate.setPreferredSize(new Dimension(80, 24));
        
		endDate.setEditor(new JSpinner.DateEditor(endDate, sdf.toPattern()));
        chkEndDate = new JCheckBox();
        chkEndDate.setSelected(false);
        chkEndDate.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		chkEndDate_actionPerformed(e);
        	}
        });
        chkEndDateRpt.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		chkEndDateRpt_actionPerformed(e);
        	}
        });
        endDateRpt.setEditor(new JSpinner.DateEditor(endDateRpt, sdf.toPattern()));
        chkEndDate_actionPerformed(null);
		chkEndDateRpt_actionPerformed(null);
        endDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)endDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	endDate.setModel(sdm);
            	
                if (ignoreEndChanged)
                    return;
                ignoreEndChanged = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDate.getModel().getValue();				
				if (ed.before(sd)) {
                    endDate.getModel().setValue(ed);
                    ed = sd;
                }
				if ((endDateMax != null) && ed.after(endDateMax.getDate())) {
					endDate.getModel().setValue(endDateMax.getDate());
                    ed = endDateMax.getDate();
				}
                if ((endDateMin != null) && ed.before(endDateMin.getDate())) {
                    endDate.getModel().setValue(endDateMin.getDate());
                    ed = endDateMin.getDate();
                }
				endCalFrame.cal.set(new CalendarDate(ed));
                ignoreEndChanged = false;
            }
        });
        
        endDateRpt.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)endDateRpt.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	endDateRpt.setModel(sdm);
            	
                if (ignoreRptEndChanged )
                    return;
                ignoreRptEndChanged  = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDateRpt.getModel().getValue();				
				if (ed.before(sd)) {
                    endDateRpt.getModel().setValue(ed);
                    ed = sd;
                }
				if ((startDateMin != null) && ed.before(startDateMin.getDate())) {
                    startDate.getModel().setValue(startDateMin.getDate());
                    ed = startDateMin.getDate();
                }
				endCalRptFrame.cal.set(new CalendarDate(ed));
				ignoreRptEndChanged  = false;
            }
        });
        setEndDateB.setMinimumSize(new Dimension(24, 24));
        setEndDateB.setPreferredSize(new Dimension(24, 24));
        setEndDateB.setText("");
        setEndDateB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/calendar.png")));
        setEndDateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEndDateB_actionPerformed(e);
            }
        });
        setEndDateRptB.setMinimumSize(new Dimension(24, 24));
        setEndDateRptB.setPreferredSize(new Dimension(24, 24));
        setEndDateRptB.setText("");
        setEndDateRptB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/calendar.png")));
        setEndDateRptB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setEndDateRptB_actionPerformed(e);
            }
        });
		jPanel6.add(jPanel1);
		jPanel1.add(chkEndDate, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(endDate, null);
        jPanel1.add(setEndDateB, null);
        

        jPanel2.add(jPanelRepeat, null);
        jPanelRepeat.add(cmboRepeatType);
        cmboRepeatType.setSelectedIndex(0);
        cmboRepeatType.setEnabled(true);
        cmboRepeatType.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		cmboRepeatType_actionPerformed(e);
        	}
        });
        
        jPanel2.add(panel_1);
                panel_1.setLayout(new GridLayout(2, 1, 0, 0));
                
                panel_1.add(panel_9);
                panel_9.add(jLabelEffort);
        
                jLabelEffort.setMaximumSize(new Dimension(100, 16));
                jLabelEffort.setMinimumSize(new Dimension(60, 16));
                jLabelEffort.setText(Local.getString("Est Effort(hrs)"));
                panel_9.add(effortField);
                effortField.setBorder(border8);
                effortField.setPreferredSize(new Dimension(30, 24));
                panel_9.add(jLabelProgress);
                
                jLabelProgress.setText("Progress(hrs)");
                panel_9.add(progress);
                
                setNotifB.setText(Local.getString("Set notification"));
                setNotifB.setIcon(
                    new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/notify.png")));
                setNotifB.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setNotifB_actionPerformed(e);
                    }
                });
                
                        priorityCB.setFont(new java.awt.Font("Dialog", 0, 11));
                        panel_1.add(jPanel4);
                        jPanel4.add(jLabel7);
                        jLabel7.setMaximumSize(new Dimension(100, 16));
                        jLabel7.setMinimumSize(new Dimension(60, 16));
                        //jLabel7.setPreferredSize(new Dimension(60, 16));
                        jLabel7.setText(Local.getString("Priority"));
                        jPanel4.add(priorityCB, null);
                        
                        jPanel4.add(setNotifB, null);
                        
                        priorityCB.setSelectedItem(Local.getString("Normal"));
        jPanel2.add(jPanelRepeatToggle);
        jPanelRepeatToggle.setEnabled(false);
        chkWorkingDays.setHorizontalAlignment(SwingConstants.CENTER);
        jPanelRepeatToggle.add(chkWorkingDays);
        chkWorkingDays.setSelected(false);
        chkWorkingDays.setEnabled(true);
        chkWorkingDays.setText("Working Days Only");
        chkWorkingDays.setHorizontalTextPosition(SwingConstants.LEADING);
        chkEndDateRpt.setSelected(false);
        jPanelRepeatToggle.add(panel);
        
        panel.add(chkEndDateRpt);
        lblEndRepeat.setText("End Repeat");
        lblEndRepeat.setMaximumSize(new Dimension(270, 16));
        lblEndRepeat.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEndRepeat.setEnabled(false);
        
        panel.add(lblEndRepeat);
        endDateRpt.setPreferredSize(new Dimension(80, 24));
        endDateRpt.setBorder(border8);
        
        panel.add(endDateRpt);         
        setEndDateRptB.setText("");
        setEndDateRptB.setPreferredSize(new Dimension(24, 24));
        setEndDateRptB.setMinimumSize(new Dimension(24, 24));
        setEndDateRptB.setEnabled(false);
        
        panel.add(setEndDateRptB);
        startCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreStartChanged)
                    return;
                startDate.getModel().setValue(startCalFrame.cal.get().getCalendar().getTime());
            }
        });
        
        endCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreEndChanged)
                    return;
                endDate.getModel().setValue(endCalFrame.cal.get().getCalendar().getTime());
            }
        });
        
        endCalRptFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreEndChanged)
                    return;
                endDateRpt.getModel().setValue(endCalRptFrame.cal.get().getCalendar().getTime());
            }
        });
    }

	public void setStartDate(CalendarDate d) {
		this.startDate.getModel().setValue(d.getDate());
	}
	
	public void setEndDate(CalendarDate d) {		
		if (d != null) 
			this.endDate.getModel().setValue(d.getDate());
	}
	
	public void setStartDateLimit(CalendarDate min, CalendarDate max) {
		this.startDateMin = min;
		this.startDateMax = max;
	}
	
	public void setEndDateLimit(CalendarDate min, CalendarDate max) {
		this.endDateMin = min;
		this.endDateMax = max;
	}
	
    void okB_actionPerformed(ActionEvent e) {
	CANCELLED = false;
        this.dispose();
    }

    void cancelB_actionPerformed(ActionEvent e) {
        this.dispose();
    }
	
    void chkEndDate_actionPerformed(ActionEvent e) {
		endDate.setEnabled(chkEndDate.isSelected());
		setEndDateB.setEnabled(chkEndDate.isSelected());
		jLabel2.setEnabled(chkEndDate.isSelected());
		if(chkEndDate.isSelected()) {
			Date currentEndDate = (Date) endDate.getModel().getValue();
			Date currentStartDate = (Date) startDate.getModel().getValue();
			if(currentEndDate.getTime() < currentStartDate.getTime()) {
				endDate.getModel().setValue(currentStartDate);
			}
		}
	}
    void chkEndDateRpt_actionPerformed(ActionEvent e) {
		endDateRpt.setEnabled(chkEndDateRpt.isSelected());
		setEndDateRptB.setEnabled(chkEndDateRpt.isSelected());
		lblEndRepeat.setEnabled(chkEndDateRpt.isSelected());
		if(chkEndDateRpt.isSelected()) {
			Date currentEndDate = (Date) endDate.getModel().getValue();
			Date currentStartDate = (Date) startDate.getModel().getValue();
			if(currentEndDate.getTime() < currentStartDate.getTime()) {
				endDate.getModel().setValue(currentStartDate);
			}
		}
	}
	void cmboRepeatType_actionPerformed(ActionEvent e) {
		jPanelRepeatToggle.setEnabled(cmboRepeatType.getSelectedIndex()!=0);
	}

	
    void setStartDateB_actionPerformed(ActionEvent e) {
    	startCalFrame.hide();
    	startCalFrame.setLocation(setStartDateB.getLocation());
        startCalFrame.setSize(200, 200);
        this.getLayeredPane().add(startCalFrame);
        startCalFrame.show();

    }

    void setEndDateB_actionPerformed(ActionEvent e) {
    	endCalFrame.hide();
    	endCalFrame.setLocation(setEndDateB.getLocation());
        endCalFrame.setSize(200, 200);
        this.getLayeredPane().add(endCalFrame);
        endCalFrame.show();
    }
    
    void setEndDateRptB_actionPerformed(ActionEvent e) {
    	endCalRptFrame.hide();
    	endCalRptFrame.setLocation(setEndDateRptB.getLocation());
    	endCalRptFrame.setSize(200, 200);
        this.getLayeredPane().add(endCalRptFrame);
        endCalRptFrame.show();
    }
    
    void setNotifB_actionPerformed(ActionEvent e) {
    	((AppFrame)App.getFrame()).workPanel.dailyItemsPanel.eventsPanel.newEventB_actionPerformed(e, 
			this.todoField.getText(), (Date)startDate.getModel().getValue(),(Date)endDate.getModel().getValue());
    }

}