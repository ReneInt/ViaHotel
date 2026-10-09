package via.sep2.model.people;

/**
 * @deprecated
 * @see Person
 */
public class Manager extends Employee {
    @Override public String toString()
    {
        return "Manager "+getFirstName()+" "+ getLastName()+" "+ getEmail();
    }

    /**
     * Manager constructor
     * @param firstName String
     * @param lastName String
     * @param email String
     */
    public Manager(String firstName, String lastName, String email)
    {
        super(firstName,lastName,email);
    }
}
