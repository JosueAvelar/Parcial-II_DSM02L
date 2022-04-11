package com.example.parcialii;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RVAdapter.CourseClickInterface{
    private FloatingActionButton addCourseFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView courseRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ModalRV> courseRVModalArrayList;
    private RVAdapter courseRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseRV = findViewById(R.id.idRVCourses);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addCourseFAB = findViewById(R.id.idFABAddCourse);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        courseRVModalArrayList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("Courses");
        addCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Agregar.class);
                startActivity(i);
            }
        });
        courseRVAdapter = new RVAdapter(courseRVModalArrayList, this, this::onCourseClick);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);
        getCourses();
    }

    private void getCourses() {
        courseRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVModalArrayList.add(snapshot.getValue(ModalRV.class));
                courseRVAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                courseRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                courseRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCourseClick(int position) {
        displayBottomSheet(courseRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this, IniciarS.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);
        return true;
    }

    private void displayBottomSheet(ModalRV modal) {
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.paginabtm, homeRL);

        bottomSheetTeachersDialog.setContentView(layout);

        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        bottomSheetTeachersDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView courseDescTV = layout.findViewById(R.id.idTVCourseDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView priceTV = layout.findViewById(R.id.idTVCoursePrice);
        ImageView courseIV = layout.findViewById(R.id.idIVCourse);

        courseNameTV.setText(modal.getCourseName());
        courseDescTV.setText(modal.getCourseDescription());
        suitedForTV.setText( modal.getBestSuitedFor());
        priceTV.setText( modal.getCoursePrice());
        Picasso.get().load(modal.getCourseImg()).into(courseIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, IniciarS.Editar.class);

                i.putExtra("course", modal);
                startActivity(i);
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getCourseLink()));
                startActivity(i);
            }
        });
    }
}