package com.example.imdbclone.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.imdbclone.model.MovieResults;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRepository {
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "638d2a39ce05cb8e7bc51d828b339cce";
    public static String LANGUAGE = "en_US";
    public static int PAGE = 1;

    private MutableLiveData<List<MovieResults.Result>> searchData = new MutableLiveData<>();
    private ArrayList<MovieResults.Result> listOfSearchedMovies = new ArrayList<>();
    private List<MovieResults.Result> searchMovies;

    public static SearchRepository repository;

    public static SearchRepository getInstance(){
        if(repository==null)repository = new SearchRepository();
        return repository;
    }

    public MutableLiveData<List<MovieResults.Result>> getSearchData(String test){
        setSearchData(test);
        searchData.setValue(listOfSearchedMovies);
        return searchData;
    }

    private void setSearchData(String test) {
        Call<MovieResults> call = apiInterface.findMovies(API_KEY,LANGUAGE,test,1);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                searchMovies = results.getResults();
                listOfSearchedMovies.clear();
                for(MovieResults.Result movie : searchMovies){
                    listOfSearchedMovies.add(movie);
                }
                searchData.postValue(listOfSearchedMovies);
            }
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
            }
        });
    }


}
