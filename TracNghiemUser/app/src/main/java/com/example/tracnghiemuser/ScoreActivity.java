package com.example.tracnghiemuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tracnghiemuser.databinding.ActivityScoreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users").child(currentUser.getUid());

        int correct = getIntent().getIntExtra("correctAnsw", 0);
        int totalQuestion = getIntent().getIntExtra("totalQuestion", 0);

        int wrong = totalQuestion - correct;
        double score = (double)(correct * 10) / totalQuestion;

        binding.totalRight.setText(String.valueOf(correct));
        binding.totalWrong.setText(String.valueOf(wrong));
        binding.totalQuestion.setText(String.valueOf(totalQuestion));
        binding.score.setText(String.valueOf(score));
        binding.progressBar.setProgress(totalQuestion);
        binding.progressBar.setProgress(correct);
        binding.progressBar.setProgressMax(totalQuestion);

        // Lấy điểm số cao nhất hiện tại từ cơ sở dữ liệu Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy điểm số cao nhất hiện tại
                    Double highestScore = dataSnapshot.child("highestScore").getValue(Double.class);
                    if (highestScore != null) {
                        // Kiểm tra nếu điểm mới cao hơn điểm cao nhất hiện tại
                        if (score > highestScore) {
                            // Lưu điểm số mới vào cơ sở dữ liệu
                            usersRef.child("highestScore").setValue(score);
                            Log.d("ScoreActivity", "Điểm số mới đã được lưu vào cơ sở dữ liệu");
                        } else {
                            Log.d("ScoreActivity", "Điểm số mới không cao hơn điểm cao nhất hiện tại");
                        }
                    } else {
                        Log.e("ScoreActivity", "Giá trị highestScore từ cơ sở dữ liệu là null");
                    }
                    // Cập nhật điểm số của người dùng trong cơ sở dữ liệu
                    usersRef.child("score").setValue(score);
                } else {
                    Log.e("ScoreActivity", "Nút người dùng không tồn tại trong cơ sở dữ liệu");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong việc đọc dữ liệu từ cơ sở dữ liệu
                Log.e("ScoreActivity", "Lỗi khi đọc dữ liệu từ cơ sở dữ liệu: " + databaseError.getMessage());
            }
        });

        binding.btnretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.backScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}