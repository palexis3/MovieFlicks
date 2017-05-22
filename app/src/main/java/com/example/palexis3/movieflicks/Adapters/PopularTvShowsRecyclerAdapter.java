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

import com.example.palexis3.movieflicks.Activities.PopularTvShowsDetailActivity;
import com.example.palexis3.movieflicks.Models.TvShows;
import com.example.palexis3.movieflicks.Networking.MyOkHttpClient;
import com.example.palexis3.movieflicks.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

public class PopularTvShowsRecyclerAdapter extends RecyclerView.Adapter<PopularTvShowsRecyclerAdapter.ViewHolder> {

    private ArrayList<TvShows> tvShowsArrayList; // get list of all tv shows
    private Context context;

    public PopularTvShowsRecyclerAdapter(Context context, ArrayList<TvShows> tvShowsArrayList) {
        this.context = context;
        this.tvShowsArrayList = tvShowsArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView showImage;
        TextView title;
        TextView moreInfo;
        TextView rating;

        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cv);
            showImage = (ImageView) view.findViewById(R.id.ivCardViewImage);
            title = (TextView) view.findViewById(R.id.tvCardViewTitle);
            moreInfo = (TextView) view.findViewById(R.id.tvCardViewMoreInfo);
            rating = (TextView) view.findViewById(R.id.tvCardViewRating);
        }

    }

    // inflate row layout (cardview_movie_or_show) xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_movie_or_show, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    // get the number of items within tvshows list
    @Override
    public int getItemCount() {
        return tvShowsArrayList != null ? tvShowsArrayList.size() : 0;
    }

    // bind items in row layout with tvShow objects variables
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TvShows show = tvShowsArrayList.get(position);

        // set title for show
        TextView title = holder.title;
        title.setText(show.getName());

        // set rating for show
        TextView rating = holder.rating;
        String rating_temp = show.getRating() > 0.0 ? String.valueOf(show.getRating()) : "N/A";
        rating.setText(rating_temp);

        // backdrop image is used for main image
        String imagePath = show.getBackdropPath();
        ImageView showImage = holder.showImage;
        showImage.setImageResource(0);

        Picasso.with(context).load(imagePath).placeholder(R.drawable.loading_icon).into(showImage);

        // attach onClickListener to 'moreInfo' textview to launch Popular tvShows detail
        TextView moreInfo = holder.moreInfo;
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(show != null) {
                    // execute async task to add show details
                   new TvShowCallerTask().execute(show);
               } else {
                   Toast.makeText(context, "Error: Could not get show details!", Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private class TvShowCallerTask extends AsyncTask<TvShows, Void, TvShows> {

        @Override
        protected TvShows doInBackground(TvShows... params) {

            TvShows show = null;

            JSONArray array;
            JSONObject object;

            // create okHttp client
            MyOkHttpClient client = new MyOkHttpClient();

            try{
                show = params[0];
                String id = String.valueOf(show.getId());

                // get the details for this specific movie
                String response = client.getShowDetails(id);
                JSONObject jsonObject = new JSONObject(response);

                // get list of genres
                ArrayList<String> genres = new ArrayList<>();
                array = jsonObject.getJSONArray("genres");
                for(int i = 0; i < array.length(); i++) {
                    try{
                        object = array.getJSONObject(i);
                        genres.add(object.getString("name"));
                    } catch(JSONException e) {
                        Log.d("GENRES-ARRAY-DEBUG", e.getLocalizedMessage());
                    }
                }


                // get list of networks for this movie
                ArrayList<String> networks = new ArrayList<>();
                array = jsonObject.getJSONArray("networks");
                for(int i = 0; i < array.length(); i++) {
                    try{
                        object = array.getJSONObject(i);
                        networks.add(object.getString("name"));
                    } catch(JSONException e) {
                        Log.d("NETWORKS-ARRAY-DEBUG", e.getLocalizedMessage());
                    }
                }

                // get homepage url
                String homepage = jsonObject.getString("homepage");

                // get last air date
                String air_date = jsonObject.getString("last_air_date");

                // get Youtube key
                array = jsonObject.getJSONObject("videos").getJSONArray("results");
                String youtubeKey = null;
                if(array.length() > 0) {
                    youtubeKey = array.getJSONObject(0).getString("key");
                }

                // get number of seasons
                int seasons = jsonObject.getInt("number_of_seasons");

                // get number of episodes
                int episodes = jsonObject.getInt("number_of_episodes");


                show.setGenres(genres);
                show.setNetworks(networks);
                show.setHomepage_url(homepage);
                show.setLastAirDate(air_date);
                show.setYoutubeKey(youtubeKey);
                show.setNumOfSeasons(seasons);
                show.setNumOfEpisodes(episodes);

            } catch (Exception e) {
                Log.d("DEBUG", e.getLocalizedMessage());
            }

            return show;
        }


        @Override
        protected void onPostExecute(TvShows tvShow) {

            // checking if tvshow passed is null or not
            boolean found = true;
            if(tvShow == null) {
                found = false;
            }

            if(found) {
                // call an intent to PopularTvShowsDetail Activity
                Intent i = new Intent(context, PopularTvShowsDetailActivity.class);
                i.putExtra("show", Parcels.wrap(tvShow));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // probably should use Dagger for dependency injection b/c calling intent from adapter
                context.startActivity(i);

            } else {
                Toast.makeText(context, "Error: Could not get show details", Toast.LENGTH_LONG).show();
            }
        }
    }

}
