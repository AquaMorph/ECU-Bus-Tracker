package com.aquamorph.ecubustracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

	EditText ed1, ed2, ed3, ed4, ed5;

	private String TAG = "MainActivity";
	public static String URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	private HandleXML obj;
	private RouteList obj2;
	private StopInfo obj3;
	Button b1;
	TextView tv1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b1 = (Button) findViewById(R.id.button);
		tv1 = (TextView) findViewById(R.id.textView);

		ed1 = (EditText) findViewById(R.id.editText);
		ed2 = (EditText) findViewById(R.id.editText2);
		ed3 = (EditText) findViewById(R.id.editText3);
		ed4 = (EditText) findViewById(R.id.editText4);
		ed5 = (EditText) findViewById(R.id.editText5);

		ed1.setText("508");

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button Pressed");
				String route = ed1.getText().toString();
				ed2.setText(route);

				obj = new HandleXML(route);
				obj.fetchXML();

				obj2 = new RouteList();
				obj2.fetchXML();

				obj3 = new StopInfo(route);
				obj3.fetchXML();

				//Lists all info about a stop
				while (obj.parsingComplete) ;
				ed2.setText(obj.getRouteTag());
				ed3.setText(obj.getRouteTitle());
				ed4.setText(obj.getStopTitle());
				ed5.setText(obj.getStopTag());

				for(int i = 0; i < obj.getPredictions().size(); i++) {
					tv1.setText(tv1.getText() + " " + obj.getPredictions().get(i).getSeconds());
				}

				//Lists all routes
				while (obj2.parsingComplete) ;
				for (Map.Entry<String, String> entry : obj2.getRouteInfo().entrySet()) {
//					tv1.setText(tv1.getText() + " " + entry.getKey() + " " + entry.getValue());
				}

				//Lists all stops
				while (obj3.parsingComplete) ;
				for (Map.Entry<String, String> entry : obj3.getStopInfo().entrySet()) {
					tv1.setText(tv1.getText() + " " + entry.getKey() + " " + entry.getValue());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
