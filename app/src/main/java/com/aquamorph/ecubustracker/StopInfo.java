package com.aquamorph.ecubustracker;

import android.util.Log;

import com.aquamorph.ecubustracker.Models.Stops;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StopInfo {
	private String TAG = "StopInfo";

	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	private ArrayList<Stops> stops = new ArrayList<>();

	public StopInfo(String stop) {
		this.urlString = MainActivity.URL + "?command=routeConfig&a=ecu&r=" + stop;
		Log.i(TAG, "URL: " + urlString);
	}

	public ArrayList<Stops> getStops() {
		return stops;
	}

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		int event;
		String text = null;

		try {
			event = myParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();

				switch (event) {
					case XmlPullParser.START_TAG:
						if (name.equals("stop")) {
							if (myParser.getAttributeValue(null, "title") != null) {
								stops.add(new Stops(myParser.getAttributeValue(null, "title"),
										myParser.getAttributeValue(null, "tag"),
										Double.parseDouble(myParser.getAttributeValue(null, "lat")),
										Double.parseDouble(myParser.getAttributeValue(null, "lon")),
										myParser.getAttributeValue(null, "stopId")));
							}
						}
						break;

					case XmlPullParser.TEXT:
						myParser.getText();
						break;

					case XmlPullParser.END_TAG:
						break;
				}
				event = myParser.next();
			}
			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fetchXML() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();

					InputStream stream = conn.getInputStream();
					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myparser = xmlFactoryObject.newPullParser();

					myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myparser.setInput(stream, null);

					parseXMLAndStoreIt(myparser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}

