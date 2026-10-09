package via.sep2.model.time;

import via.sep2.model.reservation.Dining;

import java.lang.Object;

public class HourImplementation implements Hour {
    private Dining dining;
    private int hours;
    private int minute;
/**
 * setting hours
 * @param hour is int representing hours
 * */
    public HourImplementation(int hour) {
        this.hours = hour;
    }
/**
 * setting hours and minutes
 * @param hour, minute, are ints representing hours and minutes
 * */
    public HourImplementation(int hour, int minute) {
        this.minute = minute;
        this.hours = hour;
    }
/**
 * Checking the minutes if they are putted correct
 * @param minute int representing minutes
 * @throws IllegalArgumentException if the minutes are incorrect
 * */
    private void checkMinuteValidity(int minute) throws IllegalArgumentException {
        if (minute > 59 || minute < 0) {
            throw new IllegalArgumentException("Minute cannot exceed 59 and cannot be lower than 0");
        }
    }
/**
 * Checking the hours if they are putted correct
 * @param hour int representing hours
 * @throws IllegalArgumentException if the hours are incorrect
 * */
    private void checkHourValidity(int hour) {
        if (hour > 23 || hour < 0) {
            throw new IllegalArgumentException("Hours cannot exceed 23 and cannot be lower than 0");
        }
    }
/**
 * Printing time in format hours:minutes
 * */
    public String toString() {
        return String.format( "%02d:%02d" , hours , minute);
    }
/**
 * equals method
 * @param obj Object -Object
 * */
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HourImplementation that = (HourImplementation) obj;
        return hours == that.hours && minute == that.minute;
    }


    /**
     * @see Hour#getHour()
     */
    @Override
    public Hour getHour() {

        return this;
    }

    /**
     * @see Hour#setHour(int)
     */
    @Override
    public void setHour(int hour) {
        checkHourValidity(hour);
        this.hours = hour;
    }


    /**
     * @see Hour#setHour(int, int)
     */
    @Override
    public void setHour(int hour, int minute) {
        checkHourValidity(hour);
        checkMinuteValidity(minute);
        this.hours = hour;
        this.minute = minute;
    }

    @Override public int getHourAsInt()
    {
        return hours;
    }

    @Override public int getMinutes()
    {
        return minute;
    }
}
