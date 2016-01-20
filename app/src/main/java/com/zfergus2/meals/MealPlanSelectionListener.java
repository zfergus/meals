package com.zfergus2.meals;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Changes the MainActivity's initial balance based on the plan spinner.
 */
public class MealPlanSelectionListener
	implements AdapterView.OnItemSelectedListener
{
	private MainActivity meals;

	private boolean isFirstSelection;

	/**
	 * Creates a MealPlanSelectionListener to listen for changes in meal plans.
	 * @param meals The main activity of Meals.
	 */
	public MealPlanSelectionListener(MainActivity meals)
	{
		this.meals = meals;
		this.isFirstSelection = true;
	}

	/**
	 * Changes the initial balance to the plan selected.
	 * @param parent The AdapterView where the selection happened.
	 * @param view The view within the AdapterView that was clicked.
	 * @param pos The position of the view in the adapter.
	 * @param id 	The row id of the item that is selected.
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
							   long id)
	{
		/* The initText value has not been set. */
		if(this.isFirstSelection)
		{
			initializeValues(parent);
			return;
		}

		if (pos != Spinner.INVALID_POSITION && pos < Meals.PLANS.length)
		{
			setToPlan(pos);
			this.meals.saveStartingBalance();
		}

		this.meals.savePlanSelection();
	}

	/**
	 * Set the start balance to the one at Meals.PLANS[pos].
	 * @param pos Index into plans.
	 */
	private void setToPlan(int pos)
	{
		if (pos < 0 || pos >= Meals.PLANS.length)
		{
			return;
		}

		EditText startingBalanceEdit =
			(EditText) (this.meals.findViewById(R.id.starting_balance_edit));

		meals.setIsPlanSelected(true);
		float startingBalance = Meals.PLANS[pos];
		startingBalanceEdit.setText(String.format("%.2f", startingBalance));
	}

	/**
	 * Initialize the values of the Spinner and Starting balance.
	 * @param parent The spinner.
	 */
	private void initializeValues(AdapterView<?> parent)
	{
		this.isFirstSelection = false;
		EditText startingBalanceEdit =
			(EditText) this.meals.findViewById(R.id.starting_balance_edit);

		SharedPreferences preferences =
			this.meals.getPreferences(Activity.MODE_PRIVATE);

		int initPlan =
			preferences.getInt(MainActivity.PREF_SELECTED_PLAN, 0);
		float startingBalance =
			preferences.getFloat(MainActivity.PREF_STARTING_BALANCE, -1);

		if(initPlan >= 0 && initPlan < Meals.PLANS.length)
		{
			parent.setSelection(initPlan);
			setToPlan(initPlan);
		}
		else if(startingBalance >= 0)
		{
			parent.setSelection(parent.getAdapter().getCount()-1);
			startingBalanceEdit.setText(String.format("%.2f", startingBalance));
		}
		else
		{
			parent.setSelection(0);
			setToPlan(0);
		}
	}

	/**
	 * Sets the initial balance to zero.
	 * @param parent The AdapterView where the selection happened.
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		EditText startingBalanceEdit =
			(EditText) (this.meals.findViewById(R.id.starting_balance_edit));
		startingBalanceEdit.setText("0.0");
	}
}
