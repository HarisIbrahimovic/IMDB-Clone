package com.example.imdbclone.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.model.FavActor;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.MovieWatchlist;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.example.imdbclone.repository.menuRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class menuViewModel extends ViewModel {

    private menuRepository repository;
    private MutableLiveData<Integer> cFragment = new MutableLiveData<>();
    private MutableLiveData<User> cUser = new MutableLiveData<>();
    private MutableLiveData<List<MovieResults.Result>> popularMovies;
    private MutableLiveData<List<MovieResults.Result>> topRatedMovies;
    private MutableLiveData<List<MovieResults.Result>> upcomingMovies;
    private MutableLiveData<List<MovieResults.Result>> newMovies;
    private MutableLiveData<ArrayList<MovieWatchlist>> watchListData;
    private MutableLiveData<ArrayList<FavActor>> favActorsData;
    private MutableLiveData<ArrayList<Rating>> ratingData;
    private int numFrag=0;

    public LiveData<ArrayList<Rating>> getRatingData() {
        return ratingData;
    }

    public void init(){
        if(popularMovies!=null&&newMovies!=null&&upcomingMovies!=null&&topRatedMovies!=null)return;
        repository = menuRepository.getInstance();
        popularMovies = repository.getPopularMovies();
        newMovies = repository.getNewMovies();
        upcomingMovies = repository.getUpcomingData();
        topRatedMovies = repository.getTopRatedMovies();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            cUser=repository.getCurrentUserData();
            favActorsData = repository.getListOfFavActors();
            watchListData = repository.getWatchListData();
            ratingData = repository.getRatingData();
        }
    }

    public LiveData<Integer> getCurrentFragment() {
        cFragment.postValue(numFrag);
        cFragment.setValue(numFrag);
        return cFragment;
    }

    public void setCurrentFragment(int num) {
        numFrag=num;
    }
    public LiveData<List<MovieResults.Result>> getPopularMovies(){
        return popularMovies;
    }
    public LiveData<List<MovieResults.Result>> getNewMovies(){return newMovies;};
    public LiveData<List<MovieResults.Result>> getTopRatedMovies() {return topRatedMovies; }
    public LiveData<List<MovieResults.Result>> getUpcomingMovies() {
        return upcomingMovies;
    }
    public LiveData<ArrayList<FavActor>> getFavActorsData() {return favActorsData;}
    public LiveData<User> getcUser() {
        return cUser;
    }
    public LiveData<ArrayList<MovieWatchlist>> getWatchlistData(){return watchListData;}

    public void signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    public void updatePassword(String newPass){
        repository.updatePassword(newPass);
    }
    public void updateEmail(String newEmail){
        repository.updateEmail(newEmail);
    }
    public void updateName(String newName){
        repository.updateUserName(newName);
    }

    public void updatePicutre(String imageUrl) {
        repository.updatePicutre(imageUrl);
    }
}
