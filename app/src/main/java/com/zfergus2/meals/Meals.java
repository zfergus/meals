package com.zfergus2.meals;

import java.util.*;

/**
 * Determines the average amount of money that should be spent every day from
 * now until the end of the Fall 2015 GMU semester. A work in progress, please
 * email me at zfergus2@gmu.edu if you find any bugs. Last updated: 22 September
 * 2015, 05:49:13 EST
 * @author Zachary Ferguson
 * @version 0.1.5
 * @see "http://en.wikipedia.org/wiki/Unix_time"
 */
public class Meals
{
	/**
	 * Static scanner that can be used through out the class to get input from
	 * the stdin.
	 */
	private static Scanner in = new Scanner(System.in);

	/**
	 * Unix Time for the last time to use Freedom Funds for the semester.
	 **/
	//public static final long ENDTIME = 1450587600;
	public static final Calendar END_DATE = new GregorianCalendar(2016, 04, 15);

	/**
	 * Constant array of ints representing the initial balance of the available
	 * meal plans.
	 */
	public static final float[] PLANS = {1900f, 2220f};

	/**
	 * Prompts the user for their initial and current balance and then displays
	 * a message informing the user of the average they should spend per day.
	 * @param args Command-line arguments (Note: Unused).
	 */
	public static void main(String[] args)
	{
		//////////////
		// Get info //
		//////////////
		System.out.printf("Please enter your start balance: ");
		float startBalance = in.nextFloat();
		// Add menu option for rings, 2200, or first, 1900.	
		System.out.printf("Please enter your current balance: ");
		float currentBalance = in.nextFloat();
		System.out.println();

		//////////////////////////
		// Display the message. //
		//////////////////////////
		System.out.print(getMessage(startBalance, currentBalance, END_DATE));
	}

	/**
	 * Creates a formatted String message that informs the user of the time
	 * remaining in the semester and the average amount they should spend.
	 * @param planIndex The index of the initial meal plan balance in the static
	 * array PLANS.
	 * @param currentBalance The current meal plan balance.
	 * @param endDate The last date to spend the current funds.
	 * @return Returns a string containing the amount spent, the time remaining
	 * in the semester, formatted as days, hours, minutes, seconds, and the
	 * average to spend each day.
	 * @throws ArrayIndexOutOfBoundsException Thrown if planIndex does not
	 * correspond to an available initial meal plan value.
	 */
	public static String getMessage(int planIndex, float currentBalance,
									Calendar endDate) throws
		ArrayIndexOutOfBoundsException
	{
		if(planIndex < 0 || planIndex >= PLANS.length)
		{
			throw new ArrayIndexOutOfBoundsException(String.format("No such " +
					"meal plan associated with the index, %d.", planIndex));
		}

		return getMessage(PLANS[planIndex], currentBalance, endDate);
	}

	/**
	 * Creates a formatted String message that informs the user of the time
	 * remaining in the semester and the average amount they should spend.
	 * @param startBalance The initial meal plan balance at the beginning of the
	 * semester.
	 * @param currentBalance The current meal plan balance.
	 * @param endDate The last date to spend the current funds.
	 * @return Returns a string containing the amount spent, the time remaining
	 * in the semester, formatted as days, hours, minutes, seconds, and the
	 * average to spend each day.
	 */
	public static String getMessage(float startBalance, float currentBalance,
									Calendar endDate)
	{
		/////////////////////////////////
		// Calculate time and average. //
		/////////////////////////////////
		/* Get number of second till end of semester. */
		long delta = getTimeTill(endDate);
		/* Determine number of days. */
		int days = (int) (delta / (3600 * 24));
		/* Remaining seconds. */
		int remainder = (int) (delta % (3600 * 24));
		 /* Determine number of hours in the eastern time zone. */
		int hours = ((remainder) / (3600)) - 1;
		remainder %= (3600);
		/* Determine number of mins. */
		int mins = remainder / 60;
		/* Determine number of seconds. */
		int secs = remainder % 60;

		/* Average amount to spend per day including holidays */
		double average = currentBalance / (double) days;

		////////////////////////////////
		// Create the message string. //
		////////////////////////////////
		return String.format(
//				"You started out with $%.2f and have spent $%.2f.\n" +
//				"That is %.2f%% of your start balance.\n" +
//				"There are %d days, %d hours, %d mins, and %d " +
//				"seconds left,\nand you should spend $%.2f on average " +
//				"per day, including holidays.\n",
			"Starting balance: $%.2f\n" +
				"Current balance: $%.2f\n" +
				"\n" +
				"Amount spent: $%.2f\n" +
				"Percentage spent: %.2f%%\n" +
				"\n" +
				"Time remaining in the semester:\n" +
				"\t%d days, %d hours, %d minutes, %d seconds\n" +
				"\n" +
				"Recommended spending average per day, including holidays:\n" +
				"\t$%.2f", startBalance, currentBalance,
			startBalance - currentBalance,
			(startBalance - currentBalance) / startBalance * 100, days, hours,
			mins, secs, (average > 0) ? average : currentBalance);
	}

	/**
	 * getDeltaTime Gets the amount of second between now and ENDTIME in unix
	 * time.
	 * @return Returns the amount of time, in second, between now and ENDTIME.
	 */
	public static long getTimeTill(Calendar endDate)
	{
		Calendar currentTime = new GregorianCalendar(
			new SimpleTimeZone(5, "Eastern"));

		long deltaTime = endDate.getTimeInMillis() -
			currentTime.getTimeInMillis();

		return deltaTime/1000L;
	}
}
