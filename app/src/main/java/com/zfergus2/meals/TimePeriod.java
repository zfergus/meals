package com.zfergus2.meals;

import java.util.Calendar;

/**
 * Time between two Calendar dates, expressed as weeks, days, hours, minutes,
 * and seconds.
 * @author Zachary Ferguson
 */
public class TimePeriod
{
	public static final String TIME_FORMAT_STRING =
		"%d weeks, %d days, %02dh:%02dm:%02d.%02ds";

	private int milliseconds;
	private int seconds;
	private int minutes;
	private int hours;
	private int days;
	private int weeks;

	/**
	 * Calculates the time between the given dates.
	 * @param startDate Starting date of the period.
	 * @param endDate Ending date of the period.
	 */
	public TimePeriod(Calendar startDate, Calendar endDate)
	{
		long tmp = TimePeriod.getMillisecondsBetween(startDate, endDate);

		this.milliseconds = (int) (tmp % 1000L);
		tmp /= 1000L;
		this.seconds = (int)(tmp % 60L);
		tmp /= 60L;
		this.minutes = (int)(tmp % 60L);
		tmp /= 60L;
		this.hours   = (int)(tmp % 24L);
		tmp /= 24L;
		this.days    = (int)(tmp % 7L);
		tmp /= 7L;
		this.weeks   = (int)(tmp);
	}

	/**
	 * Get the number of milliseconds.
	 * @return Returns this.milliseconds.
	 */
	public int getMilliseconds()
	{
		return this.milliseconds;
	}

	/**
	 * Get the number of seconds.
	 * @return Returns this.seconds.
	 */
	public int getSeconds()
	{
		return this.seconds;
	}

	/**
	 * Get the number of minutes.
	 * @return Returns this.minutes.
	 */
	public int getMinutes()
	{
		return this.minutes;
	}

	/**
	 * Get the number of hours.
	 * @return Returns this.hours.
	 */
	public int getHours()
	{
		return this.hours;
	}

	/**
	 * Get the number of days.
	 * @return Returns this.days.
	 */
	public int getDays()
	{
		return this.days;
	}

	/**
	 * Get the number of weeks.
	 * @return Returns this.weeks.
	 */
	public int getWeeks()
	{
		return this.weeks;
	}

	/**
	 * Gets the number of second between start and end date.
	 * @param startDate The start of the period of time.
	 * @param endDate The end of the period of time.
	 * @return Returns the amount of time, in second, between now and ENDTIME.
	 */
	public static long getMillisecondsBetween(Calendar startDate,
											  Calendar endDate)
	{
		return (endDate.getTimeInMillis() - startDate.getTimeInMillis());
	}

	/**
	 * Uses TIME_FORMAT_STRING to create a formatted string of this time period.
	 * @return Returns a string representation of this time period.
	 */
	@Override
	public String toString()
	{
		return String.format(TimePeriod.TIME_FORMAT_STRING, this.weeks,
			this.days, this.hours, this.minutes, this.seconds,
			this.milliseconds);
	}
}
