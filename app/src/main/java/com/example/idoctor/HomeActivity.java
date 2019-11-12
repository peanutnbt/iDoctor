package com.example.idoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.idoctor.model.Status;
import com.example.idoctor.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private GridView gridViewDoctor;
    private TextView mShowMore;
    public ArrayList<User> users;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private FirebaseListAdapter<Status> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        readData();

        mShowMore = findViewById(R.id.showMoreMess);
        mShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    public void readData(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("User");

        Query lastQuery = myRef.orderByChild("role").equalTo(1).limitToLast(4);


        users = new ArrayList<>();
        System.out.println("DMMMMMMMMMMMMMMMMMMMMM");
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC");
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+keyNode);
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                    System.out.println("AAAAAAAAAAA: "+user.getName());
                }

                createGrid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UUUUUUUUUUUUUUUUUUUUUU"+databaseError.toString());
            }
        });

    }

    public void createGrid(){
        gridViewDoctor = (GridView) findViewById(R.id.gridView);
        GridDoctorAdapter gridDoctorAdapter = new GridDoctorAdapter(this,users);
        gridViewDoctor.setAdapter(gridDoctorAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_menu_item:
                Intent intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
