package com.zfergus2.meals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Main activity for Meals. Gets user input for the balance and end date. Sends
 * this data to a DisplayMealsActivity.
 */
public class MainActivity extends AppCompatActivity
{
	public final static String EXTRA_STARTING_BALANCE    = "com.zfergus2.meals.STARTING";
	public final static String EXTRA_CURRENT_BALANCE = "com.zfergus2.meals.REMAINING";
	public final static String EXTRA_END_DATE_DAY    = "com.zfergus2.meals.END_DATE_DAY";
	public final static String EXTRA_END_DATE_MONTH  = "com.zfergus2.meals.END_DATE_MONTH";
	public final static String EXTRA_END_DATE_YEAR   = "com.zfergus2.meals.END_DATE_YEAR";

	private final static int REQUEST_CALENDAR = 0xCAFE;

	private final static String PREF_END_DATE_DAY = "END_DATE_DAY";
	private final static String PREF_END_DATE_MONTH = "END_DATE_MONTH";
	private final static String PREF_END_DATE_YEAR = "END_DATE_YEAR";
	public final static String PREF_SELECTED_PLAN = "SELECTED_PLAN";
	public final static String PREF_STARTING_BALANCE = "STARTING_BALANCE";

	private boolean isPlanSelected;
	private Calendar endDate;

	private Spinner planSpinner;
	private EditText startingBalance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.planSpinner = (Spinner) findViewById(R.id.plan_spinner);
		/* Create an ArrayAdapter using the string array and a default */
		/* spinner layout.                                             */
		ArrayAdapter<CharSequence> adapter =
				ArrayAdapter.createFromResource(this,
						R.array.plans_array,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(
			android.R.layout.simple_spinner_dropdown_item);
		/* Apply the adapter to the spinner */
		this.planSpinner.setAdapter(adapter);
		this.planSpinner.
			setOnItemSelectedListener(new MealPlanSelectionListener(this));

		/* Handle changes to startingBalance. */
		this.startingBalance =
			(EditText)this.findViewById(R.id.starting_balance_edit);
		this.startingBalance.
			addTextChangedListener(new StartingBalanceWatcher(this));

		/* Load the end date. */
		this.loadEndDate();

		/* Set the input box. */
		EditText dateText = ((EditText)this.findViewById(R.id.last_day_edit));
		dateText.setText(CalenderActivity.createFormattedDate(this.endDate));

		dateText.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				requestCalendar(v);
			}
		});
		dateText.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(hasFocus)
				{
					requestCalendar(v);
				}
			}
		});
	}

	public void setIsPlanSelected(boolean isPlanSelected)
	{
		this.isPlanSelected = isPlanSelected;
	}

	public boolean getIsPlanSelected()
	{
		return this.isPlanSelected;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
			endDate = Meals.END_DATE;
			saveEndDate();
			((EditText)findViewById(R.id.last_day_edit)).setText(
				CalenderActivity.createFormattedDate(endDate));
			return true;
		}
		else if(id == R.id.about_action)
		{
			Intent intent =  new Intent(MainActivity.this, AboutActivity.class);
			this.startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	public void submitBalance(View view)
	{
		try
		{
			EditText edit = (EditText) (findViewById(R.id.current_balance_edit));
			float currentBalance = Float.parseFloat(edit.getText().toString());

			float startingBalance =
				Float.parseFloat(this.startingBalance.getText().toString());

			Intent intent = new Intent(MainActivity.this,
				DisplayMealsActivity.class);

			intent.putExtra(EXTRA_CURRENT_BALANCE, currentBalance);
			intent.putExtra(EXTRA_STARTING_BALANCE, startingBalance);
			intent.putExtra(EXTRA_END_DATE_DAY,
				this.endDate.get(Calendar.DAY_OF_MONTH));
			intent.putExtra(EXTRA_END_DATE_MONTH,
				this.endDate.get(Calendar.MONTH));
			intent.putExtra(EXTRA_END_DATE_YEAR,
				this.endDate.get(Calendar.YEAR));

			startActivity(intent);
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Invalid Field Value");
			builder.setMessage("All fields must have a value.\n");
			builder.setNeutralButton("Close", null);
			builder.show();
		}
	}

	public void requestCalendar(View view)
	{
		Intent intent = new Intent(MainActivity.this, CalenderActivity.class);

		intent.putExtra(
			EXTRA_END_DATE_DAY, this.endDate.get(Calendar.DAY_OF_MONTH)
		);
		intent.putExtra(EXTRA_END_DATE_MONTH, this.endDate.get(Calendar.MONTH));
		intent.putExtra(EXTRA_END_DATE_YEAR, this.endDate.get(Calendar.YEAR));

		startActivityForResult(intent, REQUEST_CALENDAR);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == REQUEST_CALENDAR && resultCode == Activity.RESULT_OK)
		{
			this.endDate = new GregorianCalendar(
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_YEAR,
					Meals.END_DATE.get(Calendar.YEAR)),
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_MONTH,
					Meals.END_DATE.get(Calendar.MONTH)),
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_DAY,
					Meals.END_DATE.get(Calendar.DAY_OF_MONTH))
			);

			this.saveEndDate();

			((EditText)findViewById(R.id.last_day_edit)).setText(
				CalenderActivity.createFormattedDate(endDate));
		}

	}

	private void saveEndDate()
	{
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putInt(PREF_END_DATE_DAY,
			this.endDate.get(Calendar.DAY_OF_MONTH));
		editor.putInt(PREF_END_DATE_MONTH, this.endDate.get(Calendar.MONTH));
		editor.putInt(PREF_END_DATE_YEAR, this.endDate.get(Calendar.YEAR));
		editor.apply();
	}

	/**
	 * Saves the selected position to SharedPreferences.
	 */
	public void savePlanSelection()
	{
		SharedPreferences.Editor editor =
			this.getPreferences(Activity.MODE_PRIVATE).edit();
		editor.putInt(MainActivity.PREF_SELECTED_PLAN,
			this.planSpinner.getSelectedItemPosition());
		editor.apply();
	}

	/**
	 * Save the 
	 */
	public void saveStartingBalance()
	{
		SharedPreferences.Editor editor =
			this.getPreferences(Activity.MODE_PRIVATE).edit();

		float balance;
		try
		{
			balance = Float.parseFloat(startingBalance.getText().toString());
		}
		catch(Exception e)
		{
			balance = -1;
		}

		editor.putFloat(MainActivity.PREF_STARTING_BALANCE, balance);
		editor.apply();
	}

	private void loadEndDate()
	{
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);

		/* Retrieve stored or default end date. */
		int day = preferences.getInt(
			PREF_END_DATE_DAY, Meals.END_DATE.get(Calendar.DAY_OF_MONTH)
		);
		int month = preferences.getInt(
			PREF_END_DATE_MONTH, Meals.END_DATE.get(Calendar.MONTH)
		);
		int year = preferences.getInt(PREF_END_DATE_YEAR,
			Meals.END_DATE.get(Calendar.YEAR));

		this.endDate = new GregorianCalendar(year, month, day);
	}
}
