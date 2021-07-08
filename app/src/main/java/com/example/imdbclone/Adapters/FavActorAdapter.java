package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.FavActorItemBinding;
import com.example.imdbclone.model.FavActor;

import java.util.ArrayList;

public class FavActorAdapter extends RecyclerView.Adapter<FavActorAdapter.ViewHolder> {
    private ArrayList<FavActor> actors;
    private Context context;
    private FavActorClickListener favActorClickListener;

    public FavActorAdapter(ArrayList<FavActor> actors, Context context, FavActorClickListener favActorClickListener) {
        this.actors = actors;
        this.context = context;
        this.favActorClickListener = favActorClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        FavActorItemBinding favActorItemBinding = FavActorItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(favActorItemBinding, favActorClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavActor actor = actors.get(position);
        holder.favActorItemBinding.setActor(actor);
        holder.favActorItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        FavActorClickListener favActorClickListener;
        FavActorItemBinding favActorItemBinding;
        public ViewHolder(@NonNull FavActorItemBinding favActorItemBinding,FavActorClickListener favActorClickListener) {
            super(favActorItemBinding.getRoot());
            this.favActorItemBinding = favActorItemBinding;
            this.favActorClickListener = favActorClickListener;
            this.favActorItemBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            favActorClickListener.favActorClicked(getAdapterPosition());
        }
    }
    public interface FavActorClickListener{
         void favActorClicked(int position);
    }
}
