package org.redlojik.letsmeetapp.createmeeting;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.redlojik.letsmeetapp.R;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AddMeeting extends Activity implements OnClickListener {

	EditText meetingName;
	ToggleButton startStop;
	TextView selectedList;
	Button addContacts;

	int reqCode;
	String number;
	String contact;

	long startTime;
	long stopTime;
	String meetNm;

	int a, j;

	String[] phnumber, contname;
	StringBuilder sb;

	public static final String FILENAME = "testfile.xml";
	public static final String PREFS_NAME = "CurrencyPrefsFile";
	private static final int PICK_CONTACT = 0;
	SharedPreferences value;

	String tempString = null, tempCurr = null;
	StreamResult result;
	DOMSource source;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmeeting);

		meetingName = (EditText) findViewById(R.id.meetingname);
		startStop = (ToggleButton) findViewById(R.id.startstoptoggle);
		addContacts = (Button) findViewById(R.id.addrecipientsbutton);
		selectedList = (TextView) findViewById(R.id.selectedlist);

		phnumber = new String[100];
		contname = new String[100];
		sb = new StringBuilder();

		j = 0;
		addContacts.setOnClickListener(this);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		meetNm = meetingName.getText().toString();

		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);

		value = getSharedPreferences(PREFS_NAME, 0);
		tempCurr = value.getString("currencyValue", "Dummy");
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {

		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phones.moveToFirst();
						number = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}

					Cursor phones1 = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + id, null, null);
					phones1.moveToFirst();
					contact = phones1
							.getString(phones1
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

				}

				phnumber[j] = number;
				contname[j] = contact;

				System.out.println(contact + " " + number);

				for (int i = 0; i <= j; i++)
					System.out.println("Array: " + contname[i] + " "
							+ phnumber[i]);

				sb.append(contact + ": " + number + "\n");
			}

			this.selectedList.setText(sb.toString());

			j++;
		}
	}

	public void onToggleClicked(View v) {
		// Perform action on clicks
		if (((ToggleButton) v).isChecked()) {
			startTime = System.currentTimeMillis();
			startMeeting();
		} else {
			stopTime = System.currentTimeMillis();
			stopMeeting();

		}
	}

	private void startMeeting() {
		// TODO Auto-generated method stub
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("meeting");
			doc.appendChild(rootElement);

			for (int i = 0; i < j; i++) {
				// attendee elements
				Element attendee = doc.createElement("attendee");
				rootElement.appendChild(attendee);

				// set attribute to attendee element
				Attr attr = doc.createAttribute("id");
				attr.setValue("" + (i + 1));
				attendee.setAttributeNode(attr);

				Element meetingName = doc.createElement("meetname");
				meetingName.appendChild(doc.createTextNode(meetNm));
				attendee.appendChild(meetingName);

				Element contactname = doc.createElement("contname");
				contactname.appendChild(doc.createTextNode(contname[i]));
				attendee.appendChild(contactname);

				Element phonenumber = doc.createElement("phonenumber");
				phonenumber.appendChild(doc.createTextNode(phnumber[i]));
				attendee.appendChild(phonenumber);

				Element srtTm = doc.createElement("starttime");
				srtTm.appendChild(doc.createTextNode("" + startTime));
				attendee.appendChild(srtTm);

				Element currRate = doc.createElement("currencyrate");
				currRate.appendChild(doc.createTextNode("" + tempCurr));
				attendee.appendChild(currRate);

			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			source = new DOMSource(doc);

			try {
				FileOutputStream fos = openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				result = new StreamResult(fos);

				transformer.transform(source, result);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

		System.out.println("File saved!");

		postXML();
	}

	private void stopMeeting() {
		// TODO Auto-generated method stub
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("meeting");
			doc.appendChild(rootElement);

			for (int i = 0; i < j; i++) {
				// attendee elements
				Element attendee = doc.createElement("attendee");
				rootElement.appendChild(attendee);

				// set attribute to attendee element
				Attr attr = doc.createAttribute("id");
				attr.setValue("" + (i + 1));
				attendee.setAttributeNode(attr);

				Element meetingName = doc.createElement("meetname");
				meetingName.appendChild(doc.createTextNode(meetNm));
				attendee.appendChild(meetingName);

				Element contactname = doc.createElement("contname");
				contactname.appendChild(doc.createTextNode(contname[i]));
				attendee.appendChild(contactname);

				Element phonenumber = doc.createElement("phonenumber");
				phonenumber.appendChild(doc.createTextNode(phnumber[i]));
				attendee.appendChild(phonenumber);

				Element srtTm = doc.createElement("starttime");
				srtTm.appendChild(doc.createTextNode("" + startTime));
				attendee.appendChild(srtTm);

				Element stpTm = doc.createElement("stoptime");
				stpTm.appendChild(doc.createTextNode("" + stopTime));
				attendee.appendChild(stpTm);

				Element currRate = doc.createElement("currencyrate");
				currRate.appendChild(doc.createTextNode("" + tempCurr));
				attendee.appendChild(currRate);

			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			source = new DOMSource(doc);

			try {
				FileOutputStream fos = openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				result = new StreamResult(fos);

				transformer.transform(source, result);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

		System.out.println("File saved!");

		postXML();

	}

	private void postXML() {
		// TODO Auto-generated method stub
		try {

			FileInputStream foi = openFileInput(FILENAME);
			DataInputStream in = new DataInputStream(foi);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuilder sb2 = new StringBuilder();
			while ((strLine = br.readLine()) != null) {

				sb2.append(strLine);
				System.out.println("Inside while");
				// tvData.setText(strLine);
			}

			Intent i = new Intent(this, SoapProcess.class);
			Bundle bun = new Bundle();
			bun.putString("fileKey", sb2.toString());
			i.putExtras(bun);

			startActivity(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}