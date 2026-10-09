package via.sep2.model.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateImplementationTest {

    @Test
    public void testValidDateCreation() {
        DateImplementation date = new DateImplementation(2023, 5, 15);
        assertEquals(15, date.getDay());
        assertEquals(5, date.getMonth());
        assertEquals(2023, date.getYear());
        assertEquals("2023-5-15", date.toString());
    }

    @Test
    public void testSetDayInvalidTooHigh() {
        DateImplementation date = new DateImplementation(2023, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> date.setDay(32));
    }

    @Test
    public void testSetDayInvalidZero() {
        DateImplementation date = new DateImplementation(2023, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> date.setDay(0));
    }

    @Test
    public void testSetMonthInvalidTooHigh() {
        DateImplementation date = new DateImplementation(2023, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> date.setMonth(13));
    }

    @Test
    public void testSetMonthInvalidZero() {
        DateImplementation date = new DateImplementation(2023, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> date.setMonth(0));
    }

    @Test
    public void testLeapYearValidFeb29() {
        DateImplementation date = new DateImplementation(2020, 2, 29);
        assertEquals(29, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2020, date.getYear());
    }

    @Test
    public void testNonLeapYearInvalidFeb29() {
        assertThrows(IllegalArgumentException.class, () -> new DateImplementation(2023, 2, 29));
    }

    @Test
    public void testEqualsSameDate() {
        DateImplementation d1 = new DateImplementation(2023, 5, 15);
        DateImplementation d2 = new DateImplementation(2023, 5, 15);
        assertEquals(d1, d2);
    }

    @Test
    public void testEqualsDifferentDate() {
        DateImplementation d1 = new DateImplementation(2023, 5, 15);
        DateImplementation d2 = new DateImplementation(2022, 5, 15);
        assertNotEquals(d1, d2);
    }

    @Test
    public void testEqualsNull() {
        DateImplementation date = new DateImplementation(2023, 5, 15);
        assertNotEquals(null, date);
    }

    @Test
    public void testEqualsDifferentClass() {
        DateImplementation date = new DateImplementation(2023, 5, 15);
        assertNotEquals("15/5/2023", date);
    }
}