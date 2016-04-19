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
	private PSPTableModel modelTime;
	private JScrollPane paneDefects;
	private JTable tblDefects;
	private PSPTableModel modelDefects;
	private JScrollPane paneLoC;
	private JTable tblLoC;
	private PSPTableModel modelLoC;
	
	public PSPSummaryTable() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//Time Table
		tblTime = new JTable();
		modelTime = new PSPTableModel(PSPTableModel.TableType.TIME);
		tblTime.setModel(modelTime);		
		tblTime.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblTime.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneTime = new JScrollPane(tblTime); 
		add(paneTime);
		//Defect Table
		tblDefects = new JTable();
		modelDefects = new PSPTableModel(PSPTableModel.TableType.DEFECT);		
		tblDefects.setModel(modelDefects);
		tblDefects.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblDefects.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneDefects = new JScrollPane(tblDefects); 
		add(paneDefects);
		//LoC Table
		tblLoC = new JTable();
		modelLoC = new PSPTableModel(PSPTableModel.TableType.LOC);		
		tblLoC.setModel(modelLoC);
		tblLoC.getColumnModel().getColumn(0).setPreferredWidth(110);
		tblLoC.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		paneLoC = new JScrollPane(tblLoC); 
		add(paneLoC);
	}

}
