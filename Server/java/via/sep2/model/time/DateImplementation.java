package via.sep2.model.time;

import java.lang.Object;

/**@author Mario, Rodrigo
 * @version 1.0
 * Implementation of the Date interface
 */
public class DateImplementation implements Date {
    //Creating private variables
    private int day;
    private int month;
    private int year;

    /**
     *Constructor for a date object that takes 3 integers in order: year, month ,day
     * @param year int -resents the year of the date
     * @param month int -resents the month of the date
     * @param day int -resents the day of the date
     */
    public DateImplementation(int year, int month, int day){
        //Using the methods to set the proper date format
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * This method checks the validity of a day
     * @param day int representing the day
     * @throws IllegalArgumentException if the day input is invalid
     */
    private void checkDayValidity(int day) throws IllegalArgumentException {
        //check if the month is valid
        checkMonthValidity(this.month);
        //check if the day is <=0 or if it exceeds the number of days in the month
        if (0 >= day || day > DateUtility.daysInMonth(this)) {
            //throw an exception if the date is invalid
            throw new IllegalArgumentException("Invalid input in field day");
        }
    }

    /**
     * This method checks the validity of a month
     * @param month int representing the month of a date
     * @throws IllegalArgumentException if the month input is invalid
     */
    private void checkMonthValidity(int month) throws IllegalArgumentException {
        if (0 >= month || month > 12) {
            throw new IllegalArgumentException("Invalid input in field month");
        }
    }


    /**
     * @see Date#setDay(int)
     */
    @Override public void setDay(int day) {
        checkDayValidity(day);
        this.day=day;
    }


    /**
     * @see Date#setMonth(int)
     */
    @Override public void setMonth(int month) {
        checkMonthValidity(month);
            this.month=month;
    }


    /**
     * @see Date#setYear(int)
     */
    @Override public void setYear(int year) {
        this.year=year;
    }


    /**
     * @see Date#getDay()
     */
    @Override public int getDay() {
        return day;
    }


    /**
     * @see Date#getMonth()
     */
    @Override public int getMonth() {
        return month;
    }


    /**
     * @see Date#getYear()
     */
    @Override public int getYear() {
        return year;
    }

    /**
     * This method returns a String of the object with a format that it is easy to read
     * @return String with a nice formating
     */
    @Override  public String toString() {
        //Writes it in a nice format
        return ""+year+'-'+month+'-'+day;
    }


    /**
     * Compares 2 date objects if they are the same the result it's true, if they are different it returns false
     * @param obj Object
     * @return boolean
     */
    @Override public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass())
            return false;
        DateImplementation date = (DateImplementation) obj;
        return day == date.day && month == date.month && year == date.year;
    }
}
