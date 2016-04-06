package net.sf.memoranda.test;

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
        assertTrue(cd1.equals(cd3));
        assertFalse(cd1.equals(cd2));
    }

    @Test
    public void beforeTest() {
        assertTrue(cd1.before(cd2));
        assertFalse(cd2.before(cd1));
    }

    @Test
    public void afterTest() {
        assertFalse(cd1.after(cd2));
        assertTrue(cd2.after(cd1));
    }

}
