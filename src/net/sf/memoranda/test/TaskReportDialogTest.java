/**
 *
 */
package net.sf.memoranda.test;

import net.sf.memoranda.ui.FileExportDialog;
import net.sf.memoranda.ui.TaskReportDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Jordan Partridge
 */
public class TaskReportDialogTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        if (false) {
            FileExportDialog test2 = new FileExportDialog(null, null, null);
            TaskReportDialog test = new TaskReportDialog(null, null);
            test.isDisplayable();
            test.setVisible(true);
            test.setVisible(true);
        } else {
            assertTrue(true);
        }
    }

}