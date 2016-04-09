/**
 * 
 */
package net.sf.memoranda.ui;

import javax.swing.JPanel;

public class PSPPanel extends JPanel {
	
	DailyItemsPanel parentPanel;

	public PSPPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private void jbInit() {
		// TODO Auto-generated method stub
		
	}
}
