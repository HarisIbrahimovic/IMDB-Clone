package com.example.imdbclone.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imdbclone.R;
import com.example.imdbclone.Views.MainActivity;
import com.example.imdbclone.Views.SearchActivity;
import com.example.imdbclone.model.User;
import com.example.imdbclone.viewModels.menuViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class settingsFragment extends Fragment {
    private EditText email;
    private EditText userName;
    private EditText password;
    private Button uploadPicture;
    private TextView signOut;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri;
    private String imageUrl;
    private menuViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        checkUser();
        setUpStuff(view);
        observe();
        onClicks();
        return view; }

    private void checkUser() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
            getActivity().finish();
        }
    }

    private void observe() {
        viewModel.getcUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                email.setText(user.getEmail());
                password.setText(user.getPassword());
                userName.setText(user.getName());
            }
        });
    }

    private void onClicks() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePicture();
            }
        });
    }

    private void setUpStuff(View view) {
        email = view.findViewById(R.id.settingEmail);
        userName = view.findViewById(R.id.settingsName);
        password = view.findViewById(R.id.settingPassword);
        uploadPicture = view.findViewById(R.id.uploadButton);
        signOut = view.findViewById(R.id.signoutText);
        viewModel = ViewModelProviders.of(this).get(menuViewModel.class);
        viewModel.init();
        email.setOnEditorActionListener(updateEmail);
        password.setOnEditorActionListener(updatePassword);
        userName.setOnEditorActionListener(updateUserName);
    }


    private TextView.OnEditorActionListener updateEmail = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            viewModel.updateEmail(email.getText().toString().trim());
            Toast.makeText(getActivity().getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private TextView.OnEditorActionListener updatePassword = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            viewModel.updatePassword(password.getText().toString().trim());
            Toast.makeText(getActivity().getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private TextView.OnEditorActionListener updateUserName = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            viewModel.updateName(userName.getText().toString().trim());
            Toast.makeText(getActivity().getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private void chosePicture() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 &&resultCode ==RESULT_OK && data!=null &&data.getData()!=null){
            imageUri = data.getData();
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/"+randomKey);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl=uri.toString();
                        viewModel.updatePicutre(imageUrl);
                    }
                });
            }
        });

    }
}