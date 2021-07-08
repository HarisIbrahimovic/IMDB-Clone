package com.example.imdbclone.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.repository.SearchRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<MovieResults.Result>> searchData;
    private SearchRepository repository;

    public void init(String title){
        if(searchData!=null)return;
        repository = SearchRepository.getInstance();
        searchData = repository.getSearchData(title);
    }

    public LiveData<List<MovieResults.Result>> getSearchData() {
        return searchData;
    }
    public void searchAgain(String title){
        searchData = repository.getSearchData(title);
    }
}
