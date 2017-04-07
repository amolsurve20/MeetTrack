package org.redlojik.letsmeetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	private EditText username;
	private EditText password;
	private Button submitBtn;

	String usernameStr, passwordStr;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initializeComponents();
	}

	private void initializeComponents() {
		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		submitBtn = (Button) findViewById(R.id.submitButton);

		submitBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				loadValues();
			}
		});
	}

	private void loadValues() {
		usernameStr = username.getText().toString();
		passwordStr = password.getText().toString();

		Intent i = new Intent(this, SoapProcess1.class);

		Bundle bun = new Bundle();
		bun.putString("unamekey", usernameStr);
		bun.putString("passkey", passwordStr);

		i.putExtras(bun);

		startActivity(i);
	}
}