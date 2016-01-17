package com.zfergus2.meals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);

        /* Get the message from the intent. */
		Intent intent = getIntent();
		float initBalance =
				intent.getFloatExtra(MainActivity.EXTRA_INIT_BALANCE, 0f);
		float currentBalance =
				intent.getFloatExtra(MainActivity.EXTRA_CURRENT_BALANCE, 0f);

        /* Calculate the average and create a message. */
		String message = Meals.getMessage(initBalance, currentBalance,
			Meals.END_DATE);

        /* Create the text view. */
		TextView textView = (TextView) (findViewById(R.id.message_view));
		textView.setText(message);
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
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
