package com.aquamorph.ecubustracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

	EditText ed1,ed2,ed3,ed4,ed5;

	public static String URL = "http://webservices.nextbus.com/service/publicXMLFeed?command=predictions&a=ecu";
	private HandleXML obj;
	Button b1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b1=(Button)findViewById(R.id.button);

		ed1=(EditText)findViewById(R.id.editText);
		ed2=(EditText)findViewById(R.id.editText2);
		ed3=(EditText)findViewById(R.id.editText3);
		ed4=(EditText)findViewById(R.id.editText4);
		ed5=(EditText)findViewById(R.id.editText5);

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String route = ed1.getText().toString();
				ed2.setText(route);

				obj = new HandleXML(route);
				obj.fetchXML();

				while (obj.parsingComplete);
				ed2.setText(obj.getRouteTag());
				ed3.setText(obj.getRouteTitle());
				ed4.setText(obj.getStopTitle());
				ed5.setText(obj.getStopTag());
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
