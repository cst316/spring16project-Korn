package net.sf.memoranda.ui;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PSPTableModel extends DefaultTableModel implements TableModel {
	//STUB LITERALS
	//**Get the following values from a static collection of tagged tasks
	private String[] tagNameList = {"Planning", "Design", "Code","Test", 
			"Code Review", "CI Build", "Bookkeeping", "Postmortem"};
	private double[] estimatedTimeList = {10, 5, 350, 2, 
			10, 3, 15, 120};
	//We may not have actual values for all tasks. In this case, return values so far, or 0.
	private double[] actualTimeList = {3, 17, 311, 120, 
			240, 48, 15, 180};
	private double[] estimatedDefectList = {0, 0, 5, 0, //This needs to be added to task Dialogue 
			0, 0, 0, 0};
	//We may not have actual values for all tasks. In this case, return values so far, or 0.
	private double[] actualDefectList = {0, 5, 23, 6, 
			3, 1, 2, 0};
	//**Get the following values from project properties. 
	//They will all likely need to be added to the project  
	//class, the serialization, and the GUI, somewhere.
	private double estimatedProjectLinesOfCode = 1200;
	private double actualProjectLinesOfCode = 3600;
	//VARIABLES
	private double estimatedTotalTime;
	private double actualTotalTime;
	private double estimatedTotalDefects;
	private double actualTotalDefects;
	
	
	public static enum TableType {TIME,DEFECT,LOC}
	public PSPTableModel(TableType tableType){
		super();
		validateData();
		//Set Up Columns 
		String firstColumnName="";
		switch(tableType){
		case TIME:
			firstColumnName = "Time in Phase (Tags)";								
			break;
		case DEFECT:
			firstColumnName = "Defects in Phase (Tags)";
			break;
		case LOC:
			firstColumnName = "Lines of Code";					
			break;
		default:
			System.out.println("[DEBUG] Error: Improperly Defined PSPTableModel.");			
		}
		this.setColumnIdentifiers(
				new String[] {firstColumnName, "Estimated", "Actual"});	
		//Add data to rows
		if (tableType==TableType.LOC) {
			this.insertRow(0, 
					new Object[]{"Defects/KLoC", 
						(estimatedTotalDefects)/((estimatedProjectLinesOfCode)/1000.0), 
						(actualTotalDefects)/((actualProjectLinesOfCode)/1000.0)});
			this.insertRow(0, 
					new Object[]{"LoC/Hour", 
							estimatedProjectLinesOfCode/estimatedTotalTime, 
							actualProjectLinesOfCode/actualTotalTime};
			this.insertRow(0, 
					new Object[]{"Program Size (LoC)", 
							estimatedProjectLinesOfCode, 
							actualProjectLinesOfCode});
		} else {
			this.insertRow(0, 
					new Object[]{"TOTAL",0.0, 0.0});
		}
	}
	
	public void validateData(){
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
