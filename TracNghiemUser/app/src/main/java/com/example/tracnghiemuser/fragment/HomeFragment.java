package com.example.tracnghiemuser.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemuser.Adapters.MonAdapter;
import com.example.tracnghiemuser.MainActivity;
import com.example.tracnghiemuser.Models.MonModel;
import com.example.tracnghiemuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private MonAdapter adapter;
    private ArrayList<MonModel> monList;
    private DatabaseReference databaseRef;
    private TextView userNameTextView;
    private DatabaseReference usersRef;
    private String currentUserId;
    private FirebaseUser currentUser;
    private Context context;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo tham chiếu đến cơ sở dữ liệu Firebase và lấy thông tin người dùng hiện tại
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerMon);
        userNameTextView = view.findViewById(R.id.txtName);

        monList = new ArrayList<>();
        adapter = new MonAdapter(getActivity(), monList);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        retrieveRankAndScore();


        databaseRef = FirebaseDatabase.getInstance().getReference().child("cac mon");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monList.clear(); // Clear the existing list
                // Iterate through each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve monName and monImage from each child node
                    String monName = snapshot.child("monName").getValue(String.class);
                    String monImage = snapshot.child("monImage").getValue(String.class);
                    // Create a MonModel object and add it to the list
                    MonModel monModel = new MonModel(monName, monImage, snapshot.getKey(), 0);
                    monList.add(monModel);
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MainActivity when the button is clicked
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btnAllMon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MainActivity when the button is clicked
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void retrieveRankAndScore() {
        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin rank và score từ dataSnapshot
                    String userName = dataSnapshot.child("userName").getValue(String.class);


                    // Hiển thị thông tin lên giao diện người dùng
                    userNameTextView.setText(userName);

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong việc đọc dữ liệu từ cơ sở dữ liệu
                Toast.makeText(context, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
}
}