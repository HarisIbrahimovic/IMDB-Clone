package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.WatchlistItemBinding;
import com.example.imdbclone.model.MovieWatchlist;

import java.util.ArrayList;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {
    private ArrayList<MovieWatchlist> movies;
    private Context context;
    private WatchlistTouchListener watchlistTouchListener;

    public WatchlistAdapter(ArrayList<MovieWatchlist> movies, Context context, WatchlistTouchListener watchlistTouchListener) {
        this.movies = movies;
        this.context = context;
        this.watchlistTouchListener = watchlistTouchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        WatchlistItemBinding watchlistItemBinding = WatchlistItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(watchlistItemBinding,watchlistTouchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.watchlistItemBinding.setMovie(movies.get(position));
        holder.watchlistItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        WatchlistTouchListener watchlistTouchListener;
        WatchlistItemBinding watchlistItemBinding;
        public ViewHolder(@NonNull WatchlistItemBinding watchlistItemBinding, WatchlistTouchListener watchlistTouchListener) {
            super(watchlistItemBinding.getRoot());
            this.watchlistItemBinding = watchlistItemBinding;
            this.watchlistTouchListener = watchlistTouchListener;
            this.watchlistItemBinding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            watchlistTouchListener.watchlistClicked(getAdapterPosition());
        }
    }

    public interface WatchlistTouchListener{
        void watchlistClicked(int position);
    }
}
