package com.example.imdbclone.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.model.ActorCredits;
import com.example.imdbclone.model.ActorDetails;
import com.example.imdbclone.repository.ActorDetailsRepository;

import java.util.List;

public class DetailedActorViewModel extends ViewModel {

    private MutableLiveData<List<ActorCredits.Cast>> creditData;
    private MutableLiveData<ActorDetails> details;
    private ActorDetailsRepository repository;

    public void init(String id){
        if(creditData!=null)return;
        repository = ActorDetailsRepository.getInstance();
        creditData= repository.getActorCreditsData(id);
        details = repository.getActorDetailsData(id);
    }


    public boolean addToFav(String name,String posterPath,String id){
        if(repository.addToFavorites(name,posterPath,id))
            return true;
        else
            return false;
    }

    public LiveData<List<ActorCredits.Cast>> getCreditData() {
        return creditData;
    }
    public MutableLiveData<ActorDetails> getDetails() {
        return details;
    }
}
