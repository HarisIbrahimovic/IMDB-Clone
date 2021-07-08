package com.example.imdbclone.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.model.Actors;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.SingleMovie;
import com.example.imdbclone.repository.detailsRepository;

import java.util.List;

public class DetailsViewModel extends ViewModel {


    private MutableLiveData<List<Actors.Cast>> cast;
    private MutableLiveData<SingleMovie> movie;
    private detailsRepository repository;

    public void init(String id){
        if(movie!=null&&cast!=null)return;
        repository=detailsRepository.getInstance();
        movie=repository.getMovie(id);
        cast=repository.getActors(id);
    }

    public boolean addToWatchlist(String movieId,String rating, String posterPath){
        if(repository.addToWatchlist(movieId,rating,posterPath))return true;
        return false;
    }

    public LiveData<SingleMovie> getMovie() {
        return movie;
    }
    public LiveData<List<Actors.Cast>> getCast() {
        return cast;
    }
}
