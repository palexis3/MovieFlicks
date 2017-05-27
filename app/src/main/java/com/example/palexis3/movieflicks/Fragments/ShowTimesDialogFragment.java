package com.example.palexis3.movieflicks.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.palexis3.movieflicks.Adapters.ShowtimesRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.Showtimes;
import com.example.palexis3.movieflicks.R;

import java.util.ArrayList;

import butterknife.ButterKnife;


/** TODO: Get the showtime arraylist from the nearby movie detail activity through bundle checkout Parcelable, Parcels, Serializeable */

public class ShowTimesDialogFragment extends DialogFragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Showtimes> showtimesArrayList;

    public ShowTimesDialogFragment(){}; // empty constructor

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // inflate the xml for this layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_showtime_list, container, false);
        return v;
    }

    // bind all views
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rvShowsTimes);

        // set up vertical layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        // add showtimes to recycler view
        adapter = new ShowtimesRecyclerAdapter(getActivity(), showtimesArrayList);

        // add adapter to this recycler view
        recyclerView.setAdapter(adapter);
    }
}
