package via.sep2.model.people;

/**
 * @deprecated
 * @author Group 3
 * @version 1.1
 * @see Person
 */
public class Customer implements Person
{
	private String firstName;

	/**
	 * {@code ToString} for printing an Object from the class
	 *
	 * @return String of text
	 */
	@Override public String toString()
	{
		return "Customer " + firstName + " " + lastName + " " + email;
	}

	private String lastName;
	private String email;

	//Learning javadocs and using fun stuff-Mario

	/**
	 * {@inheritDoc}
	 *
	 * @see Person#getFirstName()
	 */
	@Override public String getFirstName()
	{
		return firstName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param firstName
	 * @see Person#setFirstName(String)
	 */
	@Override public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see Person#getLastName()
	 */
	@Override public String getLastName()
	{
		return lastName;
	}

	@Override public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@Override public String getEmail()
	{
		return email;
	}

	@Override public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * Customer constructor
	 *
	 * @param firstName String
	 * @param lastName  String
	 * @param email     String
	 */
	public Customer(String firstName, String lastName, String email)
	{
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}
}
