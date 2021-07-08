package com.example.imdbclone.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imdbclone.R;
import com.example.imdbclone.viewModels.userNameViewModel;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

public class userNameActivity extends AppCompatActivity {

    private userNameViewModel viewModel;
    private EditText userName;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        setUpViews();
        viewModel = ViewModelProviders.of(this).get(userNameViewModel.class);
        viewModel.init();
        onClicks();
    }

    private void onClicks() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String cUserName = userName.getText().toString().trim();
                viewModel.addUserName(cUserName,email,password);
                Toast.makeText(userNameActivity.this,"Welcome "+ userName.getText().toString(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(userNameActivity.this,menuActivity.class));
                finish();
            }
        });
    }

    private void setUpViews() {
        userName= findViewById(R.id.usernameText);
        button = findViewById(R.id.submitButton);
    }
}