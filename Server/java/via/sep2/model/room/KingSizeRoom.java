package via.sep2.model.room;

public class KingSizeRoom implements Room {
    /**
     * All the private methods for the KingSizeRoom class
     */
    private int roomNumber;
    private double price;
    private Room room;

    /**
     * The main constructor for the KingSizeRoom class
     * @param roomNumber
     */
    public KingSizeRoom (int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @see Room#getRoomNumber()
     */
    public int getRoomNumber() {
        return roomNumber;
    };


    public void setPrice(double price) {
        this.price = price;
    };

    /**
     * @see Room#getPrice()
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns all the room's information in a nice looking String
     * @return String
     */
    public String toString() {
        return "Room number: " + roomNumber + "\nPrice: " + price + "\nRoom: " + room;
    }

    /**
     * A method that compares two rooms
     * @param obj
     * @return boolean
     */
    public boolean equals (Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        KingSizeRoom other = (KingSizeRoom) obj;

        return roomNumber == other.roomNumber && price == other.price && room == other.room;
    }
}
