package via.sep2.model.time;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilityTest {

    @Test
    void testIsLeapYear() {
        assertTrue(DateUtility.isLeapYear(2000));  // Divisible by 400
        assertFalse(DateUtility.isLeapYear(1900)); // Divisible by 100 but not 400
        assertTrue(DateUtility.isLeapYear(2024));  // Divisible by 4, not 100
        assertFalse(DateUtility.isLeapYear(2023)); // Not divisible by 4
    }

    @Test
    void testDaysInMonthLeapFebruary() {
        Date leapFeb = new DateImplementation(2020, 2, 1); // (year, month, day)
        assertEquals(29, DateUtility.daysInMonth(leapFeb));
    }

    @Test
    void testDaysInMonthNormal() {
        Date april = new DateImplementation(2023, 4, 1);
        assertEquals(30, DateUtility.daysInMonth(april));
    }

    @Test
    void testCompareDates() {
        Date d1 = new DateImplementation(2023, 1, 1);
        Date d2 = new DateImplementation(2023, 1, 2);
        Date d3 = new DateImplementation(2023, 1, 1);
        assertEquals(-1, DateUtility.compareDates(d1, d2));
        assertEquals(0, DateUtility.compareDates(d1, d3));
        assertEquals(1, DateUtility.compareDates(d2, d1));
    }

    @Test
    void testNextDayEndOfMonth() {
        Date date = new DateImplementation(2023, 1, 31);
        Date next = DateUtility.nextDay(date);
        assertEquals(1, next.getDay());
        assertEquals(2, next.getMonth());
        assertEquals(2023, next.getYear());
    }

    @Test
    void testIsWithinRange() {
        Date start = new DateImplementation(2023, 1, 1);
        Date end = new DateImplementation(2023, 1, 31);
        Date mid = new DateImplementation(2023, 1, 15);
        assertTrue(DateUtility.isWithinRange(mid, start, end));
        assertTrue(DateUtility.isWithinRange(start, start, end));
        assertFalse(DateUtility.isWithinRange(new DateImplementation(2023, 2, 1), start, end));
    }

    @Test
    void testParseDate() {
        Date parsed = DateUtility.parseDate("2023-04-09");
        assertEquals(9, parsed.getDay());
        assertEquals(4, parsed.getMonth());
        assertEquals(2023, parsed.getYear());
    }

    @Test
    void testTimeBetween() {
        Date d1 = new DateImplementation(2023, 1, 1);
        Date d2 = new DateImplementation(2023, 1, 2);
        assertEquals(1, DateUtility.timeBetween(d1, d2));
    }

    @Test void isLeapYear()
    {
        assertTrue(DateUtility.isLeapYear(2004));
        assertFalse(DateUtility.isLeapYear(2002));
    }

    @Test void daysInMonth()
    {
        Date date=new DateImplementation(2002,2,13);
        assertEquals(28,DateUtility.daysInMonth(date));
    }

    @Test void today()
    {
        Date today = DateUtility.today();
        java.time.LocalDate now = java.time.LocalDate.now();
        assertEquals(now.getYear(), today.getYear());
        assertEquals(now.getMonthValue(), today.getMonth());
        assertEquals(now.getDayOfMonth(), today.getDay());
    }

    @Test void nextDay()
    {
        Date date=new DateImplementation(2002,2,2);
        Date date1=new DateImplementation(2002,2,3);
        assertEquals(date1,DateUtility.nextDay(date));

        Date date2=new DateImplementation(2002,12,31);
        Date date3=new DateImplementation(2003,1,1);
        assertEquals(date3,DateUtility.nextDay(date2));
    }

    @Test void isWithinRange()
    {
        Date start = new DateImplementation(2024, 5, 1);
        Date end = new DateImplementation(2024, 5, 10);
        Date inside = new DateImplementation(2024, 5, 5);
        Date before = new DateImplementation(2024, 4, 30);
        Date after = new DateImplementation(2024, 5, 11);

        assertTrue(DateUtility.isWithinRange(inside, start, end));
        assertTrue(DateUtility.isWithinRange(start, start, end));
        assertTrue(DateUtility.isWithinRange(end, start, end));
        assertFalse(DateUtility.isWithinRange(before, start, end));
        assertFalse(DateUtility.isWithinRange(after, start, end));
    }

    @Test void compareDates()
    {
        Date d1 = new DateImplementation(2024, 5, 1);
        Date d2 = new DateImplementation(2024, 5, 2);
        Date d3 = new DateImplementation(2024, 5, 1);

        assertEquals(-1, DateUtility.compareDates(d1, d2));
        assertEquals(1, DateUtility.compareDates(d2, d1));
        assertEquals(0, DateUtility.compareDates(d1, d3));
    }

    @Test void parseDate()
    {
        Date expected = new DateImplementation(2024, 5, 9);
        assertEquals(expected, DateUtility.parseDate("2024-05-09"));
    }

    @Test void totalDays()
    {
        Date date = new DateImplementation(2024, 1, 1);
        int days = DateUtility.totalDays(date);
        assertTrue(days > 0);
    }

    @Test void testDaysInMonth()
    {
        assertEquals(31, DateUtility.daysInMonth(1, 2024));
        assertEquals(29, DateUtility.daysInMonth(2, 2024));
        assertEquals(28, DateUtility.daysInMonth(2, 2023));
        assertEquals(30, DateUtility.daysInMonth(4, 2024));
    }

    @Test void timeBetween()
    {
        Date start = new DateImplementation(2024, 5, 1);
        Date end = new DateImplementation(2024, 5, 10);
        assertEquals(9, DateUtility.timeBetween(start, end));
    }

    @Test void toJavaUtilDate()
    {
        DateImplementation custom = new DateImplementation(2024, 5, 9);
        java.util.Date javaDate = DateUtility.toJavaUtilDate(custom);
        Calendar cal = Calendar.getInstance();
        cal.setTime(javaDate);
        assertEquals(2024, cal.get(Calendar.YEAR));
        assertEquals(4, cal.get(Calendar.MONTH));
        assertEquals(9, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test void fromJavaUtilDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.MAY, 9);
        java.util.Date javaDate = cal.getTime();

        DateImplementation converted = DateUtility.fromJavaUtilDate(javaDate);
        assertEquals(2024, converted.getYear());
        assertEquals(5, converted.getMonth());
        assertEquals(9, converted.getDay());
    }
}