package com.example.idoctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.idoctor.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "UserActivity";

    private GoogleApiClient mGoogleApiClient;

    // user uid
    private String mUID;

    // firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("User").child(mFirebaseUser.getUid());
//        String uid = mFirebaseUser.getUid();
//        mDatabaseReference = mDatabaseReference.child(mFirebaseUser.getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    User newUser = new User();
                    newUser.setUid(mFirebaseUser.getUid());
                    newUser.setPhotoURL(mFirebaseUser.getPhotoUrl().toString());
                    newUser.setRole(1);
                    newUser.setRoleName(mFirebaseUser.getDisplayName());
                    newUser.setName(mFirebaseUser.getDisplayName());
                    newUser.setDescription("Hello World");
                    newUser.setEmail(mFirebaseUser.getEmail());
                    mDatabaseReference.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG,databaseError.getMessage());
            }
        };
        mDatabaseReference.addListenerForSingleValueEvent(valueEventListener);
//        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue() == null) {
//                    User newUser = new User();
//                    newUser.setUid(mFirebaseUser.getUid());
//                    newUser.setPhotoURL(mFirebaseUser.getPhotoUrl().toString());
//                    newUser.setRole(1);
//                    newUser.setRoleName(mFirebaseUser.getDisplayName());
//                    newUser.setName(mFirebaseUser.getDisplayName());
//                    newUser.setDescription("Hello World");
//                    newUser.setEmail(mFirebaseUser.getEmail());
//                    mDatabaseReference.setValue(newUser);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.v(TAG,databaseError.getMessage());
//            }
//        });


//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.v(TAG,String.valueOf(dataSnapshot.getChildrenCount()));
//                boolean isExist = false;
//                for(DataSnapshot d:dataSnapshot.getChildren()) {
//                    User u = d.getValue(User.class);
//                    if(u.getUid().equals(mFirebaseUser.getUid())) {
//                        isExist = true;
//                        break;
//                    }
//                }
//                if(!isExist) {
//                    User newUser = new User();
//                    newUser.setUid(mFirebaseUser.getUid());
//                    newUser.setPhotoURL(mFirebaseUser.getPhotoUrl().toString());
//                    newUser.setRole(1);
//                    newUser.setRoleName(mFirebaseUser.getDisplayName());
//                    newUser.setName(mFirebaseUser.getDisplayName());
//                    newUser.setEmail(mFirebaseUser.getEmail());
//                    mDatabaseReference.push().setValue(newUser);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.v(TAG,databaseError.getMessage());
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        // check user login
        if(mFirebaseUser == null ) {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return;
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = new Intent(UserActivity.this,ExampleDoctorDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
