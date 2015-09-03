package com.aquamorph.ecubustracker.Parsers;

import android.util.Log;

import com.aquamorph.ecubustracker.MainActivity;
import com.aquamorph.ecubustracker.Models.Routes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RouteList {
	private String TAG = "RouteList";

	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	private ArrayList<Routes> routes = new ArrayList<>();

	public RouteList() {
		this.urlString = MainActivity.URL + "?command=routeList&a=ecu";
		Log.i(TAG, "URL: " + urlString);
	}

	public ArrayList<Routes> getRoutes() {
		return routes;
	}

	public void parseXMLAndStoreIt(XmlPullParser myParser) {
		int event;

		try {
			event = myParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();

				switch (event) {
					case XmlPullParser.START_TAG:
						if (name.equals("route")) {
							routes.add(new Routes(myParser.getAttributeValue(null, "tag"),
									myParser.getAttributeValue(null, "title")));
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
					XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();

					xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					xmlPullParser.setInput(stream, null);

					parseXMLAndStoreIt(xmlPullParser);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}
