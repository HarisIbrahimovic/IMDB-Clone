package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.imdbclone.databinding.ActorItemBinding;
import com.example.imdbclone.model.Actors;
import java.util.List;

public class actorAdapter extends RecyclerView.Adapter<actorAdapter.ViewHolder> {
    private List<Actors.Cast> cast;
    private Context context;
    private TouchListener touchListener;

    public actorAdapter(List<Actors.Cast> cast, Context context, TouchListener touchListener) {
        this.cast = cast;
        this.context = context;
        this.touchListener = touchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ActorItemBinding actorItemBinding = ActorItemBinding.inflate(layoutInflater,parent,false);
        return  new ViewHolder(actorItemBinding,touchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Actors.Cast actor = cast.get(position);
        holder.actorItemBinding.setActor(actor);
        holder.actorItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ActorItemBinding actorItemBinding;
        public ViewHolder(@NonNull ActorItemBinding actorItemBinding, TouchListener tTouchListener) {
            super(actorItemBinding.getRoot());
            actorItemBinding.getRoot().setOnClickListener(this);
            this.actorItemBinding = actorItemBinding;
            touchListener = tTouchListener;
        }
        @Override
        public void onClick(View v) {
            touchListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface TouchListener{
        void onNoteClick(int position);
    }
}
