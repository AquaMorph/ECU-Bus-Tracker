package com.aquamorph.ecubustracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aquamorph.ecubustracker.Models.Routes;

import java.util.ArrayList;

import static android.view.LayoutInflater.from;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.MyViewHolder> {

	private String TAG = "RouteListAdapter";
	private ArrayList<Routes> data;
	private LayoutInflater inflater;
	private Context context;

	public RouteListAdapter(Context context, ArrayList<Routes> data) {
		inflater = from(context);
		this.data = data;
		this.context = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.route_list, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Routes current = data.get(position);
		holder.name.setText(current.getTitle());
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public MyViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.routeName);
		}
	}
}
