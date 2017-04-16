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

import com.example.palexis3.movieflicks.Models.Movie;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.palexis3.movieflicks.R.id.tvOverView;
import static com.example.palexis3.movieflicks.R.id.tvTitle;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final static int AVERAGE_MOVIE = 0;
    private final static int POPULAR_MOVIE = 1;
    private final static int ALL_RATINGS_LENGTH = 2;

    private Context context; // used to determine current activity
    private List<Movie> movies; // get list of all movies

    // view holder cache class
    private static class ViewHolder {
        TextView title;
        TextView overView;
        ImageView image;
        ImageView popularImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
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

    // override arrayAdapter getView to populate movie items
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the data item for this position
        Movie movie = getItem(position);

        // holds the view type
        int viewType = 0;

        // holds view holder object
        ViewHolder viewHolder;

        // check to see if the existing view is being reused
        if(convertView == null) {

            // get the view type
            viewType = getItemViewType(position);

            // no view to reuse, so create a brand new view for row
            viewHolder = new ViewHolder();

            convertView = getInflatedLayoutForType(viewType);

            // cache the viewholder object within the view
            convertView.setTag(viewHolder);

        } else {
            // there is an existing view to be recycled
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get the current orientation to determine the appropriate image
        int orientation = context.getResources().getConfiguration().orientation;
        String imagePath = null;

        // populating the items for the average views
        if(viewType == AVERAGE_MOVIE) {

            // find specific views
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overView = (TextView) convertView.findViewById(R.id.tvOverView);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);

            // clear out image view
            viewHolder.image.setImageResource(0);

            // populate the text views
            viewHolder.title.setText(movie.getOriginalTitle());
            viewHolder.overView.setText(movie.getOverView());

            // determine correct path based on orientation
            imagePath = (orientation == Configuration.ORIENTATION_PORTRAIT) ?  movie.getPosterPath() : movie.getBackdropPath();

            // populate image using picasso
            Picasso.with(getContext()).load(imagePath).into(viewHolder.image);

        } else {
            // populating popular views
            viewHolder.popularImage = (ImageView) convertView.findViewById(R.id.ivPopularMovieImage);

            imagePath = movie.getBackdropPath();

            //clear out image view
            viewHolder.popularImage.setImageResource(0);

            Picasso.with(getContext()).load(imagePath).into(viewHolder.popularImage);

        }

        // return the view
        return convertView;
    }
}
