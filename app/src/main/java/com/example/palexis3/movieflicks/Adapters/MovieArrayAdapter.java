package com.example.palexis3.movieflicks.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final static int AVERAGE_MOVIE = 0;
    private final static int POPULAR_MOVIE = 1;
    private final static int ALL_RATINGS_LENGTH = 2;

    private Context context; // used to determine current activity
    private List<Movie> movies; // get list of all movies


    // view holder cache for movies (using butterknife for view binding)
    /**
     * Nullable annotation is used for the case when dealing with popular and average movies,
       some items will be null in both cases
    */
    static class MovieViewHolder {
        @Nullable @BindView(R.id.tvTitle) TextView title;
        @Nullable @BindView(R.id.tvOverView) TextView overView;
        @Nullable @BindView(R.id.ivMovieImage) ImageView image;
        @Nullable @BindView(R.id.ivPopularMovieImage) ImageView popularImage;
        @Nullable @BindView(R.id.tvLowerPopularMovieTitle) TextView popularMovieTitle;

        public MovieViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return ALL_RATINGS_LENGTH;
    }


    // Get the type of movie View that will be created based on rating
    @Override
    public int getItemViewType(int position) {
        return POPULAR_MOVIE;
        /** removing two different heterogenous views
        // Return an integer here representing the type of View.
        Movie movie = movies.get(position);
        return movie.getRating() <= 5 ? AVERAGE_MOVIE : POPULAR_MOVIE;
        */
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        return LayoutInflater.from(getContext()).inflate(R.layout.popular_item_movie, null);
       /** removing type switch statement
       switch (type) {
           case AVERAGE_MOVIE:
               return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
           case POPULAR_MOVIE:
               return LayoutInflater.from(getContext()).inflate(R.layout.popular_item_movie, null);
           default:
               return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
       }
       */
    }

    // override arrayAdapter getView to populate movie items based on ratings
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the view type
        int viewType = getItemViewType(position);

        // get the data item for this position
        final Movie movie = getItem(position);

        // get the current orientation to determine the appropriate image
        int orientation = context.getResources().getConfiguration().orientation;
        String imagePath;

        // will hold parent view
        View v = convertView;

        // view holder object
        MovieViewHolder movieViewHolder;

        if(v == null) {
            // get the inflated view type
            v = getInflatedLayoutForType(viewType);

            // no view to reuse, so create a brand new view for row
            movieViewHolder = new MovieViewHolder(v);

            // cached view holder object within parent view
            v.setTag(movieViewHolder);

        } else {
            // get the cached view holder object
            movieViewHolder = (MovieViewHolder) v.getTag();
        }

        // use a switch statement to populate relevant movie info based on ratings:
        // average and popular
        switch(viewType) {

            case AVERAGE_MOVIE:

                // clear out image view
                movieViewHolder.image.setImageResource(0);

                // populate the text views
                movieViewHolder.title.setText(movie.getOriginalTitle());
                movieViewHolder.overView.setText(movie.getOverView());

                // determine correct path based on orientation
                imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ?  movie.getPosterPath() : movie.getBackdropPath();

                // populate image using picasso
                Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).into(movieViewHolder.image);

                // return created view
                return v;

            case POPULAR_MOVIE:

                // backdrop movie pic will be used for popular movies
                imagePath = movie.getBackdropPath();

                // set the title for the movie
                movieViewHolder.popularMovieTitle.setText(movie.getOriginalTitle());

                // clear out image view
                movieViewHolder.popularImage.setImageResource(0);

                Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).into(movieViewHolder.popularImage);

                return v;

            default:
                // error: could not get the proper view type
                Toast.makeText(context, "Could not get proper view in movie array adapter", Toast.LENGTH_LONG).show();
                return convertView;
        }

    }
}
