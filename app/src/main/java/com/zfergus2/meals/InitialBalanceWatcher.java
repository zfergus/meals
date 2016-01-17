package com.zfergus2.meals;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

/**
 * Changes the Meals plan to optional if initial balance changed.
 */
public class InitialBalanceWatcher implements TextWatcher
{
	private MainActivity meals;

	/**
	 * Creates a InitialBalanceWatcher to watch for changes in the initial
	 * balance.
	 * @param meals The main activity of Meals.
	 */
	public InitialBalanceWatcher(MainActivity meals)
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
			Spinner plans =
				(Spinner) this.meals.findViewById(R.id.plan_spinner);
			plans.setSelection(plans.getAdapter().getCount() - 1);
		}
		else
		{
			this.meals.setIsPlanSelected(!this.meals.getIsPlanSelected());
		}
	}
}
