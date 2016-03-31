package net.sf.memoranda.test;

import net.sf.memoranda.Start;
import net.sf.memoranda.ui.App;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.WindowEvent;

import static org.junit.Assert.assertEquals;

public class TrayTest {

    @Before
    public void setUp() throws Exception {
        // Formally start the software
        Start.main(new String[]{""});

        // Wait until the frame is actually active before proceeding
        // on any tests
        while (!(App.getFrame().isActive()))
            Thread.sleep(100);
    }

    @Test
    public void testAddTray() {
        // Starting off, there should be no tray
        assertEquals(App.getFrame().trayActive(), false);

        // Minimized, there should also be no tray icon
        App.getFrame().doMinimize();
        assertEquals(App.getFrame().trayActive(), false);

        // Restore state
        App.openWindow();

        // Close the frame by simulating exiting (pressing the X or alt-f4)
        // in a way that still will hook onto event listeners
        WindowEvent wev = new WindowEvent(App.getFrame(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

        // There will be a delay before the event finishes;
        // sleep for a small amount of time to account for this
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // There should now be a tray.
        assertEquals(App.getFrame().trayActive(), true);
    }
}