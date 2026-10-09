package via.sep2.model.reservation;

import via.sep2.model.people.Customer;
import via.sep2.model.time.Date;
import java.lang.Object;
import via.sep2.model.time.DateImplementation;

/**
 * @deprecated
 * @see Reservation
 */
public class All_Inclusive implements Reservation
{
  private int id;
  private Customer person;
  private Date startDate;
  private Date endDate;
  private int roomNumber;
  private double priceModifier;
  private double finalPrice;



  public All_Inclusive(int id, Customer person, Date startDate, Date endDate, int roomNumber)
  {
		this.id=id;
    this.person = person;
    this.startDate = startDate;
    this.endDate = endDate;
    this.roomNumber = roomNumber;
  }

  public All_Inclusive(int id,String firstName, String lastName, String email, Date startDate, Date endDate, int roomNumber)
  {
		this.id=id;
    this.person = new Customer(firstName, lastName, email);
    this.startDate = startDate;
    this.endDate = endDate;
    this.roomNumber = roomNumber;
  }

  public All_Inclusive(int id,String firstName, String lastName, String email, int startDateDay, int startDateMonth,
      int startDateYear, int endDateDay, int endDateMonth, int endDateYear,
      int roomNumber)
  {
		this.id=id;
    this.person = new Customer(firstName, lastName, email);
    this.startDate = new DateImplementation(startDateYear, startDateMonth,
        startDateDay);
    this.endDate = new DateImplementation(endDateDay, endDateMonth,
        endDateYear);
    this.roomNumber = roomNumber;
  }

  @Override public double getFinalPrice()
  {
    return finalPrice;
  }

  public void setFinalPrice(double finalPrice)
  {
    this.finalPrice = finalPrice;
  }

  @Override public String toString()
  {
    return "Reservation for a simple room:"
        + "\nThe customer inside the room is : " + person
        + "\nReservation start date : " + startDate
        + "\nReservation end date : " + endDate + "\nIn room: "
        + getRoomNumber() +"\nFinal price: "+ finalPrice;
  }



  public void setRoom(int roomNumber)
  {
    this.roomNumber = roomNumber;
  }

  /**
   * @see Reservation#getRoomNumber()
   */
  public int getRoomNumber()
  {
    return roomNumber;
  }

  /**
   * @see Reservation#setCustomer(Customer)
   */
  public void setCustomer(Customer person)
  {
    this.person = person;
  }

  /**
   * @param first_Name
   * @param last_Name
   * @param email
   */
  @Override public void setCustomer(String first_Name, String last_Name,
      String email)
  {
    this.person = new Customer(first_Name, last_Name, email);
  }

  /**
   * @see Reservation#setStartDate(Date)
   */
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * @param day
   * @param month
   * @param year
   */
  @Override public void setStartDate(int day, int month, int year)
  {
    this.startDate = new DateImplementation(day, month, year);
  }

  /**
   * @see Reservation#setEndDate(Date)
   */
  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  /**
   * @param day
   * @param month
   * @param year
   */
  @Override public void setEndDate(int day, int month, int year)
  {
    this.endDate = new DateImplementation(day, month, year);
  }

  /**
   * @see Reservation#getCustomer()
   */
  public Customer getCustomer()
  {
    return person;
  }

  /**
   * @return
   */
  @Override public Reservation getReservation()
  {
    return this;
  }

  @Override public int getId()
  {
    return id;
  }

  /**
   * @return
   */
  @Override public double getPriceModifier()
  {
    return priceModifier;
  }

  /**
   *
   */
  @Override public void setPriceModifier(double priceModifier)
  {
    this.priceModifier = priceModifier;
  }

  @Override public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @return
   */
  @Override public Date getStartDate()
  {
    return startDate;
  }
  @Override public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    All_Inclusive that = (All_Inclusive) obj;
    return id == that.id && person.equals(that.person) && startDate.equals(that.startDate)
        &&endDate.equals(that.endDate) && roomNumber==that.roomNumber;
  }
}
