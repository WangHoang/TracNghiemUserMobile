package com.example.tracnghiemuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tracnghiemuser.databinding.ActivityScoreBinding;
import com.example.tracnghiemuser.fragment.AccountFragment;
import com.example.tracnghiemuser.fragment.HomeFragment;
import com.example.tracnghiemuser.fragment.rankFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

public class FirstActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    HomeFragment homeFragment = new HomeFragment();
    rankFragment rankFragment = new rankFragment();
    AccountFragment accountFragment = new AccountFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        toolbar = findViewById(R.id.toolbarFrag);

        bottomNavigationView = findViewById(R.id.menu_bottom);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.mnBottomHome) {
                    toolbar.setTitle("Trang chủ");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                }
                else if(itemId == R.id.mnBottomRank) {
                    toolbar.setTitle("Xếp hạng");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new rankFragment()).commit();
                }
                else if(itemId == R.id.mnBottomAccount) {
                    toolbar.setTitle("Tài khoản");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountFragment()).commit();
                }

                return true;
            }
        });
    }


}



