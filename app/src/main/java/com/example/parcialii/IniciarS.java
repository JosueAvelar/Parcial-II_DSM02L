package com.example.parcialii;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class IniciarS extends AppCompatActivity {

    private TextInputEditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private TextView newUserTV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_s);
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        newUserTV = findViewById(R.id.idTVNewUser);
        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.idPBLoading);

        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IniciarS.this, Registrarse.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String email = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(IniciarS.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(IniciarS.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(IniciarS.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(IniciarS.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(IniciarS.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    public static class Editar extends AppCompatActivity {

        private TextInputEditText courseNameEdt, courseDescEdt, coursePriceEdt, bestSuitedEdt, courseImgEdt, courseLinkEdt;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        ModalRV ModalRV;
        private ProgressBar loadingPB;
        private String courseID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar);
            Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
            courseNameEdt = findViewById(R.id.idEdtCourseName);
            courseDescEdt = findViewById(R.id.idEdtCourseDescription);
            coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
            bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
            courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
            courseLinkEdt = findViewById(R.id.idEdtCourseLink);
            loadingPB = findViewById(R.id.idPBLoading);
            firebaseDatabase = FirebaseDatabase.getInstance();
            ModalRV = getIntent().getParcelableExtra("course");
            Button deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);

            if (ModalRV != null) {
                courseNameEdt.setText(ModalRV.getCourseName());
                coursePriceEdt.setText(ModalRV.getCoursePrice());
                bestSuitedEdt.setText(ModalRV.getBestSuitedFor());
                courseImgEdt.setText(ModalRV.getCourseImg());
                courseLinkEdt.setText(ModalRV.getCourseLink());
                courseDescEdt.setText(ModalRV.getCourseDescription());
                courseID = ModalRV.getCourseId();
            }
            databaseReference = firebaseDatabase.getReference("Courses").child(courseID);
            addCourseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingPB.setVisibility(View.VISIBLE);
                    String courseName = courseNameEdt.getText().toString();
                    String courseDesc = courseDescEdt.getText().toString();
                    String coursePrice = coursePriceEdt.getText().toString();
                    String bestSuited = bestSuitedEdt.getText().toString();
                    String courseImg = courseImgEdt.getText().toString();
                    String courseLink = courseLinkEdt.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("courseName", courseName);
                    map.put("courseDescription", courseDesc);
                    map.put("coursePrice", coursePrice);
                    map.put("bestSuitedFor", bestSuited);
                    map.put("courseImg", courseImg);
                    map.put("courseLink", courseLink);
                    map.put("courseId", courseID);

                    databaseReference.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            loadingPB.setVisibility(View.GONE);
                            databaseReference.updateChildren(map);
                            Toast.makeText(Editar.this, "Course Updated..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Editar.this, MainActivity.class));
                        }

                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Editar.this, "Fail to update course..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCourse();
                }
            });

        }

        private void deleteCourse() {
            databaseReference.removeValue();
            Toast.makeText(this, "Course Deleted..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Editar.this, MainActivity.class));
        }
    }
}