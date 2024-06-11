package com.example.tracnghiemuser.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tracnghiemuser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rankFragment extends Fragment {
    private ListView listViewLeaderboard;
    private DatabaseReference usersRef;
    private Context context; // Thêm trường Context


    public rankFragment() {
        // Required empty public constructor
    }

    public class UserScore {
        private String userId;
        private String userName;
        private double score;

        public UserScore(String userId, String userName, double score) {
            this.userId = userId;
            this.userName = userName;
            this.score = score;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public double getScore() {
            return score;
        }
    }

    public static rankFragment newInstance() {
        return new rankFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // Lưu trữ Context khi Fragment được gắn vào hoạt động
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        listViewLeaderboard = view.findViewById(R.id.listLeaderboard);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");


        // Lắng nghe sự thay đổi của score cho mỗi người dùng
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (context != null) {
                    List<UserScore> userScores = new ArrayList<>();

                    // Lặp qua tất cả các dữ liệu người dùng
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        String userName = userSnapshot.child("userName").getValue(String.class);
                        Double userScoreDouble = userSnapshot.child("score").getValue(Double.class);
                        double userScore = (userScoreDouble != null) ? userScoreDouble : 0.0;

                        // Thêm thông tin người dùng vào danh sách
                        userScores.add(new UserScore(userId, userName, userScore));

                    }

                    // Sắp xếp danh sách theo điểm số giảm dần
                    Collections.sort(userScores, new Comparator<UserScore>() {
                        @Override
                        public int compare(UserScore user1, UserScore user2) {
                            // Sử dụng compareTo để sắp xếp từ cao đến thấp
                            return Double.compare(user2.getScore(), user1.getScore());
                        }
                    });

                    // Cập nhật rank vào cơ sở dữ liệu
                    int rank = 1;
                    for (UserScore user : userScores) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("rank", rank);
                        usersRef.child(user.getUserId()).updateChildren(updates);
                        rank++;
                    }

                    List<String> leaderboardData = new ArrayList<>();
                    for (int i = 0; i < userScores.size(); i++) {
                        UserScore user = userScores.get(i);
                        String leaderboardEntry = (i + 1) + ". " + user.getUserName() + " - " + user.getScore();
                        leaderboardData.add(leaderboardEntry);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, leaderboardData);
                    listViewLeaderboard.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (context != null) {
                    Toast.makeText(context, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}