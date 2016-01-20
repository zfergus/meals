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
	public static class MealsData
	{
		public float startBalance, currentBalance;
		public String timeRemaining;
		public double dailyAverage;

		public MealsData(float startBalance, float currentBalance,
						 String timeRemaining, double dailyAverage)
		{
			this.startBalance = startBalance;
			this.currentBalance = currentBalance;
			this.timeRemaining = timeRemaining;
			this.dailyAverage = dailyAverage;
		}
	}

	/**
	 * Static scanner that can be used through out the class to get input from
	 * the stdin.
	 */
	private static Scanner in = new Scanner(System.in);

	/**
	 * Unix Time for the last time to use Freedom Funds for the semester.
	 **/
	public static final Calendar END_DATE = new GregorianCalendar(2016, 04, 15);
	public static final TimeZone EASTERN_TIMEZONE =
		new SimpleTimeZone(-5 * 60 * 60 * 1000, "America/New_York",
		Calendar.MARCH, 8, -Calendar.SUNDAY,
		2 * 60 * 60 * 1000,
		Calendar.NOVEMBER, 1, -Calendar.SUNDAY,
		2 * 60 * 60 * 1000,
		1 * 60 * 60 * 1000);

	/**
	 * Constant array of ints representing the initial balance of the available
	 * meal plans.
	 */
	public static final float[] PLANS = {1900f, 2200f};

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
	 * Creates a MealData object with the given values.
	 * @param startBalance Starting balance to spend.
	 * @param currentBalance Remaining balance to spend.
	 * @param endDate Last day to spend funds.
	 * @return Returns a MealData created.
	 */
	public static MealsData createMealsData(float startBalance,
		float currentBalance, Calendar endDate)
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
		int hours = ((remainder) / (3600));
		remainder %= (3600);
		/* Determine number of mins. */
		int mins = remainder / 60;
		/* Determine number of seconds. */
		int secs = remainder % 60;

		/* Average amount to spend per day including holidays */
		double average = currentBalance / (double) days;

		/* Construct a MealData. */
		return new MealsData(startBalance, currentBalance,
			String.format("%d days, %02dh:%02dm:%02ds", days, hours, mins,
			secs), average);
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
	@Deprecated
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
		/* Get the MealData */
		MealsData data = createMealsData(startBalance, currentBalance, endDate);

		Calendar today = new GregorianCalendar(EASTERN_TIMEZONE);
		double weeklyBalance =
			data.dailyAverage * (today.getActualMaximum(Calendar.DAY_OF_WEEK) -
				today.get(Calendar.DAY_OF_WEEK) + 1);
		double monthlyBalance =
			data.dailyAverage * (today.getActualMaximum(Calendar.DAY_OF_MONTH) -
				today.get(Calendar.DAY_OF_MONTH) + 1);

		////////////////////////////////
		// Create the message string. //
		////////////////////////////////
		return String.format(
			"Starting balance: $%.2f\n" +
			"Current balance: $%.2f\n" +
			"\n" +
			"Amount spent: $%.2f\n" +
			"Percentage spent: %.2f%%\n" +
			"\n" +
			"Time remaining in the semester:\n" +
			"\t%s\n" +
			"\n" +
			"Recommended spending average per day, including holidays:\n" +
			"\t$%.2f\n" +
			"Amount left to spend this week:\n" +
			"\t$%.2f\n" +
			"Amount left to spend this month:\n" +
			"\t$%.2f",
			startBalance, currentBalance, startBalance - currentBalance,
			(startBalance - currentBalance) / startBalance * 100,
			data.timeRemaining,
			(data.dailyAverage > 0) ? data.dailyAverage : currentBalance,
			weeklyBalance, monthlyBalance);
	}

	/**
	 * getDeltaTime Gets the amount of second between now and ENDTIME in unix
	 * time.
	 * @return Returns the amount of time, in second, between now and ENDTIME.
	 */
	public static long getTimeTill(Calendar endDate)
	{
		Calendar currentTime = new GregorianCalendar(EASTERN_TIMEZONE);

		long deltaTime = endDate.getTimeInMillis() -
			currentTime.getTimeInMillis();

		return deltaTime/1000L;
	}
}
