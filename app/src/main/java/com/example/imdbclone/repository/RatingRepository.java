package com.example.imdbclone.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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
import java.util.UUID;

public class RatingRepository {
    static RatingRepository repository;
    private MutableLiveData<ArrayList<Rating>> ratingData= new MutableLiveData<>();
    private ArrayList<Rating> listOfRatings = new ArrayList<>();
    private User user = new User();
    private FirebaseUser fUser;

    public static RatingRepository getInstance(){
        if(repository==null)repository = new RatingRepository();
        return repository;
    }
    public MutableLiveData<ArrayList<Rating>> getRatingData(String movieId) {
        setRatingData(movieId);
        ratingData.setValue(listOfRatings);
        return ratingData;
    }

    public boolean addComment(String posterPath,String movieId,String movieName,String score, String comment){
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(fUser==null)return false;
        findCurrentUser(posterPath,movieId,movieName,score,comment);
        return true;
    }

    private boolean findCurrentUser(String posterPath,String movieId,String movieName,String score, String comment) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        user = dataSnapshot.getValue(User.class);
                        if(user.getId().equals(fUser.getUid())){
                            setRatingInFB(posterPath,movieId,movieName,score,comment);
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return true;
    }

    private void setRatingInFB(String posterPath,String movieId,String movieName,String score, String comment) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ratings");
        HashMap<String,String> rating = new HashMap<>();
        String ratingId = UUID.randomUUID().toString();
        rating.put("posterPath",posterPath);
        rating.put("movieId",movieId);
        rating.put("movieName",movieName);
        rating.put("score",score);
        rating.put("comment",comment);
        rating.put("userId",user.getId());
        rating.put("userName",user.getName());
        rating.put("userImageUrl",user.getImageUrl());
        rating.put("ratingId",ratingId);
        databaseReference.child(ratingId).setValue(rating);
    }

    public void setRatingData(String movieId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ratings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfRatings.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Rating rRating = dataSnapshot.getValue(Rating.class);
                    if(rRating.getMovieId().equals(movieId)){
                        listOfRatings.add(rRating);
                    }
                }
                ratingData.postValue(listOfRatings);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
