package com.aquamorph.ecubustracker.Parsers;

import android.util.Log;

import com.aquamorph.ecubustracker.MainActivity;
import com.aquamorph.ecubustracker.Models.Predictions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HandleXML {

	private String TAG = "HandleXML";

	private String routeTitle = "routeTitle";
	private String routeTag = "routeTag";
	private String stopTitle = "stopTitle";
	private String stopTag = "stopTag";
	private String urlString = null;
	private XmlPullParserFactory xmlFactoryObject;
	private ArrayList<Predictions> predictions = new ArrayList<>();
	public volatile boolean parsingComplete = true;

	public HandleXML(String route, String stop) {
		this.urlString = MainActivity.URL + "?command=predictions&a=ecu&r=" + route + "&s=" + stop;
	}

	public String getRouteTag() {
		return routeTag;
	}

	public String getRouteTitle() {
		return routeTitle;
	}

	public String getStopTitle() {
		return stopTitle;
	}

	public String getStopTag() {
		return stopTag;
	}

	public ArrayList<Predictions> getPredictions() {
		return predictions;
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
						if (name.equals("predictions")) {
							routeTag = myParser.getAttributeValue(null, "routeTag");
							routeTitle = myParser.getAttributeValue(null, "routeTitle");
							stopTitle = myParser.getAttributeValue(null, "stopTitle");
							stopTag = myParser.getAttributeValue(null, "stopTag");
						}
						if (name.equals("prediction")) {
							Log.i(TAG, myParser.getAttributeValue(null, "seconds"));
							predictions.add(new Predictions(Integer.parseInt(myParser.getAttributeValue(null, "seconds")),
									Integer.parseInt(myParser.getAttributeValue(null, "minutes")),
									Boolean.parseBoolean(myParser.getAttributeValue(null, "isDeparture")),
									Boolean.parseBoolean(myParser.getAttributeValue(null, "affectedByLayover")),
									myParser.getAttributeValue(null, "dirTag"),
									Integer.parseInt(myParser.getAttributeValue(null, "vehicle")),
									Integer.parseInt(myParser.getAttributeValue(null, "block"))
											));
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
