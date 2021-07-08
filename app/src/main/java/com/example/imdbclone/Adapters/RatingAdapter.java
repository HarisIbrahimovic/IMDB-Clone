package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.RatingItemBinding;
import com.example.imdbclone.model.Rating;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private ArrayList<Rating>ratings;
    private Context context;

    public RatingAdapter(ArrayList<Rating> ratings, Context context) {
        this.ratings = ratings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RatingItemBinding ratingItemBinding = RatingItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(ratingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.ratingItemBinding.setRating(rating);
        holder.ratingItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingItemBinding ratingItemBinding;
        public ViewHolder(@NonNull RatingItemBinding ratingItemBinding) {
            super(ratingItemBinding.getRoot());
            this.ratingItemBinding = ratingItemBinding;
        }
    }
}
