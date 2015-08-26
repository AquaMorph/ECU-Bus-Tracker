package com.aquamorph.ecubustracker;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RouteList {
	private String TAG = "RouteList";

	private String tag = "tag";
	private String title = "title";
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	private ArrayList<String> routes = new ArrayList<>();
	private ArrayList<String> titles = new ArrayList<>();

	public RouteList() {
		this.urlString = MainActivity.URL + "?command=routeList&a=ecu";
		Log.i(TAG, "URL: " + urlString);
	}

	public ArrayList<String> getRoutes() {
		return routes;
	}

	public ArrayList<String> getTitles() {
		return titles;
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
						if (name.equals("route")) {
							tag = myParser.getAttributeValue(null, "tag");
							title = myParser.getAttributeValue(null, "title");
							routes.add(tag);
							titles.add(title);
//							Log.i(TAG, "Tag: " + tag);
//							Log.i(TAG, "Title: " + title);
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
