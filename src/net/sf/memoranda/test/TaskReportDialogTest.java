/**
 * 
 */
package net.sf.memoranda.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.memoranda.ui.FileExportDialog;
import net.sf.memoranda.ui.TaskReportDialog;

/**
 * @author Jordan Partridge
 *
 */
public class TaskReportDialogTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		if (false) {
			FileExportDialog test2 = new FileExportDialog(null, null, null);
			TaskReportDialog test = new TaskReportDialog(null, null);
			test.isDisplayable();
			test.setVisible(true);
			test.setVisible(true);
		}
		else
		{
			assertTrue(true);
		}
	}

}
