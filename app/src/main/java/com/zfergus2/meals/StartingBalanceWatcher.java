package com.zfergus2.meals;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

/**
 * Changes the Meals plan to optional if starting balance changed.
 * @author Zachary Ferguson
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

	/**
	 * Before the text is changed. Do nothing.
	 * @param s Not used.
	 * @param start Not used.
	 * @param count Not used.
	 * @param after Not used.
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after)
	{
		return;
	}

	/**
	 * When the text is changed do nothing.
	 * @param s Not used.
	 * @param start Not used.
	 * @param before Not used.
	 * @param count Not used.
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		return;
	}

	/**
	 * After the text is changed save the starting balance to shared
	 * preferences.
	 * @param s The TextField.
	 */
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
