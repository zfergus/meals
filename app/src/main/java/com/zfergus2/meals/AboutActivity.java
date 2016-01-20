package com.zfergus2.meals;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		TextView aboutText = (TextView)this.findViewById(R.id.about_text);
		aboutText.setText(this.getString(R.string.about_text));
	}

}
