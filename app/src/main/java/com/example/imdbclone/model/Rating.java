package com.example.imdbclone.model;

public class Rating {
    private String posterPath;
    private String userImageUrl;
    private String userName;
    private String userId;
    private String movieId;
    private String movieName;
    private String comment;
    private String score;
    private String ratingId;

    public String getRatingId() {
        return ratingId;
    }

    public String getComment() {
        return comment;
    }

    public String getScore() {
        return score;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }
}
