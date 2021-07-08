package com.example.imdbclone.Views;
import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imdbclone.Adapters.actorAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.databinding.ActivityDetailedMovieBinding;
import com.example.imdbclone.model.Actors;
import com.example.imdbclone.model.SingleMovie;
import com.example.imdbclone.utils.DoubleClickListener;
import com.example.imdbclone.viewModels.DetailsViewModel;
import com.pedromassango.doubleclick.DoubleClick;

import java.util.List;

public class detailedMovieActivity extends AppCompatActivity implements actorAdapter.TouchListener {

    private ActivityDetailedMovieBinding mBinding;
    private DetailsViewModel viewModel;
    private actorAdapter ActorAdapter;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private TextView movieName;
    private SingleMovie sMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detailed_movie);
        setUpStuff();
        onClicks();
        setUpRecView();
        observe();
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(viewModel);
    }

    private void setUpStuff() {
        viewModel=  ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.init(getIntent().getStringExtra("id"));
        linearLayout = findViewById(R.id.ratings);
        sMovie = viewModel.getMovie().getValue();
        imageView = findViewById(R.id.detailedMovieImage);
        movieName = findViewById(R.id.detailedMovieName);
    }

    private void onClicks() {
        movieName.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                if(viewModel.addToWatchlist(String.valueOf(sMovie.getId()),String.valueOf(sMovie.getVoteAverage()),sMovie.getPosterPath()))
                    Toast.makeText(detailedMovieActivity.this,"Added to favorites.",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(detailedMovieActivity.this,"Please login",Toast.LENGTH_SHORT);
            }
        });

        imageView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick() {
                String url = "https://www.youtube.com/results?search_query="+sMovie.getTitle();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(detailedMovieActivity.this, RatingActivity.class);
                i2.putExtra("movieId",String.valueOf(sMovie.getId()));
                i2.putExtra("posterPath",sMovie.getPosterPath());
                i2.putExtra("movieName",sMovie.getTitle());
                startActivity(i2);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
    }


    private void setUpRecView() {
        ActorAdapter = new actorAdapter(viewModel.getCast().getValue(),getApplicationContext(),this);
        LinearLayoutManager newLinearLayoutManager =
                new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        mBinding.actorRecView.setLayoutManager(newLinearLayoutManager);
        mBinding.actorRecView.setAdapter(ActorAdapter);
    }

    private void observe() {
        viewModel.getCast().observe(this, new Observer<List<Actors.Cast>>() {
            @Override
            public void onChanged(List<Actors.Cast> casts) {
                ActorAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getMovie().observe(this, new Observer<SingleMovie>() {
            @Override
            public void onChanged(SingleMovie singleMovie) {
                sMovie=singleMovie;
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        Actors.Cast actor = viewModel.getCast().getValue().get(position);
        Intent intent = new Intent(getApplicationContext(),detailedActorActivity.class);
        intent.putExtra("id",actor.getId().toString());
        startActivity(intent);
    }

}