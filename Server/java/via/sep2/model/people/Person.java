package via.sep2.model.people;
/**
 * @deprecated This interface and implementations have been replaced by {@link dtos.person.PersonDataDto}.
 * <p>
 * The class is kept here to show how the project evolved and the progress the team made.
 * </p>
 * @author Group 3
 * @version 1.1
 */
public interface Person {
	/**
	 * Returns the first name of a person
	 *
	 * @return first name
	 */
	String getFirstName();
	/**
	 * Sets the first name of a person
	 *
	 * @param firstName is set with the String given
	 */
	void setFirstName(String firstName);
	/**
	 * Returns last name of the person
	 * @return last name
	 */
	String getLastName();
	void setLastName(String lastName);
	String getEmail();
	void setEmail(String email);
	}