package com.example.imdbclone.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.imdbclone.R;
import com.example.imdbclone.model.FavActor;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.MovieWatchlist;
import com.example.imdbclone.model.Rating;
import com.example.imdbclone.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class menuRepository {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "638d2a39ce05cb8e7bc51d828b339cce";
    public static String LANGUAGE = "en_US";
    public static int PAGE = 1;
    static menuRepository instance;
    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    private MutableLiveData<ArrayList<MovieWatchlist>> watchListData = new MutableLiveData<>();
    private MutableLiveData<List<MovieResults.Result>> upcomingData = new MutableLiveData<List<MovieResults.Result>>();
    private MutableLiveData<List<MovieResults.Result>> topRatedData = new MutableLiveData<List<MovieResults.Result>>();
    private MutableLiveData<List<MovieResults.Result>> popularData = new MutableLiveData<List<MovieResults.Result>>();
    private MutableLiveData<List<MovieResults.Result>> newData = new MutableLiveData<List<MovieResults.Result>>();
    private MutableLiveData<ArrayList<FavActor>> favActorsData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Rating>> userRatingsData = new MutableLiveData<>();
    private MutableLiveData<User> currentUserData = new MutableLiveData<>();

    private ArrayList<MovieResults.Result> listOfPopularMovies = new ArrayList<>();
    private ArrayList<MovieResults.Result> listOfTopRated = new ArrayList<>();
    private ArrayList<MovieResults.Result> listOfUpcoming = new ArrayList<>();
    private ArrayList<MovieResults.Result> listOfNew = new ArrayList<>();
    private ArrayList<MovieWatchlist> watchList = new ArrayList<>();
    private ArrayList<FavActor> listOfFavActors= new ArrayList<>();
    private ArrayList<Rating> ratingList = new ArrayList<>();

    private List<MovieResults.Result> topRatedMovies;
    private List<MovieResults.Result> upcomingMovies;
    private List<MovieResults.Result> popularMovies;
    private List<MovieResults.Result> newMovies;
    private User user;

    public MutableLiveData<ArrayList<Rating>> getRatingData() {
        setRatingData();
        return userRatingsData;
    }
    public MutableLiveData<ArrayList<MovieWatchlist>> getWatchListData() {
        setWatchlistData();
        watchListData.setValue(watchList);
        return watchListData;
    }

    public static menuRepository getInstance(){
        if(instance==null)instance = new menuRepository();
        return instance;
    }

    public MutableLiveData<User> getCurrentUserData() {
        setCurrentUserData();
        return currentUserData;
    }

    public MutableLiveData<ArrayList<FavActor>> getListOfFavActors() {
        if(user==null)return null;
        setActors(user.getId());
        favActorsData.setValue(listOfFavActors);
        return favActorsData;
    }

    public MutableLiveData<List<MovieResults.Result>> getTopRatedMovies(){
        setTopRated();
        topRatedData.setValue(listOfTopRated);
        return topRatedData;
    }

    public MutableLiveData<List<MovieResults.Result>> getPopularMovies(){
        setPopularMovies();
        popularData.setValue(listOfPopularMovies);
        return popularData;
    }

    public MutableLiveData<List<MovieResults.Result>> getUpcomingData(){
        setUpcomingMovies();
        upcomingData.setValue(listOfUpcoming);
        return upcomingData;
    }
    public MutableLiveData<List<MovieResults.Result>> getNewMovies(){
        setNewMovies();
        newData.setValue(listOfNew);
        return newData;
    }

    private void setRatingData() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser==null)return;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ratings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Rating rrating = dataSnapshot.getValue(Rating.class);
                    if(rrating.getUserId().equals(fUser.getUid())){
                        ratingList.add(rrating);
                    }
                }
                userRatingsData.postValue(ratingList);
                userRatingsData.setValue(ratingList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void setWatchlistData() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser==null) return;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid()).child("Watchlist");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MovieWatchlist movie = dataSnapshot.getValue(MovieWatchlist.class);
                    watchList.add(movie);
                }
                watchListData.postValue(watchList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setActors(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("Favorites");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfFavActors.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    FavActor favActor = dataSnapshot.getValue(FavActor.class);
                    listOfFavActors.add(favActor);
                }
                favActorsData.postValue(listOfFavActors);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setCurrentUserData() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    user = dataSnapshot.getValue(User.class);
                    if(user.getId().equals(fUser.getUid())){
                        currentUserData.postValue(user);
                        currentUserData.setValue(user);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void setNewMovies(){
        Call<MovieResults> call = apiInterface.getMoviesList("now_playing",API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                newMovies = results.getResults();
                listOfNew.clear();
                for(MovieResults.Result movie : newMovies){
                    listOfNew.add(movie);
                }
                newData.postValue(listOfNew);
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }

    public void setUpcomingMovies(){
        Call<MovieResults> call = apiInterface.getMoviesList("upcoming",API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                upcomingMovies = results.getResults();
                listOfUpcoming.clear();
                for(MovieResults.Result movie : upcomingMovies){
                    listOfUpcoming.add(movie);
                }
                upcomingData.postValue(listOfUpcoming);
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }

    public void setPopularMovies(){
        Call<MovieResults> call = apiInterface.getMoviesList("popular",API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                popularMovies = results.getResults();
                listOfPopularMovies.clear();
                for(MovieResults.Result movie : popularMovies){
                    listOfPopularMovies.add(movie);
                }
                popularData.postValue(listOfPopularMovies);
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }

    public void setTopRated(){
        Call<MovieResults> call = apiInterface.getMoviesList("top_rated",API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                topRatedMovies = results.getResults();
                listOfTopRated.clear();
                for(MovieResults.Result movie : topRatedMovies){
                    listOfTopRated.add(movie);
                }
                listOfTopRated.remove(0);
                topRatedData.postValue(listOfTopRated);
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }

    public void updatePassword(String newPass){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPass);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.child("password").setValue(newPass);
    }

    public void updateEmail(String newEmail){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(newEmail);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.child("email").setValue(newEmail);
    }

    public void updateUserName(String newName){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.child("name").setValue(newName);
    }

    public void updatePicutre(String imageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.child("imageUrl").setValue(imageUrl);
    }


}
