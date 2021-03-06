package com.example.imdbclone.viewModels;

import android.text.Editable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdbclone.repository.loginRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class loginViewModel extends ViewModel {

    private loginRepository repository;
    private MutableLiveData<Integer> cState = new MutableLiveData<Integer>();
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Integer> checkProblemsData = new MutableLiveData<>();
    private int state=0;


    public LiveData<Integer> getCheckProblemsData() {
        return checkProblemsData;
    }
    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void setCheckProblemsData(int num) {
        checkProblemsData.postValue(num);
        checkProblemsData.setValue(num);
    }

    public void init(){
       repository = loginRepository.getInstance();
       user=repository.getUser();
    }

    public MutableLiveData<Integer> getcState() {
        cState.setValue(state);
        return cState;
    }

    public void changeState(){
        if(state==0)state=1;
        else state=0;
        cState.postValue(state);
        cState.setValue(state);
    }


    public void createUser(String email, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||email.length()<10||password.length()<8){
            setCheckProblemsData(2);
            return;
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    repository.setcUser();
                    user=repository.getUser();
                }else setCheckProblemsData(1);
            }
        });
    }

    public void loginUser(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    repository.setcUser();
                    user=repository.getUser();
                }else setCheckProblemsData(1);
            }
        });
    }


}
