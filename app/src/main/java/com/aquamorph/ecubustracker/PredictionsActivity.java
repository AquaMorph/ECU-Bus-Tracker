package com.aquamorph.ecubustracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aquamorph.ecubustracker.Models.Predictions;
import com.aquamorph.ecubustracker.Models.Routes;
import com.aquamorph.ecubustracker.Models.Stops;
import com.aquamorph.ecubustracker.Parsers.RouteInfo;
import com.aquamorph.ecubustracker.Parsers.RouteList;
import com.aquamorph.ecubustracker.Parsers.StopInfo;

import java.util.ArrayList;

public class PredictionsActivity extends AppCompatActivity {
	private String TAG = "MainActivity";
	public static final String URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	public static final String UNIVERSITY = "&a=ecu";
	private RecyclerView recyclerView;
	private TextView noData;
	private PredictionAdapter adapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	ArrayList<Predictions> predictions = new ArrayList<>();
	ArrayList<Routes> routes = new ArrayList<>();
	ArrayList<Stops> stops = new ArrayList<>();
	ArrayList<String> stopNames = new ArrayList<>();
	String routeID = "508";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.predictions);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

		recyclerView = (RecyclerView) findViewById(R.id.rv);
		adapter = new PredictionAdapter(getApplicationContext(), predictions);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(llm);

		getRoutes();
		getStop();
		refresh();

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
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
			openSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		final LoadPredictionsTask loadPredictionsTask = new LoadPredictionsTask();
		loadPredictionsTask.execute();
	}

	public void getRoutes() {
		final LoadRouteListTask loadPredictionsTask = new LoadRouteListTask();
		loadPredictionsTask.execute();
	}

	public void getStop() {
		final LoadStopTask loadStopTask = new LoadStopTask();
		loadStopTask.execute();
	}

	public void openSettings() {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	class LoadPredictionsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			mSwipeRefreshLayout.setRefreshing(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			RouteInfo routeInfo;

			routeInfo = new RouteInfo(routeID, stops.get(0).getTag());
			routeInfo.fetchXML();

			//Lists all info about a stop
			while (routeInfo.parsingComplete) ;

			predictions.clear();
			predictions.addAll(routeInfo.getPredictions());
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.notifyDataSetChanged();
			mSwipeRefreshLayout.setRefreshing(false);
		}
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

	class LoadStopTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			StopInfo stopInfo = new StopInfo(routeID);
			stopInfo.fetchXML();
			while (stopInfo.parsingComplete) ;
			stops.clear();
			stops.addAll(stopInfo.getStops());
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			for (int i = 0; i < stops.size(); i++) {
				Log.i(TAG, "Stops: " + stops.get(i).getTag());
				stopNames.add(stops.get(i).getTitle());
			}
		}
	}
}
