/**
 * 
 */
package net.sf.memoranda.ui;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JScrollPane;

public class PSPSummaryTable extends JPanel {
	//STUB LITERALS
	//  Get the following values from a static collection of tagged tasks
	String[] tagNameList = {"Planning", "Design", "Code","Test", 
			"Code Review", "CI Build", "Bookkeeping", "Postmortem", null };
	double[] estimatedTimeList = {10, 5, 350, 2, 
			10, 3, 15, 120, 73 };
	double[] actualTimeList = {3, 17, 311, 120, 
			240, 48, 15, 180, 73};
	double[] estimatedDefectList = {0, 0, 5, 0, //This needs to be added to task Dialogue 
			0, 0, 0, 0, 2};
	double[] actualDefectList = {0, 5, 23, 6, 
			3, 1, 2, 0, 0};
	//  Get the following values from project properties. 
	//They will all likely need to be added to the project  
	//class, the serialization, and the GUI, somewhere.
	int projectLinesOfCode = 1200;
	//COMPONENTS
	private JTable tblTime;
	@SuppressWarnings("serial")
	public PSPSummaryTable() {
		
		tblTime = new JTable();
		tblTime.setModel(new DefaultTableModel(
			new Object[][] {
				{"TOTAL", 0.0, 0.0},
			},
			new String[] {
				"Time in Phase (Tags)", "Estimated", "Actual"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblTime.getColumnModel().getColumn(0).setPreferredWidth(110);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		tblTime.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//add(table);
		add(new JScrollPane(tblTime));
	}

}
