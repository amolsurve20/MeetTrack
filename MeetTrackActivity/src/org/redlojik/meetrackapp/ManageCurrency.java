package org.redlojik.letsmeetapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageCurrency extends Activity implements OnClickListener {
	EditText currencyRate;
	Button saveCurrency;
	String tempCurrency=null;

	public static final String PREFS_NAME = "CurrencyPrefsFile";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.managecurrency);

		currencyRate = (EditText) (findViewById(R.id.currencyUSD));
		saveCurrency = (Button) (findViewById(R.id.saveCurrency));

		saveCurrency.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		SharedPreferences value = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = value.edit();
		tempCurrency = currencyRate.getText().toString();
		
		if(!(tempCurrency.equals("")))
		{
			editor.putString("currencyValue", tempCurrency);
			editor.commit();
			
			Toast.makeText(this, "Rate Saved", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(this, "Please enter rate or Press back to return to previous screen", Toast.LENGTH_LONG).show();
	}
}
