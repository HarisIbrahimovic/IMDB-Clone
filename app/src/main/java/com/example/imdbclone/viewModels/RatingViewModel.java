package com.example.imdbclone.viewModels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.model.Rating;
import com.example.imdbclone.repository.RatingRepository;

import java.util.ArrayList;

public class RatingViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Rating>> ratingsData;
    private RatingRepository ratingRepository;

    public void init(String movieId){
        if(ratingsData!=null)return;
        ratingRepository = RatingRepository.getInstance();
        ratingsData = ratingRepository.getRatingData(movieId);
    }

    public boolean addRating(String posterPath,String movieId, String movieName,String score,String comment){
        if(ratingRepository.addComment(posterPath, movieId, movieName,score,comment))return true;
        else return false;
    }

    public LiveData<ArrayList<Rating>> getRatingsData() {
        return ratingsData;
    }
}
