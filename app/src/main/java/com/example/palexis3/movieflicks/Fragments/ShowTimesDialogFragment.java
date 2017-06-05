package com.example.palexis3.movieflicks.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.palexis3.movieflicks.Adapters.ShowtimesRecyclerAdapter;
import com.example.palexis3.movieflicks.Models.Showtimes;
import com.example.palexis3.movieflicks.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class ShowTimesDialogFragment extends DialogFragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Showtimes> showtimesArrayList;
    private RecyclerView.ItemDecoration itemDecoration;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // getting the show time arraylist passed in
        showtimesArrayList = Parcels.unwrap(getArguments().getParcelable("showtimes"));

        // get the title
        title = getArguments().getString("title");
    }

    public ShowTimesDialogFragment(){} // empty constructor


    public static ShowTimesDialogFragment newInstance(ArrayList<Showtimes> showtimesArrayList, String title) {

        ShowTimesDialogFragment frag = new ShowTimesDialogFragment();

        // supply showtime list as bundle argument
        Bundle bundle = new Bundle();
        bundle.putParcelable("showtimes", Parcels.wrap(showtimesArrayList));
        bundle.putString("title", title);
        frag.setArguments(bundle);

        return frag;
    }

    // inflate the xml for this layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // setting the title for this dialog
        getDialog().setTitle(title);

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

        // add item decoration
        itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        // add showtimes to recycler view
        adapter = new ShowtimesRecyclerAdapter(getActivity(), showtimesArrayList);

        // add adapter to this recycler view
        recyclerView.setAdapter(adapter);
    }
}
