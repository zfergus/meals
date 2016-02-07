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
 * @author Zachary Ferguson
 */
public class MainActivity extends AppCompatActivity
{
	/** Key of the Stating balance. **/
	public final static String EXTRA_STARTING_BALANCE =
		"com.zfergus2.meals.STARTING";
	public final static String EXTRA_CURRENT_BALANCE =
		"com.zfergus2.meals.REMAINING";
	public final static String EXTRA_END_DATE_DAY =
		"com.zfergus2.meals.END_DATE_DAY";
	public final static String EXTRA_END_DATE_MONTH =
		"com.zfergus2.meals.END_DATE_MONTH";
	public final static String EXTRA_END_DATE_YEAR =
		"com.zfergus2.meals.END_DATE_YEAR";

	/** Request code for a calendar request. **/
	private final static int REQUEST_CALENDAR = 0xCAFE;

	/*                   Keys of shared preference values.                    */
	private final static String PREF_END_DATE_DAY = "END_DATE_DAY";
	private final static String PREF_END_DATE_MONTH = "END_DATE_MONTH";
	private final static String PREF_END_DATE_YEAR = "END_DATE_YEAR";
	public final static String PREF_SELECTED_PLAN = "SELECTED_PLAN";
	public final static String PREF_STARTING_BALANCE = "STARTING_BALANCE";

	/** Is a meal plan selected? **/
	private boolean isPlanSelected;

	/**
	 * Set if a plan is selected.
	 * @param isPlanSelected New value of this.isPlanSelected.
	 */
	public void setIsPlanSelected(boolean isPlanSelected)
	{
		this.isPlanSelected = isPlanSelected;
	}

	/**
	 * Get the current value of this.isPlanSelected.
	 * @return Returns if a meal plan is selected.
	 */
	public boolean getIsPlanSelected()
	{
		return this.isPlanSelected;
	}

	/** Last day to use the allotted funds. **/
	private Calendar endDate;

	/** Reference to the plan spinner. **/
	private Spinner planSpinner;
	/** Reference to the starting balance field. **/
	private EditText startingBalance;

	/** Url of the Xml file containing the default end date. **/
	private static final String END_DATE_URL =
		"http://zfergus.me/com.zfergus2.Meals.default_end_date.xml";

	/** Default end date. **/
	public static Calendar defaultEndDate;

	/**
	 * On creation of the MainActivity load values.
	 * @param savedInstanceState Passed to super.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.planSpinner = (Spinner) findViewById(R.id.plan_spinner);
		/* Create an ArrayAdapter using the string array and a default */
		/* spinner layout.                                             */
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
			.createFromResource(this, R.array.plans_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(
			android.R.layout.simple_spinner_dropdown_item);
		/* Apply the adapter to the spinner */
		this.planSpinner.setAdapter(adapter);
		this.planSpinner.
			setOnItemSelectedListener(new MealPlanSelectionListener(this));

		/* Handle changes to startingBalance. */
		this.startingBalance =
			(EditText) this.findViewById(R.id.starting_balance_edit);
		this.startingBalance.
			addTextChangedListener(new StartingBalanceWatcher(this));

		/* Set the default end date. */
		MainActivity.defaultEndDate = Meals.END_DATE;
		(new DownloadXmlTask()).execute(MainActivity.END_DATE_URL);

		/* Load the selected end date from shared preferences. */
		this.loadEndDate();

		/* Set the input box. */
		EditText dateText = ((EditText) this.findViewById(R.id.last_day_edit));
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

	/**
	 * Inflate the menu to fit the screen.
	 * @param menu Menu to be created.
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
	 * When menu item is selected execute the associated code.
	 * @param item Selected item.
	 * @return Returns if the item was used.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		/* Reset to the default end date. */
		if(id == R.id.reset_date_action)
		{
			endDate = MainActivity.defaultEndDate;
			saveEndDate();
			((EditText) findViewById(R.id.last_day_edit))
				.setText(CalenderActivity.createFormattedDate(endDate));
			return true;
		}
		/* Load the about page. */
		else if(id == R.id.about_action)
		{
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			this.startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Create a new intent for DisplayMessageActivity.
	 * @param view This view.
	 */
	public void submitMessageData(View view)
	{
		try
		{
			EditText edit = (EditText) (findViewById(R.id.current_balance_edit));
			float currentBalance = Float.parseFloat(edit.getText().toString());

			float startingBalance =
				Float.parseFloat(this.startingBalance.getText().toString());

			Intent intent = new Intent(MainActivity.this, DisplayMealsActivity.class);

			intent.putExtra(EXTRA_CURRENT_BALANCE, currentBalance);
			intent.putExtra(EXTRA_STARTING_BALANCE, startingBalance);
			intent.putExtra(EXTRA_END_DATE_DAY,
				this.endDate.get(Calendar.DAY_OF_MONTH));
			intent.putExtra(EXTRA_END_DATE_MONTH,
				this.endDate.get(Calendar.MONTH));
			intent
				.putExtra(EXTRA_END_DATE_YEAR, this.endDate.get(Calendar.YEAR));

			startActivity(intent);
		}
		catch(Exception e)
		{
			/* Some fields are empty. */
			System.err.println(e.toString());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Invalid Field Value");
			builder.setMessage("All fields must have a value.\n");
			builder.setNeutralButton("Close", null);
			builder.show();
		}
	}

	/**
	 * Create a request intent to get the end date.
	 * @param view This view.
	 */
	public void requestCalendar(View view)
	{
		Intent intent = new Intent(MainActivity.this, CalenderActivity.class);

		intent.putExtra(EXTRA_END_DATE_DAY, this.endDate.get(Calendar.DAY_OF_MONTH));
		intent.putExtra(EXTRA_END_DATE_MONTH, this.endDate.get(Calendar.MONTH));
		intent.putExtra(EXTRA_END_DATE_YEAR, this.endDate.get(Calendar.YEAR));

		startActivityForResult(intent, REQUEST_CALENDAR);
	}

	/**
	 * When the result of an intent is returned, run the associated code.
	 * @param requestCode The request identifier.
	 * @param resultCode Was the intent successful.
	 * @param data Intent that finished.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		/* Calendar picker finished. */
		if(requestCode == REQUEST_CALENDAR && resultCode == Activity.RESULT_OK)
		{
			this.endDate = new GregorianCalendar(
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_YEAR,
					MainActivity.defaultEndDate.get(Calendar.YEAR)),
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_MONTH,
					MainActivity.defaultEndDate.get(Calendar.MONTH)),
				data.getIntExtra(CalenderActivity.EXTRA_END_DATE_DAY,
					MainActivity.defaultEndDate.get(Calendar.DAY_OF_MONTH)));

			this.saveEndDate();

			((EditText) findViewById(R.id.last_day_edit))
				.setText(CalenderActivity.createFormattedDate(endDate));
		}

	}

	/**
	 * Save the selected end date to the shared preferences.
	 */
	private void saveEndDate()
	{
		SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor
			.putInt(PREF_END_DATE_DAY, this.endDate.get(Calendar.DAY_OF_MONTH));
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
	 * Save the starting balance to the shared preferences.
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

	/**
	 * Load the selected end date from shared preferences.
	 */
	private void loadEndDate()
	{
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);

		/* Retrieve stored or default end date. */
		int day = preferences.getInt(PREF_END_DATE_DAY,
			MainActivity.defaultEndDate.get(Calendar.DAY_OF_MONTH));
		int month = preferences.getInt(PREF_END_DATE_MONTH,
			MainActivity.defaultEndDate.get(Calendar.MONTH));
		int year = preferences.getInt(PREF_END_DATE_YEAR,
			MainActivity.defaultEndDate.get(Calendar.YEAR));

		this.endDate = new GregorianCalendar(year, month, day);
	}
}
