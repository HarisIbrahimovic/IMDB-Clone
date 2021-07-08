package com.example.imdbclone.utils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.BindingAdapter;

public class TextViewBidingAdapters {

    @BindingAdapter("setTextView")
    public static void setTextView(TextView button, String value) {
        if (value != null) button.setText(value);
        else button.setText("");
    }

    @BindingAdapter("releaseDate")
    public static void setReleaseDate(TextView textView, String value){
        if (value != null) textView.setText("Release date: "+ value);
        else textView.setText("");
    }

    @BindingAdapter("originalTitle")
    public static void setOriginalTitle(TextView textView, String value){
        if (value != null) textView.setText("Original title: "+ value);
        else textView.setText("");
    }

    @BindingAdapter("popularity")
    public static void setPopularity(TextView textView, String value){
        if (value != null) textView.setText("Popularity: "+ value);
        else textView.setText("");
    }

    @BindingAdapter("overview")
    public static void setOverView(TextView textView, String value){
        if (value != null) textView.setText("Overview: "+ value);
        else textView.setText("");
    }

    @BindingAdapter("ratingScore")
    public static void setRating(AppCompatButton button, double value){
        if (value==0) button.setText("NR");
        else button.setText(""+value);
    }

    @BindingAdapter("ratingScoreS")
    public static void setRatingS(AppCompatButton button, String value){
        if (value.equals("0.0")) button.setText("NR");
        else button.setText(""+value);
    }


}
