package com.aquamorph.ecubustracker;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aquamorph.ecubustracker.Models.Predictions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	EditText ed1;

	private String TAG = "MainActivity";
	public static String URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	private HandleXML obj;
	private RouteList obj2;
	private StopInfo obj3;
	private RecyclerView recyclerView;
	private PredictionAdapter adapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	ArrayList<Predictions> test = new ArrayList<>();
	Button b1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b1 = (Button) findViewById(R.id.button);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

		recyclerView = (RecyclerView) findViewById(R.id.rv);
		adapter = new PredictionAdapter(getApplicationContext(), test);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(llm);

		ed1 = (EditText) findViewById(R.id.editText);

		ed1.setText("508");

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}
		});

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Refresh items
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void refresh() {
		String route = ed1.getText().toString();
//				ed2.setText(route);

		obj = new HandleXML(route);
		obj.fetchXML();

		obj2 = new RouteList();
		obj2.fetchXML();

		obj3 = new StopInfo(route);
		obj3.fetchXML();

		//Lists all info about a stop
		while (obj.parsingComplete) ;
//				ed2.setText(obj.getRouteTag());
//				ed3.setText(obj.getRouteTitle());
//				ed4.setText(obj.getStopTitle());
//				ed5.setText(obj.getStopTag());
//				tv1.setText("");

		for (int i = 0; i < obj.getPredictions().size(); i++) {
//					tv1.setText(tv1.getText() + " " + obj.getPredictions().get(i).getSeconds() + " " + obj.getPredictions().get(i).getMinutes());
		}

		//Lists all routes
//				while (obj2.parsingComplete) ;
//				for(int i = 0; i < obj2.getRoutes().size(); i++) {
//					tv1.setText(tv1.getText() + " " + obj2.getRoutes().get(i).getTitle() + " " + obj2.getRoutes().get(i).getTag());
//				}

		//Lists all stops
		while (obj3.parsingComplete) ;
		for (int i = 0; i < obj3.getStops().size(); i++) {
//					tv1.setText(tv1.getText() + " " + obj3.getStops().get(i).getTitle() + " " + obj3.getStops().get(i).getStopId());
		}
		test.clear();
		test.addAll(obj.getPredictions());

		adapter.notifyDataSetChanged();
		mSwipeRefreshLayout.setRefreshing(false);

	}
}
