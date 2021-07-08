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
import android.widget.TextView;
import android.widget.Toast;

import com.example.imdbclone.Adapters.SearchAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.viewModels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.TouchListener {

    private SearchViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private String incomingTitle;
    private EditText searcBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpStuff();
        observe();
    }

    private void setUpStuff() {
        incomingTitle = getIntent().getStringExtra("title");
        searcBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.searchRecView);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.init(incomingTitle);
        adapter = new SearchAdapter(this, viewModel.getSearchData().getValue(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        searcBar.setOnEditorActionListener(editorActionListener);
    }

    private void observe() {
        viewModel.getSearchData().observe(this, new Observer<List<MovieResults.Result>>() {
            @Override
            public void onChanged(List<MovieResults.Result> results) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String title = searcBar.getText().toString().trim();
            if(TextUtils.isEmpty(title))return true;
            viewModel.searchAgain(title);
            return true;
        }
    };

    @Override
    public void noteClicked(int position) {
        MovieResults.Result result = viewModel.getSearchData().getValue().get(position);
        Intent intent = new Intent(getApplicationContext(),detailedMovieActivity.class);
        intent.putExtra("id",result.getId().toString());
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
    }
}