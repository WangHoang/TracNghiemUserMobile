package com.example.tracnghiemuser.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracnghiemuser.FirstActivity;
import com.example.tracnghiemuser.R;
import com.example.tracnghiemuser.SignInActivity;
import com.example.tracnghiemuser.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountFragment extends Fragment {

    private TextView textViewUserName;
    private TextView textViewScore;
    private TextView textViewRank;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;
    private String currentUserId;
    private Context context;
    private Button btnGoToRank;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Đăng xuất người dùng khỏi Firebase
        startActivity(new Intent(getContext(), SignInActivity.class)); // Chuyển đến màn hình đăng nhập
        getActivity().finish(); // Đóng fragment hiện tại
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // Ánh xạ các thành phần giao diện
        textViewUserName = view.findViewById(R.id.nameAccount);
        textViewScore = view.findViewById(R.id.scoreAccount);
        textViewRank = view.findViewById(R.id.rankAccount);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnGoToRank = view.findViewById(R.id.btnRank);

        // Gọi phương thức để lấy rank và score từ cơ sở dữ liệu Firebase
        retrieveRankAndScore();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức đăng xuất
                logoutUser();
            }
        });
        btnGoToRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một instance mới của rankFragment
                Fragment rankFragment = new rankFragment();

                // Chuyển đến rankFragment bằng cách thêm nó vào activity's layout
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, rankFragment);
                transaction.addToBackStack(null);  // Add fragment này vào back stack để quay lại khi cần
                transaction.commit();
            }
        });

        // Truy vấn và hiển thị thông tin người dùng

        return view;

    }

    // Phương thức để lấy rank và score từ cơ sở dữ liệu Firebase
    private void retrieveRankAndScore() {
        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin rank và score từ dataSnapshot
                    String userName = dataSnapshot.child("userName").getValue(String.class);
                    Long userRankLong = dataSnapshot.child("rank").getValue(Long.class);
                    Double userScoreDouble = dataSnapshot.child("score").getValue(Double.class);

                    // Chuyển đổi từ Long sang String
                    String userRank = String.valueOf(userRankLong);

                    // Chuyển đổi từ Double sang String
                    String userScore = String.valueOf(userScoreDouble);

                    // Hiển thị thông tin lên giao diện người dùng
                    textViewUserName.setText(userName);
                    textViewRank.setText(userRank);
                    textViewScore.setText(userScore);
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
