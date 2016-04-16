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
		//Time Table
		tblTime = new JTable();
		tblTime;
		modelTime = new DefaultTableModel( {
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
