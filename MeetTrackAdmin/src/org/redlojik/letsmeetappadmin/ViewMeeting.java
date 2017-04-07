package org.redlojik.letsmeetappadmin;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewMeeting extends ListActivity {
	TextView currencyRt;
	TextView meetingNm;
	TextView meetDur;
	TextView meetCost;

	String meetName;
	String currStr;
	String startStr;
	String stopStr;

	long startLong;
	long stopLong;
	long duration;
	long minutes;

	float totalCost;

	ArrayList<HashMap<String, String>> mylist;
	String xml;
	Document doc;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);

		mylist = new ArrayList<HashMap<String, String>>();

		postXML();

	}

	private void postXML() {
		// TODO Auto-generated method stub
		XMLAsyncTask task1 = new XMLAsyncTask();
		task1.execute();

	}

	class XMLAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				xml = XMLfunctions.getXML();
				doc = XMLfunctions.XMLfromString(xml);

			}

			catch (Exception exception) {
				System.out.println(exception);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			NodeList nodes = doc.getElementsByTagName("attendee");

			Element e = (Element) nodes.item(0);
			meetName = "" + XMLfunctions.getValue(e, "meetname");
			currStr = "" + XMLfunctions.getValue(e, "currencyrate");
			startStr = "" + XMLfunctions.getValue(e, "starttime");
			stopStr = "" + XMLfunctions.getValue(e, "stoptime");

			startLong = Long.parseLong(startStr);
			stopLong = Long.parseLong(stopStr);
			duration = stopLong - startLong;
			minutes = (long) ((duration / (1000 * 60)) % 60);

			currencyRt = (TextView) findViewById(R.id.currencyRate);
			meetingNm = (TextView) findViewById(R.id.meetingName);
			meetDur = (TextView) findViewById(R.id.meetDuration);
			meetCost = (TextView) findViewById(R.id.meetingCost);

			currencyRt.setText("Meeting Cost in USD: $" + currStr);
			meetingNm.setText("Meeting Name: " + meetName);
			meetDur.setText("Duration(in minutes): " + Long.toString(minutes));

			if (minutes > 60) {
				float temp = minutes / 60;
				totalCost = Float.parseFloat(currStr) * temp;
			} else {
				totalCost = Float.parseFloat(currStr);
			}

			meetCost.setText("Meeting Cost in USD: $" + totalCost);

			for (int i = 0; i < nodes.getLength(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();

				Element e2 = (Element) nodes.item(i);
				map.put("contname", "" + XMLfunctions.getValue(e2, "contname"));
				map.put("phonenumber",
						"Contact:" + XMLfunctions.getValue(e2, "phonenumber"));
				mylist.add(map);
			}

			ListAdapter adapter = new SimpleAdapter(ViewMeeting.this, mylist,
					R.layout.viewlogs,
					new String[] { "contname", "phonenumber" }, new int[] {
							R.id.contName, R.id.phoneNumber });

			setListAdapter(adapter);
		}
	}

}
