package com.example.imdbclone.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.imdbclone.model.ActorCredits;
import com.example.imdbclone.model.Actors;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.SingleMovie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class detailsRepository {
    static detailsRepository instance;
    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "638d2a39ce05cb8e7bc51d828b339cce";
    public static String LANGUAGE = "en_US";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    private MutableLiveData<SingleMovie> movie = new MutableLiveData<SingleMovie>();
    private SingleMovie sMovie;

    private List<Actors.Cast> cCast;
    private ArrayList<Actors.Cast> listOfCast = new ArrayList<>();
    private MutableLiveData<List<Actors.Cast>> castData = new MutableLiveData<List<Actors.Cast>>();

    public static detailsRepository getInstance(){
        if(instance==null)instance = new detailsRepository();
        return instance;
    }

    public MutableLiveData<List<Actors.Cast>> getActors(String id){
        setActors(id);
        castData.setValue(listOfCast);
        return castData;
    }
    public MutableLiveData<SingleMovie> getMovie(String id) {
        setMovie(id);
        return movie;
    }

    public void setActors(String movieId){
        Call call = apiInterface.getMovieCast(movieId,API_KEY,LANGUAGE);
        call.enqueue(new Callback<Actors>() {
            @Override
            public void onResponse(Call<Actors> call, Response<Actors> response) {
                Actors actors = response.body();
                cCast = actors.getCast();
                listOfCast.clear();
                for(Actors.Cast c: cCast){
                    listOfCast.add(c);
                }
                castData.postValue(listOfCast);
            }
            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

    }

    public void setMovie(String id) {
        Call<SingleMovie> call = apiInterface.getSingleMovie(id,API_KEY,LANGUAGE);
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
                sMovie = response.body();
                movie.postValue(sMovie);
                movie.setValue(sMovie);
            }
            @Override
            public void onFailure(Call<SingleMovie> call, Throwable t) {
            }
        });
    }

    public boolean addToWatchlist(String movieId,String rating, String posterPath){
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(fUser==null)return false;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        HashMap<String, String> movie = new HashMap<>();
        movie.put("movieId",movieId);
        movie.put("posterPath",posterPath);
        movie.put("rating",rating);
        databaseReference.child("Watchlist").child(movieId).setValue(movie);
        return true;
    }
}
