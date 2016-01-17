package com.zfergus2.meals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;

/**
 * Main activity for Meals. Gets user input for the balance and end date. Sends
 * this data to a DisplayMessageActivity.
 */
public class MainActivity extends AppCompatActivity
{
	public final static String EXTRA_INIT_BALANCE = "com.zfergus2.meals.INIT";
	public final static String EXTRA_CURRENT_BALANCE =
			"com.zfergus2.meals.REMAINING";

	private boolean isPlanSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Spinner spinner = (Spinner) findViewById(R.id.plan_spinner);
		/* Create an ArrayAdapter using the string array and a default */
		/* spinner layout.                                             */
		ArrayAdapter<CharSequence> adapter =
				ArrayAdapter.createFromResource(this,
						R.array.plans_array,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(
			android.R.layout.simple_spinner_dropdown_item);
		/* Apply the adapter to the spinner */
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MealPlanSelectionListener(this));

		/* Handle changes to initBalance. */
		((EditText)this.findViewById(R.id.init_balance_edit)).
			addTextChangedListener(new InitialBalanceWatcher(this));
		this.isPlanSelected = true;

		/* Add calendar popup. */
		EditText dateSelect = (EditText)this.findViewById(R.id.end_date_edit);
		dateSelect.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showCalendarPopup((EditText) v);
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
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showCalendarPopup(final EditText editText)
	{
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout =
			layoutInflater.inflate(R.layout.calendar_popup, null, false);
		final PopupWindow popupWindow = new PopupWindow(layout, 600, 600);

		popupWindow.setContentView(layout);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		CalendarView cv = (CalendarView) layout.findViewById(R.id.calendarView);
		cv.setBackgroundColor(Color.WHITE);
		cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
		{

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
											int month, int dayOfMonth)
			{
				popupWindow.dismiss();
				editText.setText(
					String.format("%d/%d/%d", dayOfMonth, month+1, year));
			}
		});
		popupWindow.showAtLocation(layout, Gravity.TOP, 5, 170);
	}

	public void submitBalance(View view)
	{
		try
		{
			EditText edit = (EditText) (findViewById(R.id.current_balance_edit));
			float currentBalance = Float.parseFloat(edit.getText().toString());

			edit = (EditText) (findViewById(R.id.init_balance_edit));
			float initBalance = Float.parseFloat(edit.getText().toString());

			Intent intent = new Intent(this, DisplayMessageActivity.class);

			intent.putExtra(EXTRA_CURRENT_BALANCE, currentBalance);
			intent.putExtra(EXTRA_INIT_BALANCE, initBalance);

			startActivity(intent);
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
	}
}
