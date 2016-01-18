package com.zfergus2.meals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Activity for displaying a calender and getting a date.
 */
public class CalenderActivity extends AppCompatActivity
	implements CalendarView.OnDateChangeListener
{
	public final static String EXTRA_END_DATE_DAY   = "com.zfergus2.meals.END_DATE_DAY";
	public final static String EXTRA_END_DATE_MONTH = "com.zfergus2.meals.END_DATE_MONTH";
	public final static String EXTRA_END_DATE_YEAR  = "com.zfergus2.meals.END_DATE_YEAR";

	private CalendarView calendarView;
	private Calendar selectedDate;

	private TextView formattedDate;

	/**
	 * Initialize the fields.
	 * @param savedInstanceState Not Used
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Intent intent = getIntent();

		this.selectedDate = new GregorianCalendar(
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_YEAR,
				Meals.END_DATE.get(Calendar.YEAR)),
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_MONTH,
				Meals.END_DATE.get(Calendar.MONTH)),
			intent.getIntExtra(MainActivity.EXTRA_END_DATE_DAY,
				Meals.END_DATE.get(Calendar.DAY_OF_MONTH))
		);

		this.calendarView = (CalendarView)this.findViewById(R.id.calendarView);
		this.calendarView.setOnDateChangeListener(this);

		this.formattedDate = (TextView)this.findViewById(R.id.date_text);
		this.formattedDate.setText(createFormattedDate(this.selectedDate));
	}

	@Override
	protected void onStart ()
	{
		super.onStart();

		this.calendarView.setDate(this.selectedDate.getTimeInMillis());
	}

	/**
	 * Create the option menu to reset the date.
	 * @param menu The menu to add to.
	 * @return Returns true.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	/**
	 * Handel resetting the date to the default one defined in meals.
	 * @param item The menu item selected.
	 * @return Returns if handled.
	 */
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
			this.selectedDate = Meals.END_DATE;
			this.calendarView.setDate(this.selectedDate.getTimeInMillis());
			this.formattedDate.setText(createFormattedDate(this.selectedDate));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Send a resulting intent when the back button is pressed.
	 */
	@Override
	public void onBackPressed()
	{
		Intent resultIntent = new Intent();

		resultIntent.putExtra(EXTRA_END_DATE_DAY,
			this.selectedDate.get(Calendar.DAY_OF_MONTH));
		resultIntent.putExtra(EXTRA_END_DATE_MONTH,
			this.selectedDate.get(Calendar.MONTH));
		resultIntent.putExtra(EXTRA_END_DATE_YEAR,
			this.selectedDate.get(Calendar.YEAR));

		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	/**
	 * Created a date formated string.
	 * Format: DAY_OF_WEEK, DAY_OF_MONTH MONTH_NAME, YEAR
	 * @param cal Date to format.
	 * @return Returns a string of the formatted date.
	 */
	public static String createFormattedDate(Calendar cal)
	{
		return String.format(
			"%s, %d %s, %d",
			cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US),
			cal.get(Calendar.DAY_OF_MONTH),
			cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US),
			cal.get(Calendar.YEAR)
		);
	}

	/**
	 * Set the selected date and update the formatted date.
	 * @param view The calendar.
	 * @param year The selected year.
	 * @param month The selected month. (Zero based)
	 * @param dayOfMonth The selected day of month.
	 */
	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
									int dayOfMonth)
	{
		this.selectedDate = new GregorianCalendar(year, month, dayOfMonth);
		this.formattedDate.setText(createFormattedDate(this.selectedDate));
	}
}
