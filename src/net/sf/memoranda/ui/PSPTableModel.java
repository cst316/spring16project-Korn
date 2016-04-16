package net.sf.memoranda.ui;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PSPTableModel extends DefaultTableModel implements TableModel {
	public static enum TableType {TIME,DEFECT,LOC}
	public PSPTableModel(TableType tableType){
		super();
		switch(tableType){
		case TIME:
			this.insertRow(0, new Object[][] {
				{"TOTAL", 0.0, 0.0},
			});
			new String[] {
					"Time in Phase (Tags)", "Estimated", "Actual"
				};
		case DEFECT:
			
		case LOC:
			
		default:
			System.out.println("[DEBUG] Error: Improperly Defined PSPTableModel.");
		}
			
	}
	
	@SuppressWarnings("rawtypes")
	private Class[] columnTypes = new Class[] {
			String.class, Double.class, Double.class
		};
	
	//Overrides
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}
	
}
