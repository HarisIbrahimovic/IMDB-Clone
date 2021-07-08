package com.example.imdbclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.NowPlayingItemBinding;
import com.example.imdbclone.model.MovieResults;

import java.util.List;

public class newMoviesAdapter extends RecyclerView.Adapter<newMoviesAdapter.ViewHolder> {

    private List<MovieResults.Result> movies;
    private Context context;
    private TouchListener touchListener;

    public newMoviesAdapter(List<MovieResults.Result> movies, Context context, TouchListener touchListener) {
        this.movies = movies;
        this.context = context;
        this.touchListener = touchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        NowPlayingItemBinding nowPlayingItemBinding = NowPlayingItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(nowPlayingItemBinding,touchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResults.Result result = movies.get(position);
        holder.nowPlayingItemBinding.setMovie(result);
        holder.nowPlayingItemBinding.executePendingBindings();
        holder.nowPlayingItemBinding.getTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTickets(result.getTitle());}
        });
    }

    private void openTickets(String title) {
        String url = "https://www.fandango.com/search?q="+title+"&mode=general";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NowPlayingItemBinding nowPlayingItemBinding;
        public ViewHolder(@NonNull NowPlayingItemBinding nowPlayingItemBinding, TouchListener tTouchListener)  {
            super(nowPlayingItemBinding.getRoot());
            this.nowPlayingItemBinding = nowPlayingItemBinding;
            this.nowPlayingItemBinding.getRoot().setOnClickListener(this);
            touchListener = tTouchListener;
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
