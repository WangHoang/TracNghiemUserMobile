package com.example.tracnghiemuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tracnghiemuser.Adapters.MonAdapter;
import com.example.tracnghiemuser.Models.MonModel;
import com.example.tracnghiemuser.databinding.ActivityMainBinding;
import com.example.tracnghiemuser.fragment.AccountFragment;
import com.example.tracnghiemuser.fragment.HomeFragment;
import com.example.tracnghiemuser.fragment.rankFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<MonModel> list;
    MonAdapter adapter;
    ProgressDialog progressDialog;
    Dialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);

        if(loadingDialog.getWindow()!= null){

            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyMon.setLayoutManager(layoutManager);

        adapter = new MonAdapter(this, list);
        binding.recyMon.setAdapter(adapter);



        database.getReference().child("cac mon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(new MonModel(

                                dataSnapshot.child("monName").getValue().toString(),
                                dataSnapshot.child("monImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();

                }
                else {
                    Toast.makeText(MainActivity.this, "Môn không tồn tại", Toast.LENGTH_SHORT).show();

                    loadingDialog.dismiss();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        binding.btnBackMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }



}