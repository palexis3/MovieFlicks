package com.example.palexis3.movieflicks.Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.palexis3.movieflicks.Models.Showtimes;
import com.example.palexis3.movieflicks.R;
import com.example.palexis3.movieflicks.Utilities;

import java.util.ArrayList;

public class ShowtimesRecyclerAdapter extends RecyclerView.Adapter<ShowtimesRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Showtimes> showtimesArrayList;

    public ShowtimesRecyclerAdapter(Context context, ArrayList<Showtimes> showtimesArrayList) {
        this.context = context;
        this.showtimesArrayList = showtimesArrayList;
    }

    // get the amount of items in array list
    @Override
    public int getItemCount() {
       return (showtimesArrayList != null) ? showtimesArrayList.size() : 0;
    }

    // construct view holder for row layout performance
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView theater;
        TextView date;
        TextView specs;
        TextView tickets;

        public ViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.cvShowtime);
            theater = (TextView) v.findViewById(R.id.tvShowtimeTheater);
            date = (TextView) v.findViewById(R.id.tvShowtimeDate);
            specs = (TextView) v.findViewById(R.id.tvShowtimeSpecs);
            tickets = (TextView) v.findViewById(R.id.tvShowtimeTicketUri);
        }

    }

    // inflate row layout (cardview_showtime) xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_showtime, parent, false);
       ViewHolder vh = new ViewHolder(view);

       return vh;
    }


    @Override
    public void onBindViewHolder(ShowtimesRecyclerAdapter.ViewHolder holder, int position) {

        final Showtimes showtime = showtimesArrayList.get(position);

        // set the title
        TextView title = holder.theater;
        title.setText(showtime.getTheaterName());

        // set the date
        TextView date = holder.date;
        date.setText(String.format("%s %s", showtime.getDate(), showtime.getTime()));

        // set the specs
        TextView specs = holder.specs;
        specs.setText(Utilities.convertToString(showtime.getSpecs()));

        // set the ticket uri
        TextView ticketUri = holder.tickets;
        ticketUri.setText(showtime.getTicketUri());
    }

}
