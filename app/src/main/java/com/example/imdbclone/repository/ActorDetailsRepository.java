package com.example.imdbclone.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.imdbclone.model.ActorCredits;
import com.example.imdbclone.model.ActorDetails;
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

public class ActorDetailsRepository {
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "638d2a39ce05cb8e7bc51d828b339cce";
    public static String LANGUAGE = "en_US";

    private MutableLiveData<List<ActorCredits.Cast>> actorCreditsData = new MutableLiveData<>();
    private MutableLiveData<ActorDetails> actorDetailsData = new MutableLiveData<>();
    private ArrayList<ActorCredits.Cast> listOfCredits = new ArrayList<>();
    private List<ActorCredits.Cast> credits;
    static ActorDetailsRepository repository;
    private ActorDetails details;
    private FirebaseUser user;

    public static ActorDetailsRepository getInstance(){
        if(repository==null)repository=new ActorDetailsRepository();
        return repository;
    }

    public MutableLiveData<List<ActorCredits.Cast>> getActorCreditsData(String id) {
        setCredits(id);
        actorCreditsData.setValue(listOfCredits);
        return actorCreditsData;
    }

    public MutableLiveData<ActorDetails> getActorDetailsData(String id) {
        setDetails(id);
        return actorDetailsData;
    }

    private void setCredits(String id) {
        Call call = apiInterface.getActorCredits(id,API_KEY,LANGUAGE);
        call.enqueue(new Callback<ActorCredits>() {
            @Override
            public void onResponse(Call<ActorCredits> call, Response<ActorCredits> response) {
                ActorCredits cCredits = response.body();
                credits = cCredits.getCast();
                listOfCredits.clear();
                for(ActorCredits.Cast c: credits){
                    listOfCredits.add(c);
                }
                actorCreditsData.postValue(listOfCredits);
            }
            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }


    private void setDetails(String id) {
        Call call = apiInterface.getActorDetails(id,API_KEY,LANGUAGE);
        call.enqueue(new Callback<ActorDetails>() {
            @Override
            public void onResponse(Call<ActorDetails> call, Response<ActorDetails> response) {
                details=response.body();
                actorDetailsData.postValue(details);
                actorDetailsData.setValue(details);
            }
            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    public boolean addToFavorites(String name,String posterPath, String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)return false;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Favorites");
        HashMap<String, String> actor = new HashMap<>();
        actor.put("name",name);
        actor.put("posterPath",posterPath);
        actor.put("id",id);
        databaseReference.child(id).setValue(actor);
        return true;
    }
}
