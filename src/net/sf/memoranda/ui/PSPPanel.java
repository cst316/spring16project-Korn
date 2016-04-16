/**
 * 
 */
package net.sf.memoranda.ui;

import javax.swing.JPanel;

public class PSPPanel extends JPanel {
	PSPSummaryTable tblPSP;

	public PSPPanel() {
        try {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	private void jbInit() {
		tblPSP = new PSPSummaryTable();
		add(tblPSP);
	}
}
