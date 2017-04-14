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

    private Context context; // used to determine current activity

    // view holder cache class
    private static class ViewHolder {
        TextView title;
        TextView overView;
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
        this.context = context;
    }

    // override arrayAdapter getView to populate movie items
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the data item for this position
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        // check to see if the existing view is being reused
        if(convertView == null) {

            // no view to reuse, so create a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            // find specific views
            viewHolder.title = (TextView) convertView.findViewById(tvTitle);
            viewHolder.overView = (TextView) convertView.findViewById(tvOverView);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);

            // cache the viewholder object within the view
            convertView.setTag(viewHolder);

        } else {
            // there is an existing view to be recycled
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear out image view
        viewHolder.image.setImageResource(0);

        // populate the text views
        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overView.setText(movie.getOverView());

        // get the current orientation to determine the appropriate image
        int orientation = context.getResources().getConfiguration().orientation;
        String imagePath = null;

        // determine correct path based on orientation
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            imagePath = movie.getPosterPath();
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imagePath = movie.getBackdropPath();
        }

        // populate image using picasso
        Picasso.with(getContext()).load(imagePath).into(viewHolder.image);

        // return the view
        return convertView;
    }
}
