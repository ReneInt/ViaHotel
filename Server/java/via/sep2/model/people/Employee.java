package via.sep2.model.people;

/**
 * @deprecated
 * @see Person
 */
public class Employee implements Person {
	private String firstName;
	private String lastName;
	private String email;

	@Override public String toString()
	{
		return "Employee "+ firstName + ' '+ lastName + ' '+ email;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Constructor for employee
	 * @param firstName String
	 * @param lastName String
	 * @param email String
	 */
	public Employee(String firstName, String lastName, String email) {
		setFirstName(firstName);
		setEmail(email);
		setLastName(lastName);
	}
}
