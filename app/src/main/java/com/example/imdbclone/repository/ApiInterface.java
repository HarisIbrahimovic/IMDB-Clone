package com.example.imdbclone.repository;

import com.example.imdbclone.model.ActorCredits;
import com.example.imdbclone.model.ActorDetails;
import com.example.imdbclone.model.Actors;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.model.SingleMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/{key_word}")
    Call<MovieResults> getMoviesList(
            @Path("key_word") String key_word,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("/3/movie/{movieId}/credits")
    Call<Actors> getMovieCast(
            @Path("movieId") String id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("/3/movie/{movieId}")
    Call<SingleMovie> getSingleMovie(
            @Path("movieId") String id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("/3/person/{actorId}/movie_credits")
    Call<ActorCredits> getActorCredits(
            @Path("actorId") String actorId,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("/3/search/movie")
    Call<MovieResults> findMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );
    @GET("/3/person/{person_id}")
    Call<ActorDetails> getActorDetails(
            @Path("person_id") String actorId,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

}
