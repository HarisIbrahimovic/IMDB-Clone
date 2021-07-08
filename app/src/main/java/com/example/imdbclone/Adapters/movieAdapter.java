package com.example.imdbclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.imdbclone.R;
import com.example.imdbclone.model.MovieResults;

import java.util.ArrayList;
import java.util.List;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder> {
    private List<MovieResults.Result> movies;
    private Context context;
    private int type;
    private TouchListener touchListener;

    public movieAdapter(List<MovieResults.Result> movies, Context context, int type, TouchListener touchListener) {
        this.movies = movies;
        this.context = context;
        this.type = type;
        this.touchListener=touchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(type==1)
            view= LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        else
             view = LayoutInflater.from(context).inflate(R.layout.big_movie_item,parent,false);
        return new ViewHolder(view,touchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieResults.Result result = movies.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms( new CenterCrop(), new RoundedCorners(45));
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+result.getPosterPath())
                .apply(requestOptions)
                .into(holder.image);
        if(type==0){
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.youtube.com/results?search_query="+result.getTitle();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
            return;
        }
        holder.button.setText(result.getVoteAverage().toString());
        if(result.getVoteAverage().toString().equals("0.0"))holder.button.setText("NR");
    }

    @Override
    public int getItemCount() {
        if(movies==null) return 0;
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(@NonNull View itemView,TouchListener TouchListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            touchListener = TouchListener;
        }
        ImageView image = itemView.findViewById(R.id.smallMoviePicutre);
        Button button = itemView.findViewById(R.id.scoreButtonSmall);

        @Override
        public void onClick(View v) {
            touchListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface TouchListener{
        void onNoteClick(int position);
    }
}
