package com.example.imdbclone.repository;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class loginRepository {
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<FirebaseUser>();
    private FirebaseUser cUser= FirebaseAuth.getInstance().getCurrentUser();
    private static loginRepository repository;


    public MutableLiveData<FirebaseUser> getUser() {
        user.postValue(cUser);
        user.setValue(cUser);
        return user;
    }

    public static loginRepository getInstance(){
        if(repository==null)repository=new loginRepository();
        return repository;
    }



    public void setcUser(){
        cUser= FirebaseAuth.getInstance().getCurrentUser();
        user.postValue(cUser);
        user.setValue(cUser);
    }


}
