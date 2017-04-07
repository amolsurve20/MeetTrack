package org.redlojik.letsmeetapp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SoapProcess1 extends Activity {
	private static final String SOAP_ACTION = "http://10.0.2.2:8080/soap/greetingService"; // 192.168.0.104
																								// 10.0.2.2
	private static final String OPERATION_NAME = "sayGreeting";
	private static final String WSDL_TARGET_NAMESPACE = "urn:greetingService";
	private static final String SOAP_ADDRESS = "http://10.0.2.2:8080/soap/servlet/rpcrouter";
	private String usrnm;
	private String psswrd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		usrnm = b.getString("unamekey");
		psswrd = b.getString("passkey");

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		request.addProperty("username", usrnm);
		request.addProperty("password", psswrd);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

		try {
			// httpTransport.debug = true;
			httpTransport.call(SOAP_ACTION, envelope);
			Object response = envelope.getResponse();
			String tempRes = response.toString();
			
			if(tempRes.equals("Authenticated"))
			{
				Intent i = new Intent(this, LetsMeetAPPActivity.class);
				startActivity(i);
			}
			else
				Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
		}

		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}