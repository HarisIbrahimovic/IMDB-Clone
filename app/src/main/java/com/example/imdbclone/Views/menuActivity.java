package com.example.imdbclone.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.imdbclone.Adapters.movieAdapter;
import com.example.imdbclone.R;
import com.example.imdbclone.fragments.homeFragment;
import com.example.imdbclone.fragments.profileFragment;
import com.example.imdbclone.fragments.searchFragment;
import com.example.imdbclone.fragments.settingsFragment;
import com.example.imdbclone.model.MovieResults;
import com.example.imdbclone.viewModels.loginViewModel;
import com.example.imdbclone.viewModels.menuViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class menuActivity extends AppCompatActivity {

    private menuViewModel viewModel;
    private Fragment selected = null;
    private int cFragment=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel = ViewModelProviders.of(this).get(menuViewModel.class);
        viewModel.init();
        observe();
        setFragments();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }


    private void observe() {
        viewModel.getCurrentFragment().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cFragment = integer;
                setFragments();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.home:
                    selected = new homeFragment();
                    viewModel.setCurrentFragment(0);
                    break;
                case R.id.Search:
                    selected = new searchFragment();
                    viewModel.setCurrentFragment(1);
                    break;
                case R.id.Profile:
                    selected = new profileFragment();
                    viewModel.setCurrentFragment(2);
                    break;
                case R.id.Settings:
                    selected = new settingsFragment();
                    viewModel.setCurrentFragment(3);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
            return true;
        }
    };

    private void setFragments(){
        switch (cFragment){
            case 0:
                selected = new homeFragment();
                viewModel.setCurrentFragment(0);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                break;
            case 1:
                selected = new searchFragment();
                viewModel.setCurrentFragment(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                break;
            case 2:

                selected = new profileFragment();
                viewModel.setCurrentFragment(2);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                break;
            case 3:
                selected = new settingsFragment();
                viewModel.setCurrentFragment(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                break;
        }
    }

}