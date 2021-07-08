package com.example.imdbclone.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.imdbclone.repository.userNameRepository;

public class userNameViewModel extends ViewModel {

    private userNameRepository repository;
    public void init(){
        repository = userNameRepository.getInstance();
    }

    public void addUserName(String name,String email, String password) {
        repository.addUserName(name,email,password);
    }
}
