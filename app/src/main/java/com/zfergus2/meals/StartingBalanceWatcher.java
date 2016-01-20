package com.zfergus2.meals;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

/**
 * Changes the Meals plan to optional if starting balance changed.
 */
public class StartingBalanceWatcher implements TextWatcher
{
	private MainActivity meals;

	/**
	 * Creates a StartingBalanceWatcher to watch for changes in the starting
	 * balance.
	 * @param meals The main activity of Meals.
	 */
	public StartingBalanceWatcher(MainActivity meals)
	{
		this.meals = meals;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after)
	{
		return;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		return;
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if(!this.meals.getIsPlanSelected())
		{
			/* Set spinner selection to "other" */
			Spinner plans = (Spinner)this.meals.findViewById(R.id.plan_spinner);
			plans.setSelection(plans.getAdapter().getCount() - 1);

			this.meals.saveStartingBalance();
		}
		else
		{
			this.meals.setIsPlanSelected(!this.meals.getIsPlanSelected());
		}
	}
}
