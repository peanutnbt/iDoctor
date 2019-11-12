package com.example.idoctor.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idoctor.R;
import com.example.idoctor.model.User;

import java.util.List;

// Home
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private UserRecyclerAdapter mUserRecyclerAdapter;
    private GridLayoutManager mGridLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        mRecyclerView = root.findViewById(R.id.users_recycle);
        mGridLayoutManager = new GridLayoutManager(root.getContext(),getResources().getInteger(R.integer.course_grid_span));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        homeViewModel.getText().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mUserRecyclerAdapter = new UserRecyclerAdapter(root.getContext(),users);
                mRecyclerView.setAdapter(mUserRecyclerAdapter);
            }
        });
        return root;
    }

}