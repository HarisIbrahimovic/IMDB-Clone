package com.example.imdbclone.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class GlideBidingAdapters {

    @BindingAdapter("detailedMovieImage")
    public static void setImage(ImageView view, String imageUrl){
        Context context = view.getContext();
        RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(15));
        if(imageUrl==null) {
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/60/No_Picture.jpg";
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options)
                    .into(view);
            return;
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+imageUrl)
                .apply(options)
                .into(view);
    }

    @BindingAdapter("actorImage")
    public static void setActorImage(ImageView view, String imageUrl){
        Context context = view.getContext();
        RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(45));
        if(imageUrl==null) {
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/60/No_Picture.jpg";
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options)
                    .into(view);
            return;
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+imageUrl)
                .apply(options)
                .into(view);

    }

    @BindingAdapter("ratingImage")
    public static void setRatingImage(ImageView view, String imageUrl){
        Context context = view.getContext();
        RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(15));
        if(imageUrl.equals("default")){
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/60/No_Picture.jpg";
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options)
                    .into(view);
            return;
        }
        Glide.with(context)
                    .load(imageUrl)
                    .apply(options)
                    .into(view);
    }

}
