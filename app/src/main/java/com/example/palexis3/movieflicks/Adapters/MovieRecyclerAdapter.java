package com.example.palexis3.movieflicks.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private List<Movie> movies; // get list of all movies
    private Context context;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView movieImage;
        TextView title;
        TextView rating;
        TextView info;

        public MovieViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cv);
            movieImage = (ImageView) view.findViewById(R.id.ivCardViewMovieImage);
            title = (TextView) view.findViewById(R.id.tvCardViewMovieTitle);
            rating = (TextView) view.findViewById(R.id.tvCardViewRating);
            info = (TextView) view.findViewById(R.id.tvCardViewMoreInfo);
        }

    }

    public MovieRecyclerAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_cardview, parent, false);
        MovieViewHolder mvh = new MovieViewHolder(v);
        return mvh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movies.get(position);

        // backdrop movie pic will be used for popular movies
        String imagePath = movie.getBackdropPath();

        TextView tvTilte =  holder.title;
        // set the title for the movie
        tvTilte.setText(movie.getOriginalTitle());

        TextView tvRating = holder.rating;
        // set the rating for this movie
        tvRating.setText(movie.getRating());

        ImageView ivMovie = holder.movieImage;
        // clear out image view
        ivMovie.setImageResource(0);

        Picasso.with(context).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).into(ivMovie);
    }
    

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
