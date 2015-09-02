package com.aquamorph.ecubustracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aquamorph.ecubustracker.Models.Predictions;

import java.util.ArrayList;

import static android.view.LayoutInflater.from;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.MyViewHolder> {

	private String TAG = "PredictionAdapter";
	private ArrayList<Predictions> data;
	private LayoutInflater inflater;

	public PredictionAdapter(Context context,ArrayList<Predictions> data) {
		inflater = from(context);
		Log.i(TAG, "Running Adaoter");
		this.data = data;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.busestimate, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Predictions current = data.get(position);
		holder.seconds.setText(Integer.toString(current.getSeconds()));
//		Log.i(TAG,"Seconds: " + current.getSeconds());
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		protected TextView seconds;

		public MyViewHolder(View itemView) {
			super(itemView);
			seconds = (TextView) itemView.findViewById(R.id.seconds);
		}
	}
}
