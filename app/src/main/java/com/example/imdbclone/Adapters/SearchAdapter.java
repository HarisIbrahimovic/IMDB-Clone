package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.Views.SearchActivity;
import com.example.imdbclone.databinding.ActorItemBinding;
import com.example.imdbclone.databinding.SearchItemBinding;
import com.example.imdbclone.model.MovieResults;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<MovieResults.Result> movies;
    private TouchListener touchListener;
    private Context context;

    public SearchAdapter(Context context, List<MovieResults.Result> movies, TouchListener touchListener) {
        this.context = context;
        this.movies = movies;
        this.touchListener = touchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        SearchItemBinding searchItemBinding = SearchItemBinding.inflate(layoutInflater,parent,false);
        return  new ViewHolder(searchItemBinding,touchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResults.Result result = movies.get(position);
        holder.searchItemBinding.setMovie(result);
        holder.searchItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SearchItemBinding searchItemBinding;
        public ViewHolder(@NonNull SearchItemBinding searchItemBinding,TouchListener listener) {
            super(searchItemBinding.getRoot());
            this.searchItemBinding=searchItemBinding;
            this.searchItemBinding.getRoot().setOnClickListener(this);
            touchListener = listener;
        }

        @Override
        public void onClick(View v) {
            touchListener.noteClicked(getAdapterPosition());
        }
    }

    public interface TouchListener{
        void noteClicked(int position);
    }
}
