package org.redlojik.letsmeetapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewLogs extends ListActivity {
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

	ListAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);

		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

		try{
		String xml = XMLfunctions.getXML();
		Document doc = XMLfunctions.XMLfromString(xml);

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

		currencyRt.setText("Rate in USD: $" + currStr);
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
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		adapter = new SimpleAdapter(ViewLogs.this, mylist, R.layout.viewlogs,
				new String[] { "contname", "phonenumber" }, new int[] {
						R.id.contName, R.id.phoneNumber });

		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				System.out.println(adapter.getItem(position));

			}
		});
	}
}