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

    // creating variable for edit text, textview,
    // button, progress bar and firebase auth.
    private TextInputEditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private TextView newUserTV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_s);
        // initializing all our variables.
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        newUserTV = findViewById(R.id.idTVNewUser);
        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.idPBLoading);
        // adding click listener for our new user tv.
        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line opening a login activity.
                Intent i = new Intent(IniciarS.this, Registrarse.class);
                startActivity(i);
            }
        });

        // adding on click listener for our login button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text on below line.
                String email = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                // on below line validating the text input.
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(IniciarS.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // on below line we are calling a sign in method and passing email and password to it.
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // on below line we are checking if the task is success or not.
                        if (task.isSuccessful()) {
                            // on below line we are hiding our progress bar.
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(IniciarS.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                            // on below line we are opening our mainactivity.
                            Intent i = new Intent(IniciarS.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // hiding our progress bar and displaying a toast message.
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
        // in on start method checking if
        // the user is already sign in.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // if the user is not null then we are
            // opening a main activity on below line.
            Intent i = new Intent(IniciarS.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    public static class Editar extends AppCompatActivity {

        // creating variables for our edit text, firebase database,
        // database reference, course rv modal,progress bar.
        private TextInputEditText courseNameEdt, courseDescEdt, coursePriceEdt, bestSuitedEdt, courseImgEdt, courseLinkEdt;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        ModalRV ModalRV;
        private ProgressBar loadingPB;
        // creating a string for our course id.
        private String courseID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar);
            // initializing all our variables on below line.
            Button addCourseBtn = findViewById(R.id.idBtnAddCourse);
            courseNameEdt = findViewById(R.id.idEdtCourseName);
            courseDescEdt = findViewById(R.id.idEdtCourseDescription);
            coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
            bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
            courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
            courseLinkEdt = findViewById(R.id.idEdtCourseLink);
            loadingPB = findViewById(R.id.idPBLoading);
            firebaseDatabase = FirebaseDatabase.getInstance();
            // on below line we are getting our modal class on which we have passed.
            ModalRV = getIntent().getParcelableExtra("course");
            Button deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);

            if (ModalRV != null) {
                // on below line we are setting data to our edit text from our modal class.
                courseNameEdt.setText(ModalRV.getCourseName());
                coursePriceEdt.setText(ModalRV.getCoursePrice());
                bestSuitedEdt.setText(ModalRV.getBestSuitedFor());
                courseImgEdt.setText(ModalRV.getCourseImg());
                courseLinkEdt.setText(ModalRV.getCourseLink());
                courseDescEdt.setText(ModalRV.getCourseDescription());
                courseID = ModalRV.getCourseId();
            }

            // on below line we are initialing our database reference and we are adding a child as our course id.
            databaseReference = firebaseDatabase.getReference("Courses").child(courseID);
            // on below line we are adding click listener for our add course button.
            addCourseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on below line we are making our progress bar as visible.
                    loadingPB.setVisibility(View.VISIBLE);
                    // on below line we are getting data from our edit text.
                    String courseName = courseNameEdt.getText().toString();
                    String courseDesc = courseDescEdt.getText().toString();
                    String coursePrice = coursePriceEdt.getText().toString();
                    String bestSuited = bestSuitedEdt.getText().toString();
                    String courseImg = courseImgEdt.getText().toString();
                    String courseLink = courseLinkEdt.getText().toString();
                    // on below line we are creating a map for
                    // passing a data using key and value pair.
                    Map<String, Object> map = new HashMap<>();
                    map.put("courseName", courseName);
                    map.put("courseDescription", courseDesc);
                    map.put("coursePrice", coursePrice);
                    map.put("bestSuitedFor", bestSuited);
                    map.put("courseImg", courseImg);
                    map.put("courseLink", courseLink);
                    map.put("courseId", courseID);

                    // on below line we are calling a database reference on
                    // add value event listener and on data change method
                    databaseReference.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // making progress bar visibility as gone.
                            loadingPB.setVisibility(View.GONE);
                            // adding a map to our database.
                            databaseReference.updateChildren(map);
                            // on below line we are displaying a toast message.
                            Toast.makeText(Editar.this, "Course Updated..", Toast.LENGTH_SHORT).show();
                            // opening a new activity after updating our coarse.
                            startActivity(new Intent(Editar.this, MainActivity.class));
                        }

                        public void onCancelled(@NonNull DatabaseError error) {
                            // displaying a failure message on toast.
                            Toast.makeText(Editar.this, "Fail to update course..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // adding a click listener for our delete course button.
            deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // calling a method to delete a course.
                    deleteCourse();
                }
            });

        }

        private void deleteCourse() {
            // on below line calling a method to delete the course.
            databaseReference.removeValue();
            // displaying a toast message on below line.
            Toast.makeText(this, "Course Deleted..", Toast.LENGTH_SHORT).show();
            // opening a main activity on below line.
            startActivity(new Intent(Editar.this, MainActivity.class));
        }
    }
}