package com.example.imdbclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imdbclone.Adapters.movieAdapter;
import com.example.imdbclone.Adapters.newMoviesAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.Views.detailedMovieActivity;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.viewModels.menuViewModel;

import java.util.List;

public class searchFragment extends Fragment implements movieAdapter.TouchListener,newMoviesAdapter.TouchListener{

    private menuViewModel viewModel;
    private RecyclerView upcomingRecView;
    private RecyclerView topRatedRecView;
    private movieAdapter topRatedAdapter;
    private newMoviesAdapter upcomingAdapter;
    private List<MovieResults.Result> topRatedList;
    private List<MovieResults.Result> upcomingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setUpStuff(view);
        observe();
        setUpRecView();
        return  view ;
    }

    private void setUpStuff(View view) {
        topRatedRecView = view.findViewById(R.id.topRatedRecycler);
        upcomingRecView = view.findViewById(R.id.upcomingRecView);
        viewModel = ViewModelProviders.of(this).get(menuViewModel.class);
        viewModel.init();
        topRatedList = viewModel.getTopRatedMovies().getValue();
        upcomingList = viewModel.getUpcomingMovies().getValue();
    }

    private void setUpRecView() {
        LinearLayoutManager topRatedManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager upcomingManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        topRatedAdapter = new movieAdapter(topRatedList,getActivity().getApplicationContext(),0,this);
        upcomingAdapter = new newMoviesAdapter(upcomingList,getActivity().getApplicationContext(),this);
        upcomingRecView.setLayoutManager(upcomingManager);
        topRatedRecView.setLayoutManager(topRatedManager);
        topRatedRecView.setAdapter(topRatedAdapter);
        upcomingRecView.setAdapter(upcomingAdapter);
    }

    private void observe() {
        viewModel.getTopRatedMovies().observe(this, new Observer<List<MovieResults.Result>>() {
            @Override
            public void onChanged(List<MovieResults.Result> results) {
                topRatedAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getUpcomingMovies().observe(this, new Observer<List<MovieResults.Result>>() {
            @Override
            public void onChanged(List<MovieResults.Result> results) {
                upcomingAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        MovieResults.Result result = viewModel.getTopRatedMovies().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(),detailedMovieActivity.class);
        intent.putExtra("id",result.getId().toString());
        startActivity(intent);
    }

    @Override
    public void noteClicked(int position) {
        MovieResults.Result result = viewModel.getUpcomingMovies().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), detailedMovieActivity.class);
        intent.putExtra("id",result.getId().toString());
        startActivity(intent);
    }
}