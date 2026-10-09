package via.sep2.model.reservation;

import via.sep2.model.time.Date;
import via.sep2.model.people.Customer;
/**
 * @deprecated This interface and implementations have been replaced by {@link dtos.reservation.ReservationDataDto}.
 * <p>
 * The class is kept here to show how the project evolved and the progress the team made.
 * </p>
 * @author Group 3
 * @version 1.1
 */
public interface Reservation {
    int getId();
    void setFinalPrice(double finalPrice);
    double getFinalPrice();
    public void setRoom(int roomNumber);

    public int getRoomNumber();

    public void setCustomer(Customer person);

    public void setCustomer(String first_Name, String last_Name, String email);

    public void setStartDate(Date startDate);

    public void setStartDate(int day, int month, int year);

    public void setEndDate(Date endDate);

    public void setEndDate(int day, int month, int year);

    public Customer getCustomer();
    public Reservation getReservation();
    public double getPriceModifier();
    public void setPriceModifier(double price);
    Date getEndDate();
    Date getStartDate();
}
