package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.UserRatingsItemBinding;
import com.example.imdbclone.model.Rating;

import java.util.ArrayList;

public class UserRatingsAdapter extends RecyclerView.Adapter<UserRatingsAdapter.ViewHolder> {
    private ArrayList<Rating> ratings;
    private Context context;
    private RatingClicked ratingClicked;

    public UserRatingsAdapter(ArrayList<Rating> ratings, Context context, RatingClicked ratingClicked) {
        this.ratings = ratings;
        this.context = context;
        this.ratingClicked = ratingClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        UserRatingsItemBinding userRatingsItemBinding = UserRatingsItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(userRatingsItemBinding,ratingClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = ratings.get(position);
        holder.userRatingsItemBinding.setRating(rating);
        holder.userRatingsItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        UserRatingsItemBinding userRatingsItemBinding;
        RatingClicked ratingClicked;
        public ViewHolder(@NonNull UserRatingsItemBinding userRatingsItemBinding, RatingClicked ratingClicked) {
            super(userRatingsItemBinding.getRoot());
            this.userRatingsItemBinding = userRatingsItemBinding;
            this.ratingClicked = ratingClicked;
            this.userRatingsItemBinding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            ratingClicked.ratingClicked(getAdapterPosition());

        }
    }

    public interface RatingClicked{
        void ratingClicked(int position);
    }
}
