package com.aquamorph.ecubustracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aquamorph.ecubustracker.Models.Routes;
import com.aquamorph.ecubustracker.Parsers.RouteList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private String TAG = "MainActivity";
	public static final String URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	public static final String UNIVERSITY = "&a=ecu";
	private RecyclerView recyclerView;
	private RouteListAdapter adapter;
	ArrayList<Routes> routes = new ArrayList<>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getRoutes();
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.routeRV);
		adapter = new RouteListAdapter(getApplicationContext(), routes);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(llm);
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
			openSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void openSettings() {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button:
				Intent intent = new Intent(this, PredictionsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("route", "803");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
		}
	}

	public void getRoutes() {
		final LoadRouteListTask loadPredictionsTask = new LoadRouteListTask();
		loadPredictionsTask.execute();
	}

	class LoadRouteListTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			RouteList routeList = new RouteList();
			routeList.fetchXML();
			while (routeList.parsingComplete) ;
			routes.clear();
			routes.addAll(routeList.getRoutes());
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			for (int i = 0; i < routes.size(); i++) {
				Log.i(TAG, "Routes: " + routes.get(i).getTitle());
			}
		}
	}
}
