package com.zfergus2.meals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DisplayMealsActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);

        /* Get the message from the intent. */
		Intent intent = getIntent();
		float startingBalance =
				intent.getFloatExtra(MainActivity.EXTRA_STARTING_BALANCE, 0f);
		float currentBalance =
				intent.getFloatExtra(MainActivity.EXTRA_CURRENT_BALANCE, 0f);

		Calendar cal = new GregorianCalendar(
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_YEAR,
				Meals.END_DATE.get(Calendar.YEAR)),
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_MONTH,
				Meals.END_DATE.get(Calendar.MONTH)),
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_DAY,
				Meals.END_DATE.get(Calendar.DAY_OF_MONTH))
		);
		cal.setTimeZone(Meals.EASTERN_TIMEZONE);

        /* Calculate the average and create a message. */
		Meals.MealsData data =
			Meals.createMealsData(startingBalance, currentBalance, cal);

        /* Create the text view. */
		this.setFormattedText(R.id.start_balance_val, data.startBalance);
		this.setFormattedText(R.id.current_balance_val, data.currentBalance);

		this.setFormattedText(R.id.amount_spent_val,
			data.startBalance - data.currentBalance);
		this.setFormattedText(R.id.percent_spent_val,
			(data.startBalance - data.currentBalance) / data.startBalance * 100);

		((TextView)this.findViewById(R.id.time_remaining_val)).
			setText(data.timeRemaining);

		this.setFormattedText(R.id.daily_avg_val, data.dailyAverage);
		Calendar today = new GregorianCalendar(Meals.EASTERN_TIMEZONE);
		double currentWeeklyBalance = Math.min(data.currentBalance,
			data.dailyAverage * (today.getActualMaximum(Calendar.DAY_OF_WEEK) -
				today.get(Calendar.DAY_OF_WEEK) + 1));
		this.setFormattedText(R.id.weekly_avg_val, currentWeeklyBalance);
		double currentMonthlyBalance = Math.min(data.currentBalance,
			data.dailyAverage * (today.getActualMaximum(Calendar.DAY_OF_MONTH) -
				today.get(Calendar.DAY_OF_MONTH) + 1));
		this.setFormattedText(R.id.monthly_avg_val, currentMonthlyBalance);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.reset_date_action)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set the text of a TextView with id textID to the formated version of its
	 * current text and the given arguments.
	 * @param textID ID of the TextView to find.
	 * @param args Arguments for the TextView's formatting.
	 * @param <T> Type of argument to be given.
	 */
	private <T> void setFormattedText(int textID, T... args)
	{
		TextView text = (TextView)this.findViewById(textID);
		text.setText(String.format(text.getText().toString(), args));
	}
}
