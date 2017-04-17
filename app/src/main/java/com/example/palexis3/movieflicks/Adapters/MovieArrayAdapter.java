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

    // view holder cache class for average movies (using Butterknife for view binding)
    static class ViewHolderAverage {
        @BindView(R.id.tvTitle) TextView title;
        @BindView(R.id.tvOverView) TextView overView;
        @BindView(R.id.ivMovieImage) ImageView image;

        public ViewHolderAverage(View v) {
            ButterKnife.bind(this, v);
        }
    }

    // view holder cache class for popular movies (using Butterknife for view binding)
    static class ViewHolderPopular {
        @BindView(R.id.ivPopularMovieImage) ImageView popularImage;

        public ViewHolderPopular(View v) {
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
        // Return an integer here representing the type of View.
        Movie movie = movies.get(position);
        return movie.getRating() <= 5 ? AVERAGE_MOVIE : POPULAR_MOVIE;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
       switch (type) {
           case AVERAGE_MOVIE:
               return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
           case POPULAR_MOVIE:
               return LayoutInflater.from(getContext()).inflate(R.layout.popular_item_movie, null);
           default:
               return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
       }
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
        View v = convertView;;

        // use a switch statement to determine the two types of movies based on ratings:
        // average and popular
        switch(viewType) {

            case AVERAGE_MOVIE:

                ViewHolderAverage holderAverage;

                if(v == null) {

                    // get the inflated layout for average movies
                    v = getInflatedLayoutForType(viewType);

                    // no view to reuse, so create a brand new view for row
                    holderAverage = new ViewHolderAverage(v);

                    // cache average view holder within parent view
                    v.setTag(holderAverage);

                } else {
                    // get the cached view holder object
                    holderAverage = (ViewHolderAverage) v.getTag();
                }

                // clear out image view
                holderAverage.image.setImageResource(0);

                // populate the text views
                holderAverage.title.setText(movie.getOriginalTitle());
                holderAverage.overView.setText(movie.getOverView());

                // determine correct path based on orientation
                imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ?  movie.getPosterPath() : movie.getBackdropPath();

                // populate image using picasso
                Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).into(holderAverage.image);

                // return created view
                return v;

            case POPULAR_MOVIE:

                ViewHolderPopular viewHolderPopular;

                if(v == null) {

                    // get the inflated view for popular movies
                    v = getInflatedLayoutForType(viewType);

                    // create new view holder object for popular movies
                    viewHolderPopular = new ViewHolderPopular(v);

                    // cache popular view holder within parent view
                    v.setTag(viewHolderPopular);
                } else {
                    // get the cached popular view holder
                    viewHolderPopular = (ViewHolderPopular) v.getTag();
                }

                imagePath = movie.getBackdropPath();

                //clear out image view
                viewHolderPopular.popularImage.setImageResource(0);

                Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).into(viewHolderPopular.popularImage);

                return v;


            default:
                // error: could not get the proper view type
                Toast.makeText(context, "Could not get proper view in movie array adapter", Toast.LENGTH_LONG).show();
                return convertView;
        }

    }
}
