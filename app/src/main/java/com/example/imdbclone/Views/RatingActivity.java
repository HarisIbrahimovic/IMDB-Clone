package com.example.imdbclone.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imdbclone.Adapters.RatingAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.viewModels.RatingViewModel;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    private RatingAdapter ratingAdapter;
    private RecyclerView recyclerView;
    private RatingBar ratingBar;
    private EditText editText;
    private RatingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setUpStuff();
        setUpRecView();
        observe();
    }

    private void observe() {
        viewModel.getRatingsData().observe(this, new Observer<ArrayList<Rating>>() {
            @Override
            public void onChanged(ArrayList<Rating> ratings) {
                ratingAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpRecView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ratingAdapter = new RatingAdapter(viewModel.getRatingsData().getValue(),this);
        recyclerView.setAdapter(ratingAdapter);
    }

    private void setUpStuff() {
        viewModel = ViewModelProviders.of(this).get(RatingViewModel.class);
        viewModel.init(getIntent().getStringExtra("movieId"));
        ratingBar = findViewById(R.id.ratingBarRating);
        editText = findViewById(R.id.commentText);
        editText.setOnEditorActionListener(editorActionListener);
        recyclerView = findViewById(R.id.ratingsRecView);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String comment = editText.getText().toString().trim();
            String score = String.valueOf(ratingBar.getRating()*2);
            String movieId = getIntent().getStringExtra("movieId");
            String posterPath = getIntent().getStringExtra("posterPath");
            String movieName = getIntent().getStringExtra("movieName");
            if(viewModel.addRating(posterPath,movieId,movieName,score,comment)) {
               Toast.makeText(getApplicationContext(),"Rating added",Toast.LENGTH_SHORT).show();
            }else Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
