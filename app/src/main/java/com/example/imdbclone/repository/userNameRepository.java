package com.example.imdbclone.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class userNameRepository {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
    static userNameRepository repository;

    public static userNameRepository getInstance(){
        if(repository==null)repository= new userNameRepository();
        return repository;
    }

    public void addUserName(String name,String email, String password) {
        databaseReference.child(cUser.getUid()).child("username").setValue(name);
        HashMap<String, String> User = new HashMap<>();
        User.put("name",name);
        User.put("id",cUser.getUid());
        User.put("password",password);
        User.put("email",email);
        User.put("imageUrl","default");
        databaseReference.child(cUser.getUid()).setValue(User);
    }
}
