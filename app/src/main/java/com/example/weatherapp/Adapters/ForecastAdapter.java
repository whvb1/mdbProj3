package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Models.weather;
import com.example.weatherapp.R;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.weatherViewHolder> {

    private ArrayList<weather> weathers;

    // Provide a suitable constructor for the adapter. This would be a good
    // place to have the dataset be initialized.
    public ForecastAdapter(ArrayList<weather> weathers) {
        this.weathers = weathers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ForecastAdapter.weatherViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Get the context from parent and then make an inflater from this context.
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout of a individual cell.
        View noteView = inflater.inflate(R.layout.forecast_row, parent, false);

        // Return a new holder instance
        weatherViewHolder viewHolder = new weatherViewHolder(noteView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final weatherViewHolder holder, final int position) {
        // Get an element from your dataset at this position
        // Replace the contents of the view with that element
        holder.txtName_small.setText(weathers.get(position).getName());
        holder.txtHigh_small.setText(Integer.toString(weathers.get(position).getTemperatureHigh()));
        holder.txtLow_small.setText(Integer.toString(weathers.get(position).getTemperatureLow()));
        //holder.txtHigh_small.setText(Integer.toString(weathers.get(position).getTemperature()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return weathers.size();
    }

    // Set the data to be something totally new.
    public void setData(ArrayList<weather> weathers) {
        this.weathers = weathers;
        // This line is very important. The recycler view will not visually update until
        // call this notifyDataSetChanged.
        this.notifyDataSetChanged();
    }

    public static class weatherViewHolder extends RecyclerView.ViewHolder {
        // Initialized all the UI components of an individual row.
        TextView txtName_small;
        Button btnEdit;
        TextView txtTemp_small;
        TextView txtHigh_small, txtLow_small;

        public weatherViewHolder(View rowView) {
            super(rowView);
            txtName_small = rowView.findViewById(R.id.txtName_small);
            txtHigh_small = rowView.findViewById(R.id.txtHigh_small);
            txtLow_small = rowView.findViewById(R.id.txtLow_small);
        }
    }
}