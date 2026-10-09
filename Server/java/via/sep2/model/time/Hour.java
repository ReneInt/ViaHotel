package via.sep2.model.time;

/** @author Bartosz
 * @version 1.0
 * Interface for the Hour Object
 */
public interface Hour {
	/**
	 * This method returns the hour
	 * @return Hour with the time
	 */
	 Hour getHour();

	/**
	 * This method sets the hour sets the hour with 0 minutes
	 * @param hour int for hour
	 */
	 void setHour(int hour);

	/**
	 * This method sets the hour sets the hour with minutes
	 * @param hour int for the hour input
	 * @param minute int for the minute input
	 */
	 void setHour(int hour, int minute);
	 int getMinutes();
	 int getHourAsInt();
}
