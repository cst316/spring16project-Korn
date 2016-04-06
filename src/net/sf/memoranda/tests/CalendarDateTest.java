package net.sf.memoranda.tests;

import net.sf.memoranda.date.CalendarDate;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalendarDateTest {

    private CalendarDate cd1;
    private CalendarDate cd2;
    private CalendarDate cd3;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        cd1 = new CalendarDate(10, 2, 2016);
        cd2 = new CalendarDate(11, 2, 2016);
        cd3 = new CalendarDate(10, 2, 2016);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void equalsTest() {
        Boolean t = cd1.equals(cd3);
        System.out.println("[DEBUG] Test Equals true: " + t.toString());
        assertTrue(t);
        Boolean f = cd1.equals(cd2);
        System.out.println("[DEBUG] Test Equals false: " + f.toString());
        assertFalse(f);
    }

    @Test
    public void beforeTest() {
        Boolean t = cd1.before(cd2);
        System.out.println("[DEBUG] Test Before true: " + t.toString());
        assertTrue(t);
        Boolean f = cd2.before(cd1);
        System.out.println("[DEBUG] Test Before false: " + f.toString());
        assertFalse(f);
    }

    @Test
    public void afterTest() {
        Boolean t = cd2.after(cd1);
        System.out.println("[DEBUG] Test After true: " + t.toString());
        assertTrue(t);
        Boolean f = cd1.after(cd2);
        System.out.println("[DEBUG] Test After false: " + f.toString());
        assertFalse(f);
    }

}
