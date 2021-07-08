package com.example.imdbclone.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.imdbclone.Adapters.ActorCreditAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.databinding.ActivityDetailedActorBinding;
import com.example.imdbclone.databinding.ActivityDetailedMovieBinding;
import com.example.imdbclone.model.ActorCredits;
import com.example.imdbclone.model.ActorDetails;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.viewModels.DetailedActorViewModel;

import java.util.List;

public class detailedActorActivity extends AppCompatActivity implements ActorCreditAdapter.TouchListener{

    private ActivityDetailedActorBinding detailedActorBinding;
    private ActorCreditAdapter actorCreditAdapter;
    private RecyclerView recyclerView;
    private String posterPath;
    private TextView textView;
    private String name;
    private DetailedActorViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailedActorBinding = DataBindingUtil.setContentView(this, R.layout.activity_detailed_actor);
        viewModel = ViewModelProviders.of(this).get(DetailedActorViewModel.class);
        viewModel.init(getIntent().getStringExtra("id"));
        textView = findViewById(R.id.addActorToFav);
        onClicks();
        setUpRecView();
        observe();
        detailedActorBinding.setLifecycleOwner(this);
        detailedActorBinding.setViewModel(viewModel);
    }

    private void onClicks() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.addToFav(name,posterPath,getIntent().getStringExtra("id")))
                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecView() {
        recyclerView = findViewById(R.id.creditRecView);
        actorCreditAdapter = new ActorCreditAdapter(viewModel.getCreditData().getValue(),getApplicationContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(actorCreditAdapter);
    }

    private void observe() {
        viewModel.getDetails().observe(this, new Observer<ActorDetails>() {
            @Override
            public void onChanged(ActorDetails actorDetails) {
                name = actorDetails.getName();
                posterPath = actorDetails.getProfilePath();
            }
        });
        viewModel.getCreditData().observe(this, new Observer<List<ActorCredits.Cast>>() {
            @Override
            public void onChanged(List<ActorCredits.Cast> casts) {
                actorCreditAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        ActorCredits.Cast credits = viewModel.getCreditData().getValue().get(position);
        Intent intent = new Intent(getApplicationContext(),detailedMovieActivity.class);
        intent.putExtra("id",credits.getId().toString());
        startActivity(intent);
    }

}