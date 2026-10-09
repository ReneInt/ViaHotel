package via.sep2.model.room;

/**
 * @deprecated This interface and implementations have been replaced by {@link dtos}.
 * <p>
 * The class is kept here to show how the project evolved and the progress the team made.
 * </p>
 * @author Group 3
 * @version 1.1
 */
public interface Room {
    /**
     * This method returns the room
     */
    /**
     * This method returns the room number
     */
    public int getRoomNumber();

    /**
     * This method sets the price
     */
    public void setPrice(double price);

    /**
     * This method returns the price
     */
    public double getPrice();
}