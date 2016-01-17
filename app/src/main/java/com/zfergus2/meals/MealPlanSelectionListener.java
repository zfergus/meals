package com.zfergus2.meals;

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

	/**
	 * Creates a MealPlanSelectionListener to listen for changes in meal plans.
	 * @param meals The main activity of Meals.
	 */
	public MealPlanSelectionListener(MainActivity meals)
	{
		this.meals = meals;
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
		float initBalance;
		EditText initText =
			(EditText) (this.meals.findViewById(R.id.init_balance_edit));

		if (pos != Spinner.INVALID_POSITION && pos >= 0 &&
			pos < Meals.PLANS.length)
		{
			initBalance = Meals.PLANS[pos];
			initText.setText("" + initBalance);
		}

		meals.setIsPlanSelected(true);
	}

	/**
	 * Sets the initial balance to zero.
	 * @param parent The AdapterView where the selection happened.
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		EditText initText =
			(EditText) (this.meals.findViewById(R.id.init_balance_edit));
		initText.setText("0.0");
	}
}
