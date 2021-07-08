package com.example.imdbclone.fragments;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.imdbclone.Adapters.FavActorAdapter;
import com.example.imdbclone.Adapters.UserRatingsAdapter;
import com.example.imdbclone.Adapters.WatchlistAdapter;
import com.example.imdbclone.Adapters.movieAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.Views.MainActivity;
import com.example.imdbclone.Views.detailedActorActivity;
import com.example.imdbclone.Views.detailedMovieActivity;
import com.example.imdbclone.model.FavActor;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.MovieWatchlist;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.example.imdbclone.viewModels.menuViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class profileFragment extends Fragment implements FavActorAdapter.FavActorClickListener, WatchlistAdapter.WatchlistTouchListener, UserRatingsAdapter.RatingClicked{

    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private UserRatingsAdapter userRatingsAdapter;
    private WatchlistAdapter watchlistAdapter;
    private FavActorAdapter favActorAdapter;
    private RecyclerView favoriteActorsRec;
    private RecyclerView watchListRecView;
    private RecyclerView ratingsRecView;
    private menuViewModel viewModel;
    private ImageView imageView;
    private TextView textView;
    private User cUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if(checkUser())return view;
        viewModel = ViewModelProviders.of(this).get(menuViewModel.class);
        viewModel.init();
        setUpViews(view);
        setUpRecView();
        observe(view);
        return view;
    }

    private void setUpRecView() {
        //Adapters
        userRatingsAdapter = new UserRatingsAdapter(viewModel.getRatingData().getValue(),getActivity().getApplicationContext(),this);
        watchlistAdapter = new WatchlistAdapter(viewModel.getWatchlistData().getValue(),getActivity().getApplicationContext(),this);
        favActorAdapter = new FavActorAdapter(viewModel.getFavActorsData().getValue(),getActivity().getApplicationContext(),this);
        //layouts
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager wLlinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager rlinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rlinearLayoutManager.setReverseLayout(true);
        rlinearLayoutManager.setStackFromEnd(true);
        //setingUpLayouts
        ratingsRecView.setLayoutManager(rlinearLayoutManager);
        watchListRecView.setLayoutManager(wLlinearLayoutManager);
        favoriteActorsRec.setLayoutManager(linearLayoutManager);
        //setingUpAdapters
        favoriteActorsRec.setAdapter(favActorAdapter);
        ratingsRecView.setAdapter(userRatingsAdapter);
        watchListRecView.setAdapter(watchlistAdapter);
    }

    private void setUpViews(View view) {
        textView = view.findViewById(R.id.userName);
        imageView = view. findViewById(R.id.userImage);
        favoriteActorsRec = view.findViewById(R.id.favActorsRec);
        watchListRecView = view.findViewById(R.id.watchListRecView);
        ratingsRecView = view.findViewById(R.id.userRatingRec);
    }

    private void observe(View view) {
        viewModel.getcUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                cUser = user;
                setValues(view);
            }
        });
        viewModel.getFavActorsData().observe(this, new Observer<ArrayList<FavActor>>() {
            @Override
            public void onChanged(ArrayList<FavActor> favActors) {
                favActorAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getWatchlistData().observe(this, new Observer<ArrayList<MovieWatchlist>>() {
            @Override
            public void onChanged(ArrayList<MovieWatchlist> movieWatchlists) {
                watchlistAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getRatingData().observe(this, new Observer<ArrayList<Rating>>() {
            @Override
            public void onChanged(ArrayList<Rating> ratings) {
                userRatingsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setValues(View view) {
        try{
            RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(45));
            Context context = view.getContext();
            if(cUser.getImageUrl().equals("default"))     {
                String imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/60/No_Picture.jpg";
                Glide.with(context)
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);
            }else {
                Glide.with(context)
                        .load(cUser.getImageUrl())
                        .apply(options)
                        .into(imageView);
            }
            textView.setText(cUser.getName());
        }catch (Exception e){
        }
    }

    private boolean checkUser() {
        if(user==null){
            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void favActorClicked(int position) {
        FavActor actor = viewModel.getFavActorsData().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), detailedActorActivity.class);
        intent.putExtra("id",actor.getId());
        startActivity(intent);
    }

    @Override
    public void watchlistClicked(int position) {
        MovieWatchlist result = viewModel.getWatchlistData().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), detailedMovieActivity.class);
        intent.putExtra("id",result.getMovieId());
        startActivity(intent);
    }

    @Override
    public void ratingClicked(int position) {
        Rating rating = viewModel.getRatingData().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), detailedMovieActivity.class);
        intent.putExtra("id",rating.getMovieId());
        startActivity(intent);
    }
}