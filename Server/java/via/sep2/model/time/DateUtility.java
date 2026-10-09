package via.sep2.model.time;

import dtos.time.DateDto;

import java.util.Calendar;

public class DateUtility {
    /**
     * A static method that checks if the year is a leap year by taking the year
     *
     * @param year int
     * @return boolean
     */
    public static boolean isLeapYear(int year) {
        //Function that checks if the year is a leap year
        //Checks if the year is divisible by 4
        if (year % 4 == 0) {
            //Checks if the year is divisible by 100 but not by 400
            if (year % 100 == 0 && year % 400 != 0) {
                //In this case it returns false
                return false;
                //We check if it's divisible by 400
            } else if (year % 100 == 0 && year % 400 == 0) {
                //If the condition is met the program returns true
                return true;
            } else {
                //Otherwise the year is divisible by 4 in which case it returns true;
                return true;
            }
        } else
            //If it's not divisible by 4 it returns false from the start
            return false;
    }

    /**
     * This function returns the number of days in the month of a specific date;
     * @param date Date is taken as a parameter
     * @return int with the days in month value
     * @throws IllegalArgumentException thrown if the month does not exist
     */
    public static int daysInMonth(Date date) throws IllegalArgumentException
    {
        //Check if the year is leap and the month is February
        if(isLeapYear(date.getYear())&& date.getMonth()==2)
        {
            return 29;
        }
        //return the value based on the month of the year
        switch (date.getMonth()) {
            case 1,3,5,7,8,10,12:
                return 31;
            case 4,6,9,11:
                return 30;
            case 2:
                return 28;
                //if the month is not a month it will throw and exception
            default: {
                throw new IllegalArgumentException("Not a month");
            }
        }
    }
    /**
     * Returns today as a Date object
     *
     * @return Date
     */
    public static Date today() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return new DateImplementation(now.getYear(), now.getMonthValue(),now.getDayOfMonth());
    }

    /**
     * Changes the given date to the next day in the calendar
     *
     * @param date Date
     * @return Date
     */
    public static Date nextDay(Date date) {
        //Updates the date to become the next day
        int day = date.getDay();//This takes 1
        int month=date.getMonth();
        int year=date.getYear();
        day++;
//checks if next day goes into the next month
        if (daysInMonth(date) < day) {//This comparison runs once
            month++;
            day = 1;
            //checks if the next month goes into the next year
            if (month > 12) {
                year++;
                month = 1;
            }
        }
        //updates the day.
        return new DateImplementation(year,month,day);
    }

    /**
     * Checks if a specific date is between 2 dates.
     *
     * @param date      Date
     * @param startDate Date
     * @param endDate   Date
     * @return boolean
     */
    public static boolean isWithinRange(Date date, Date startDate, Date endDate) {
        //Checks if a date is withinRange of other dates
        return (compareDates(date, startDate) >= 0 && compareDates(date, endDate) <= 0) ||
                (compareDates(date, startDate) == 0) ||
                (compareDates(date, endDate) == 0);
        //This has O(1) time complexity;
    }

    /**
     * Compares 2 Date objects to see which one of them is on a later date, used for isWithinRangeMethod
     *
     * @param date1 Date
     * @param date2 Date
     * @return int
     */
    public static int compareDates(Date date1, Date date2) {
        //Compares 2 dates
        int day1=date1.getDay();
        int day2=date2.getDay();
        int month1=date1.getMonth();
        int month2=date2.getMonth();
        int year1=date1.getYear();
        int year2=date1.getYear();
        if (year1 < year2) {//This takes 2 and the comparison runs once
            //if the first date year is before the second date year it returns -1
            //because of return it will exit if any of this condition apply
            return -1;//this takes 1
        } else if (year1 > year2) {//This takes 2 and the comparison runs once
            //if the first date year is after the second date year it returns 1
            return 1;//this takes 1
        }

        if (month1 < month2) {//This takes 2 and the comparison runs once
            //if the first date month is before the second date month it returns -1
            return -1;//this takes 1
        } else if (month1 > month2) {//This takes 2 and the comparison runs once
            //if the first date month is after the second date month it returns 1
            return 1;//this takes 1
        }

        if (day1 < day2) {//This takes 2 and the comparison runs once
            //if the first date day is before the second date day it returns -1
            return -1;//this takes 1
        } else if (day1 > day2) {//This takes 2 and the comparison runs once
            return 1;//this takes 1
        }
        //if the dates are on the same date, it returns 0 to show that they are the same date
        return 0;//this takes 1
        //This has O(1) time complexity;
    }

    /**
     * Takes a date in a String form (for example from a date picker) and change it into a new Date object
     *
     * @param date String in a YYYY-MM-DD format
     *
     * @return new Date
     */
    public static Date parseDate(String date)
    {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        if (date.charAt(5) == '0') {
            month = month % 10;
        }
        if (date.charAt(8) == '0') {
            day = day % 10;
        }
        return new DateImplementation(year,month,day);
    }
    public static int totalDays(Date date) {
        //totalDays is a local variable that counts the total amount of days
        int totalDays = 0;//This takes 1
        //The counting starts from 0
        int year=date.getYear();
        int n = 0;//This takes 1
        // For a less time constrain if the year is higher than 2023 it will start counting from 2023
        if (year > 2023) {//This comparison is run once
            n = 2023;//This takes 1
        }
        //Loop to find all the days that are present between years
        for (int y = n; y < year; y++) {//This takes 4n
            //We check if the year is leap.
            if (isLeapYear(year)) {//This comparison runs n times
                totalDays += 366;//This takes 1
            } else {
                totalDays += 365;//This takes 1
            }
        }
        int month=date.getMonth();
        //Loop to find all the days in the months of the date
        for (int m = 1; m < month; m++) {//This takes  4n
            totalDays += daysInMonth(m,year);//this takes 1
        }

        totalDays += date.getDay();//This takes 1

        return totalDays;//This takes 1
        //This has O(n) time complexity because we ignore constants
    }
    public static int daysInMonth(int month, int year) {
        //This checks the month number
        if (month == 1 || month == 3 || month == 5 || month == 7//This comparison runs once and takes 7
                || month == 8 || month == 10 || month == 12)
            //It returns 31
            return 31;//This takes 1
            //This checks if the month is february
        else if (month == 2) {//This comparison runs once
            //It checks if the year is a leap year
            if (isLeapYear(year)) {//This comparison runs once
                //If it is a leap year it returns 29
                return 29;//This takes 1
            } else
                //Otherwise it returns 28
                return 28;//This takes 1
        }
        //This checks the month number
        else if (month == 4 || month == 6 || month == 9//This comparison runs once
                || month == 11)
            return 30;//This takes 1
        else
            return 0;//This takes 1
    }

    /**
     * Handles the number of days between two days
     * @param startDate as start date
     * @param endDate as end date
     * @return number of days between
     */
    public static int timeBetween(Date startDate, Date endDate) {
        //The total amount of days between 2 dates
        return totalDays(endDate) - totalDays(startDate);//This takes 1
        //This has O(1) time complexity
    }

    /**
     * Transforms custom DateImplementation into {@link java.util.Date}
     * @param customDate Date
     * @return {@code java.util.Date}
     */
    public static java.util.Date toJavaUtilDate(DateImplementation customDate) {
        Calendar calendar = Calendar.getInstance();
        //using the javaCalendar to parse our date into java util date
        calendar.set(Calendar.YEAR, customDate.getYear());
        calendar.set(Calendar.MONTH, customDate.getMonth() - 1); // Month is 0-based in Calendar
        calendar.set(Calendar.DAY_OF_MONTH, customDate.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**
     * Transforms {@link java.util.Date} into custom DateImplementation
     * @param  date java.util.Date
     * @return {@link DateImplementation}
     */
    public static DateImplementation fromJavaUtilDate(java.util.Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        //using calendar to parse from java util to custom date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DateImplementation(year, month, day);
    }
    public static DateDto fromJavaUtilDateDto(java.util.Date date)
    {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        //using calendar to parse from java util to custom date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DateDto(year, month, day);
    }
    public static DateDto fromDateToDto(Date date)
    {
        return new DateDto(date.getYear(),date.getMonth(),date.getDay());
    }

}
