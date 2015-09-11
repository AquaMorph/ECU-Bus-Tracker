package com.aquamorph.ecubustracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.aquamorph.ecubustracker.Models.Routes;
import com.aquamorph.ecubustracker.Parsers.RouteList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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

		recyclerView = (RecyclerView) findViewById(R.id.routeRV);
		adapter = new RouteListAdapter(getApplicationContext(), routes);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(llm);
		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
			@Override
			public void onClick(View view, int position) {
				launchPredictions(routes.get(position).getTitle().substring(0,3));
			}

			@Override
			public void onLongClick(View view, int position) {
				launchPredictions(routes.get(position).getTitle().substring(0,3));
			}
		}));
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

	public void launchPredictions(String route) {
		Intent intent = new Intent(this, PredictionsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("route", route);
		intent.putExtras(bundle);
		startActivity(intent);
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
			adapter.notifyDataSetChanged();
		}
	}

	class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

		private GestureDetector gestureDetector;
		private ClickListener clickListener;

		public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
			this.clickListener = clickListener;
			gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
//					return super.onSingleTapUp(e);
					return true;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if(child!=null && clickListener!=null) {
						clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
					}
					super.onLongPress(e);
				}
			});
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
			View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
			if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)) {
				clickListener.onClick(child, rv.getChildPosition(child));
			}
			Log.d(TAG, "onInterceptTouchEvent" + gestureDetector.onTouchEvent(e) + e);
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {
			Log.d(TAG, "onTouchEvent" + e);

		}

		@Override
		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

		}
	}

	public static interface ClickListener {
		public void onClick(View view, int position);
		public void onLongClick(View view, int position);
	}
}
