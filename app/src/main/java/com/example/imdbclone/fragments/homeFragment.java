package com.example.imdbclone.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.imdbclone.Adapters.movieAdapter;
import com.example.imdbclone.Adapters.newMoviesAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.Views.SearchActivity;
import com.example.imdbclone.Views.detailedMovieActivity;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.viewModels.menuViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class homeFragment extends Fragment implements movieAdapter.TouchListener, com.example.imdbclone.Adapters.newMoviesAdapter.TouchListener{

    private movieAdapter popularMoviesAdapter;
    private newMoviesAdapter newmoviesadapter;
    private RecyclerView popularRecyclerView;
    private RecyclerView newRecyclerView;
    private FloatingActionButton openTrailer;
    private EditText searchBar;
    private ImageView image;
    private menuViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        setUpStuff(view);
        setUpRecView();
        observe();
        onClicks();
        return view;
    }



    private void setUpStuff(View view) {
        image = view.findViewById(R.id.posterPic);
        popularRecyclerView = view.findViewById(R.id.recViewPopularMovies);
        newRecyclerView = view.findViewById(R.id.recyclerViewNowPlaying);
        openTrailer = view.findViewById(R.id.openTrailer);
        searchBar = view.findViewById(R.id.search_bar);
        viewModel = ViewModelProviders.of(this).get(menuViewModel.class);
        viewModel.init();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms( new CenterCrop(), new RoundedCorners(45));
        Glide.with(getActivity().getApplicationContext())
                .load("https://cdn.pastemagazine.com/www/system/images/photo_albums/best-movie-posters-2016/large/moonlight-ver2-xlg.jpg?1384968217")
                .apply(requestOptions)
                .into(image);

    }

    private void setUpRecView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager newLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        popularMoviesAdapter = new movieAdapter(viewModel.getPopularMovies().getValue(),getActivity().getApplicationContext(),1,this);
        newmoviesadapter = new newMoviesAdapter(viewModel.getNewMovies().getValue(),getActivity().getApplicationContext(),this);
        popularRecyclerView.setLayoutManager(linearLayoutManager);
        newRecyclerView.setLayoutManager(newLinearLayoutManager);
        popularRecyclerView.setAdapter(popularMoviesAdapter);
        newRecyclerView.setAdapter(newmoviesadapter);
    }

    private void observe() {
        viewModel.getPopularMovies().observe(this, new Observer<List<MovieResults.Result>>() {
            @Override
            public void onChanged(List<MovieResults.Result> results) {
                popularMoviesAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getNewMovies().observe(this, new Observer<List<MovieResults.Result>>() {
            @Override
            public void onChanged(List<MovieResults.Result> results) {
                newmoviesadapter.notifyDataSetChanged();
            }
        });
    }


    private void onClicks() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),detailedMovieActivity.class);
                intent.putExtra("id","376867");
                startActivity(intent);
            }
        });
        openTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=9NJj12tJzqc&ab_channel=A24";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        searchBar.setOnEditorActionListener(editorActionListener);
    }

    @Override
    public void onNoteClick(int position) {
        MovieResults.Result result = viewModel.getPopularMovies().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(),detailedMovieActivity.class);
        intent.putExtra("id",result.getId().toString());
        startActivity(intent);
    }

    @Override
    public void noteClicked(int position) {
        MovieResults.Result result = viewModel.getNewMovies().getValue().get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(),detailedMovieActivity.class);
        intent.putExtra("id",result.getId().toString());
        startActivity(intent);
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String title = searchBar.getText().toString().trim();
            Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
            intent.putExtra("title", title);
            if(TextUtils.isEmpty(title))
                return true;
            searchBar.setText("");
            startActivity(intent);
            getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            return true;
        }
    };
}