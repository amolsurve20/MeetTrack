package org.redlojik.letsmeetapp;

import android.app.Activity;
import org.redlojik.letsmeetapp.createmeeting.AddMeeting;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LetsMeetAPPActivity extends Activity implements OnClickListener {
	View CreateButton;
	View LogsButton;
	View CurrButton;

	View AboutButton;
	View HelpButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeComponents();
	}

	private void initializeComponents() {
		CreateButton = findViewById(R.id.createbutton);
		LogsButton = findViewById(R.id.logsbutton);
		CurrButton = findViewById(R.id.currbutton);

		AboutButton = findViewById(R.id.aboutbutton);
		HelpButton = findViewById(R.id.helpbutton);

		CreateButton.setOnClickListener(this);
		LogsButton.setOnClickListener(this);
		CurrButton.setOnClickListener(this);

		AboutButton.setOnClickListener(this);
		HelpButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createbutton:
			Intent i1 = new Intent(this, AddMeeting.class);
			startActivity(i1);
			break;

		case R.id.logsbutton:
			Intent i2 = new Intent(this, ViewLogs.class);
			startActivity(i2);
			break;

		case R.id.currbutton:
			Intent i3 = new Intent(this, ManageCurrency.class);
			startActivity(i3);
			break;

		case R.id.aboutbutton:
			Intent i4 = new Intent(this, AboutApp.class);
			startActivity(i4);
			break;

		case R.id.helpbutton:
			Intent i5 = new Intent(this, HelpApp.class);
			startActivity(i5);
			break;
		}

	}
}