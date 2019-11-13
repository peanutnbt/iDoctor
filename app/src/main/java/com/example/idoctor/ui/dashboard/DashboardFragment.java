package com.example.idoctor.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idoctor.R;
import com.example.idoctor.model.ChatMessage;
import com.example.idoctor.model.User;
import com.example.idoctor.ui.home.HomeViewModel;
import com.example.idoctor.ui.home.UserRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Message
public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    //
    private RecyclerView mRecyclerView;
    private ChatHistoryAdapter chatHistoryAdapter;
    private GridLayoutManager mGridLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // get list view
        //final ListView listView = root.findViewById(R.id.list_user_chatted);
        //

        mRecyclerView = root.findViewById(R.id.list_user_chatted);
        mGridLayoutManager = new GridLayoutManager(root.getContext(),1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        //

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.getListMessages().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                chatHistoryAdapter = new ChatHistoryAdapter(root.getContext(),users);
                mRecyclerView.setAdapter(chatHistoryAdapter);
                Log.i("I'm fuking here,look at me, bro",chatHistoryAdapter.getItemCount()+"");
            }


        });
        return root;
    }

}