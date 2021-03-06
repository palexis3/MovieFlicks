package com.example.palexis3.movieflicks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.movieflicks.Activities.UpcomingMovieDetailActivity;
import com.example.palexis3.movieflicks.Models.NewMovies;
import com.example.palexis3.movieflicks.Networking.MyOkHttpClient;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

public class UpcomingMovieRecyclerAdapter extends RecyclerView.Adapter<UpcomingMovieRecyclerAdapter.ViewHolder> {

    private List<NewMovies> movies; // get list of all movies
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView movieImage;
        TextView title;
        TextView rating;
        TextView info;

        public ViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.cv);
            movieImage = (ImageView) view.findViewById(R.id.ivCardViewImage);
            title = (TextView) view.findViewById(R.id.tvCardViewTitle);
            rating = (TextView) view.findViewById(R.id.tvCardViewRating);
            info = (TextView) view.findViewById(R.id.tvCardViewMoreInfo);
        }

    }

    public UpcomingMovieRecyclerAdapter(Context context, List<NewMovies> movies) {
        this.movies = movies;
        this.context = context;
    }

    // inflate row layout (cardview_movie_or_show) xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_movie_or_show, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final NewMovies movie = movies.get(position);

        // backdrop movie pic will be used for popular movies
        String imagePath = movie.getBackdropPath();

        TextView tvTitle =  holder.title;
        // set the title for the movie
        tvTitle.setText(movie.getOriginalTitle());

        TextView tvRating = holder.rating;
        String rating = movie.getRating() > 0.0 ? String.valueOf(movie.getRating()) : "N/A";

        // set the rating for this movie
        tvRating.setText(rating);

        ImageView ivMovie = holder.movieImage;

        // clear out image view
        ivMovie.setImageResource(0);

        Picasso.with(context).load(imagePath).placeholder(R.drawable.loading_icon).into(ivMovie);

        // attach click listener to 'movie info' textview to launch movie details activity
        TextView tvInfo = holder.info;
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie != null) {
                    // execute asynctask to get youtube key
                    new YouTubeCallerTask().execute(movie);
                } else {
                    Toast.makeText(context, "Error: Could not get movie details", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    // an async task that gets the youtube key for a movie and then calls the youtube movie details intent
    private class YouTubeCallerTask extends AsyncTask<NewMovies, Void, NewMovies> {

        // get this movie youtube key
        @Override
        protected NewMovies doInBackground(NewMovies... params) {

            NewMovies movie = null;

            // create okhttp client
            MyOkHttpClient client = new MyOkHttpClient();
            String key;

            // getting the key for youtube player for a movie
            try {
                movie = params[0];
                String response = client.getSingleMovie(String.valueOf(movie.getId()));
                JSONObject json = new JSONObject(response);
                key = json.getJSONArray("results").getJSONObject(0).getString("key");
                movie.setYoutubeKey(key); // setting the key for this movie
            } catch (Exception e) {
                Log.d("DEBUG", e.getLocalizedMessage());
            }

            return movie;
        }

        @Override
        protected void onPostExecute(NewMovies movie) {

            boolean found = true;
            if(movie == null) {
                found = false;
            }

            // first check if we can get this movie's detail
            if(found) {
                // launch the movie details activity for movie
                Intent i = new Intent(context, UpcomingMovieDetailActivity.class);
                i.putExtra("movie", Parcels.wrap(movie));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // probably not the best way to start intent, try Dagger
                context.startActivity(i);
            } else {
                Toast.makeText(context, "Error: Could not get movie details", Toast.LENGTH_LONG).show();
            }
        }
    }
}
