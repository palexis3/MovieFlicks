package com.example.palexis3.movieflicks.Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Models.NearbyMovies;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NearbyMoviesRecyclerAdapter extends RecyclerView.Adapter<NearbyMoviesRecyclerAdapter.ViewHolder> {

    private ArrayList<NearbyMovies> nearbyMoviesArrayList;
    private Context context;

    // adapter constructor
    public NearbyMoviesRecyclerAdapter(Context context, ArrayList<NearbyMovies> nearbyMoviesArrayList) {
        this.context = context;
        this.nearbyMoviesArrayList = nearbyMoviesArrayList;
    }


    /** TODO: Construct cardview for nearby movies and then have 'more info' textview spawn off the nearby movie detail activity */

    // construct view holder for row layout performance
    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView movieImage;
        TextView title;
        TextView showtimes;
        TextView moreInfo;
        TextView rating;

        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cv);
            movieImage = (ImageView) view.findViewById(R.id.ivCardViewImage);
            title = (TextView) view.findViewById(R.id.tvCardViewTitle);
            showtimes = (TextView) view.findViewById(R.id.tvCardViewNumberOfShowtimes);
            moreInfo = (TextView) view.findViewById(R.id.tvCardViewMoreInfo);
            rating = (TextView) view.findViewById(R.id.tvCardViewRating);
        }

    }

    // inflate row layout (cardview_nearby_movie) xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_nearby_movie, parent, false);
       ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    // get the number of items in the nearby_movie_list
    @Override
    public int getItemCount() {
        return nearbyMoviesArrayList != null ? nearbyMoviesArrayList.size() : 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final NearbyMovies movie = nearbyMoviesArrayList.get(position);

        // set the title
        TextView title = holder.title;
        title.setText(movie.getTitle());

        // set the movie rating (PG-13, R, etc)
        TextView rating = holder.rating;
        rating.setText(movie.getMovieRating());

        TextView showtimes = holder.showtimes;
        String res = String.format("%d show times near you", movie.getShowtimesList().size());
        showtimes.setText(res);

        ImageView image = holder.movieImage;
        image.setImageResource(0);

        // FOR THE TIME BEING, PLACE THE 'NOT FOUND' image, must figure out how to get actual image
        if(movie.getImagePath().equals("N/A")) {
            Picasso.with(context).load(R.drawable.not_found).placeholder(R.drawable.loading_icon).into(image);
        } else {
            Picasso.with(context).load(movie.getImagePath()).placeholder(R.drawable.loading_icon).into(image);
        }

        // attach listener to 'more info' to launch nearby movies detail activity
        TextView moreInfo = holder.moreInfo;
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Get the nearby movies detail activity", Toast.LENGTH_LONG).show();
            }
        });
    }
}