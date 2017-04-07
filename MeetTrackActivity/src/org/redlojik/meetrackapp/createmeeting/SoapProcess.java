package org.redlojik.letsmeetapp.createmeeting;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class SoapProcess extends Activity {
	private static final String SOAP_ACTION = "http://10.0.2.2:8080/soap/uploadXMLDemo";
	private static final String OPERATION_NAME = "uploadFile";
	private static final String WSDL_TARGET_NAMESPACE = "urn:uploadXMLDemo";
	private static final String SOAP_ADDRESS = "http://10.0.2.2:8080/soap/servlet/rpcrouter";

	private String tempFile;

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		tempFile = b.getString("fileKey");
		System.out.println(tempFile);

		postXML();

	}

	private void postXML() {
		// TODO Auto-generated method stub
		XMLAsyncTask task1 = new XMLAsyncTask();
		task1.execute();

	}

	class XMLAsyncTask extends AsyncTask<Void, Void, Void> {

		String reply = "";

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
					OPERATION_NAME);
			request.addProperty("getXML", tempFile);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = false;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

			try {
				// httpTransport.debug = true;
				httpTransport.call(SOAP_ACTION, envelope);
				// textView.setText("=========" + httpTransport.responseDump);
				Object response = envelope.getResponse();
				// result.setText("" + response);

				reply = "" + response;

			}

			catch (Exception exception) {
				reply = "" + exception;
				System.out.println(reply);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			System.out.println(reply);
			finish();
		}

	}
}