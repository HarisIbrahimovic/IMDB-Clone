package com.example.imdbclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdbclone.databinding.CreditItemBinding;
import com.example.imdbclone.model.ActorCredits;

import java.util.List;

public class ActorCreditAdapter extends RecyclerView.Adapter<ActorCreditAdapter.ViewHolder> {
    private List<ActorCredits.Cast> list;
    private Context context;
    private TouchListener touchListener;

    public ActorCreditAdapter(List<ActorCredits.Cast> list, Context context, TouchListener touchListener) {
        this.list = list;
        this.context = context;
        this.touchListener = touchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CreditItemBinding creditItemBinding = CreditItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(creditItemBinding,touchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ActorCredits.Cast cast = list.get(position);
            holder.creditItemBinding.setCredit(cast);
            holder.creditItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TouchListener touchListener;
        CreditItemBinding creditItemBinding;
        public ViewHolder(@NonNull CreditItemBinding creditItemBinding,TouchListener listener) {
            super(creditItemBinding.getRoot());
            this.creditItemBinding = creditItemBinding;
            this.creditItemBinding.getRoot().setOnClickListener(this);
            this.touchListener = listener;
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
