package com.example.imdbclone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.SingleMovie;

import java.util.List;

public class BrowserAdapter {

    @BindingAdapter("checkArray")
    public static void setArray(TextView textView, List<SingleMovie.Genre> list){
        try{
            textView.setText(list.get(0).getName());
        }catch (Exception e){
            textView.setText("Unknown");
        }
    }

}
