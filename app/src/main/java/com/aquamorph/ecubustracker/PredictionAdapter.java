package com.aquamorph.ecubustracker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquamorph.ecubustracker.Models.Predictions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.view.LayoutInflater.from;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.MyViewHolder> {

	private String TAG = "PredictionAdapter";
	private ArrayList<Predictions> data;
	private LayoutInflater inflater;
	private Context context;


	public PredictionAdapter(Context context,ArrayList<Predictions> data) {
		inflater = from(context);
		this.data = data;
		this.context = context;
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
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String theTime = sdf.format(new Date(System.currentTimeMillis() + current.getSeconds()*1000));

		holder.time.setText(theTime);
		holder.seconds.setText(Integer.toString(current.getSeconds()));
		holder.minutes.setText(Integer.toString(current.getMinutes()));
		Drawable busicon = ContextCompat.getDrawable(context, R.drawable.bus_icon);
		busicon = busicon.mutate();

		if(current.getMinutes() <= 5) {
			busicon.setColorFilter(0xff4CAF50, PorterDuff.Mode.MULTIPLY);
		} else if(current.getMinutes() <= 15) {
			busicon.setColorFilter(0xffFFC107, PorterDuff.Mode.MULTIPLY);
		} else {
			busicon.setColorFilter(0xffF44336, PorterDuff.Mode.MULTIPLY);
		}
		if(Build.VERSION.SDK_INT >= 16) {
			holder.busicon.setBackground(busicon);
		} else {
			holder.busicon.setImageDrawable(busicon);
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		protected TextView time;
		protected TextView seconds;
		protected TextView minutes;
		protected ImageView busicon;

		public MyViewHolder(View itemView) {
			super(itemView);
			time = (TextView) itemView.findViewById(R.id.time);
			seconds = (TextView) itemView.findViewById(R.id.seconds);
			minutes = (TextView) itemView.findViewById(R.id.minutes);
			busicon = (ImageView) itemView.findViewById(R.id.imageView);
		}
	}
}
