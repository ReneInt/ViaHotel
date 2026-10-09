package via.sep2.model.reservation;

import via.sep2.model.people.Person;
import via.sep2.model.time.Date;
import via.sep2.model.time.Hour;
import via.sep2.model.time.HourImplementation;

import java.lang.Object;

/**
 * @deprecated
 * @see Dining
 */
public class DiningImplementation implements Dining {
    private int id;
    private Date date;
    private Hour hour;
    private Person person;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     *Constructor for a date object that takes 3 objects in order: Date, Hour and Customer
     * @param date Date - marks of which date reservation is made
     * @param hour Hour - marks of which hour reservation is made
     * @param person Customer - marks which Customer made the reservation
     */
    public DiningImplementation(int id,Date date, Hour hour, Person person) {
        this.id=id;
        this.date = date;
        this.hour = hour;
        this.person = person;
    }

    /**
     * Compares 2 Dining objects if they are the same the result it's true, if they are different it returns false
     * @param obj Object
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        DiningImplementation temp = (DiningImplementation) obj;
        return date.equals(temp.date) && hour.equals(temp.hour) && person.equals(temp.person);
    }

    /**
     * This method returns a String of the object with a format that it is easy to read
     * @return String with a nice formating
     */
    public String toString() {
        return "\nThe reservation of customer: " + person + "\nOn date: " + date + "\nOn hour: " + hour;
    }


    /**
     * @see Dining#getDiningTime()
     */
    public Hour getDiningTime() {
        return hour;
    }

    /**
     * setting DiningTime
     * @param hour is Hour object representing for what time reservation is made
     * */
    public void setDiningTime(Hour hour) {
        this.hour = hour;
    }
    /**
     * @see Dining#setDiningTime(int)
     */
    public void setDiningTime(int hour) {
        this.hour = new HourImplementation(hour);
    }

    /**
     * @see Dining#setDiningTime(int, int)
     */
    public void setDiningTime(int hour, int minutes) {
        this.hour = new HourImplementation(hour, minutes);
    }

    /**
     * @see Dining#getDiningDate()
     */
    public Date getDiningDate() {
        return date;
    }


    /**
     * @see Dining#setDiningDate(Date)
     */
    public void setDiningDate(Date date) {
        this.date=date;
    }


    public void setCustomer(Person person) {
        this.person=person;
    }


    /**
     * @see Dining#getCustomer()
     */
    public Person getCustomer() {
        return person;
    }

}
