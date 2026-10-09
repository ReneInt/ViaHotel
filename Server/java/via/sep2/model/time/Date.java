package via.sep2.model.time;

/**@author Mario,Rodrigo,Bartosz,Tomasz,Rene
 * @version 1.0
 * Interface for a Date object
 */
public interface Date {
	/**
	 * Sets the day of the date
	 * @param day int - represents the day of the date
	 */
	public abstract void setDay(int day);

	/**
	 * Sets the month of the date
	 * @param month - represents the month of the date
	 */
	public abstract void setMonth(int month);

	/**
	 * Sets the year of the date
	 * @param year - represents the year of the date
	 */
	public abstract void setYear(int year);

	/**
	 * Returns the day of a date
	 * @return int with the day
	 */
	public abstract int getDay();

	/**
	 * Returns the month of a date
	 * @return int with the month
	 */
	public abstract int getMonth();

	/**
	 * Returns the year of a date
	 * @return int with the year
	 */
	public abstract int getYear();

}
