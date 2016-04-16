/**
 * 
 */
package net.sf.memoranda.ui;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.functors.ForClosure;

import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

@SuppressWarnings("serial")
public class PSPSummaryTable extends JPanel {
	//STUB LITERALS
	//  Get the following values from a static collection of tagged tasks
	String[] tagNameList = {"Planning", "Design", "Code","Test", 
			"Code Review", "CI Build", "Bookkeeping", "Postmortem"};
	double[] estimatedTimeList = {10, 5, 350, 2, 
			10, 3, 15, 120};
	//We may not have actual values for all tasks. In this case, return values so far, or 0.
	double[] actualTimeList = {3, 17, 311, 120, 
			240, 48, 15, 180};
	double[] estimatedDefectList = {0, 0, 5, 0, //This needs to be added to task Dialogue 
			0, 0, 0, 0};
	//We may not have actual values for all tasks. In this case, return values so far, or 0.
	double[] actualDefectList = {0, 5, 23, 6, 
			3, 1, 2, 0};
	//  Get the following values from project properties. 
	//They will all likely need to be added to the project  
	//class, the serialization, and the GUI, somewhere.
	double estimatedProjectLinesOfCode = 1200;
	double actualProjectLinesOfCode = 3600;
	//NON-GUI VARIABLES
	double estimatedTotalTime;
	double actualTotalTime;
	double estimatedTotalDefects;
	double actualTotalDefects;
	//COMPONENTS
	private JScrollPane paneTime;
	private JTable tblTime;
	private DefaultTableModel modelTime;
	private JScrollPane paneDefects;
	private JTable tblDefects;
	private DefaultTableModel modelDefects;
	private JScrollPane paneLoC;
	private JTable tblLoC;
	
	public PSPSummaryTable() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		assert(tagNameList.length == estimatedTimeList.length);
		assert(tagNameList.length == actualTimeList.length);
		assert(tagNameList.length == estimatedDefectList.length);
		assert(tagNameList.length == actualDefectList.length);
		estimatedTotalTime = 0.0;
		actualTotalTime = 0.0;
		estimatedTotalDefects = 0;
		actualTotalDefects = 0;
		for (int i = 0; i < estimatedTimeList.length; i++) {
			estimatedTotalTime+=estimatedTimeList[i];
		}
		for (int i = 0; i < actualTimeList.length; i++) {
			actualTotalTime+=actualTimeList[i];
		}
		for (int i = 0; i < estimatedDefectList.length; i++) {
			estimatedTotalDefects+=estimatedDefectList[i];
		}
		for (int i = 0; i < actualDefectList.length; i++) {
			actualTotalDefects+=actualDefectList[i];
		}
		//Time Table
		tblTime = new JTable();
		tblTime;
		modelTime = new DefaultTableModel(
				new Object[][] {
					{"TOTAL", 0.0, 0.0},
				},
				new String[] {
					"Time in Phase (Tags)", "Estimated", "Actual"
				}
			) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		for (int i = tagNameList.length; i > 0; i--) {
			modelTime.insertRow(0, 
					new Object[]{tagNameList[i-1], 
							estimatedTimeList[i-1],
							actualTimeList[i-1]});
		}
		tblTime.setModel(modelTime);
		
		tblTime.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblTime.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneTime = new JScrollPane(tblTime); 
		add(paneTime);
		//Defect Table
		tblDefects = new JTable();
		modelDefects = new DefaultTableModel(
				new Object[][] {
					{"TOTAL", 0, 0},
				},
				new String[] {
					"Defects in Phase (Tags)", "Estimated", "Actual"
				}
			) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, Integer.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
		for (int i = tagNameList.length; i > 0; i--) {
			modelDefects.insertRow(0, 
					new Object[]{tagNameList[i-1], 
							estimatedDefectList[i-1],
							actualDefectList[i-1]});
		}
		tblDefects.setModel(modelDefects);
		tblDefects.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblDefects.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneDefects = new JScrollPane(tblDefects); 
		add(paneDefects);
		//LoC Table
		tblLoC = new JTable();
		tblLoC.setModel(new DefaultTableModel(
			new Object[][] {
				{"Program Size (LoC)", 
					estimatedProjectLinesOfCode, 
					actualProjectLinesOfCode},
				{"LoC/Hour", 
					estimatedProjectLinesOfCode/estimatedTotalTime, 
					actualProjectLinesOfCode/actualTotalTime},
				{"Defects/KLoC", 
					(estimatedTotalDefects)/((estimatedProjectLinesOfCode)/1000.0), 
					(actualTotalDefects)/((actualProjectLinesOfCode)/1000.0)},
			},
			new String[] {
				"Lines of Code", "Estimated", "Actual"
			}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblLoC.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblLoC.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneLoC = new JScrollPane(tblLoC); 
		add(paneLoC);
	}

}
