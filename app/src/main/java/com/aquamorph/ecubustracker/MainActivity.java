package com.aquamorph.ecubustracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.aquamorph.ecubustracker.Models.Predictions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private String TAG = "MainActivity";
	public static String URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	private HandleXML obj;
	private RouteList obj2;
	private StopInfo obj3;
	private RecyclerView recyclerView;
	private PredictionAdapter adapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	ArrayList<Predictions> test = new ArrayList<>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //Hides keyboard
		setContentView(R.layout.activity_main);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

		recyclerView = (RecyclerView) findViewById(R.id.rv);
		adapter = new PredictionAdapter(getApplicationContext(), test);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(llm);

		final RefreshTask refreshTask = new RefreshTask();
		refreshTask.execute();

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				final RefreshTask refreshTask = new RefreshTask();
				refreshTask.execute();
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

	class RefreshTask extends AsyncTask<Void,Void,Void> {

		@Override
		protected void onPreExecute() {
			mSwipeRefreshLayout.setRefreshing(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String route = "508";

			obj = new HandleXML(route);
			obj.fetchXML();

			//Lists all info about a stop
			while (obj.parsingComplete) ;

			test.clear();
			test.addAll(obj.getPredictions());

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
}
