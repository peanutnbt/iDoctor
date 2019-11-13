package com.example.idoctor.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idoctor.HomeActivity;
import com.example.idoctor.R;
import com.example.idoctor.SearchActivity;
import com.example.idoctor.model.User;

import java.util.List;

// Home
public class HomeFragment extends Fragment {

    private TextView mShowMore;
    private RecyclerView mRecyclerView;
    private UserRecyclerAdapter mUserRecyclerAdapter;
    private GridLayoutManager mGridLayoutManager;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
//        viewModel.getArticles().observe(this, new Observer<List<User>>() {
//            @Override
//            public void onChanged(@Nullable List<User> users) {
//                System.out.println("0000000000000000");
//                System.out.println("user: "+ users.get(0).getName());
//                System.out.println("user: "+ users.get(1).getName());
//                System.out.println("user: "+ users.get(2).getName());
//                System.out.println("user: "+ users.get(3).getName());
////                mRecyclerView.setAdapter(new UserRecyclerAdapter(getContext(),users));
//                mUserRecyclerAdapter = new UserRecyclerAdapter(getContext(),users);
//            }
//        });
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        mShowMore = root.findViewById(R.id.showMoreMess);
        mShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView = root.findViewById(R.id.recycler_view);
//        return root;

//        mRecyclerView = root.findViewById(R.id.users_recycle);
        mGridLayoutManager = new GridLayoutManager(root.getContext(),getResources().getInteger(R.integer.course_grid_span));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
//        mRecyclerView.setAdapter(mUserRecyclerAdapter);
//        homeViewModel.getArticles().observe(this, new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                mUserRecyclerAdapter = new UserRecyclerAdapter(root.getContext(),users);
//                mRecyclerView.setAdapter(mUserRecyclerAdapter);
//            }
//        });
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getArticles().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                System.out.println("0000000000000000");
                System.out.println("user: "+ users.get(0).getName());
                System.out.println("user: "+ users.get(1).getName());
                System.out.println("user: "+ users.get(2).getName());
                System.out.println("user: "+ users.get(3).getName());
//                mRecyclerView.setAdapter(new UserRecyclerAdapter(getContext(),users));
                mUserRecyclerAdapter = new UserRecyclerAdapter(root.getContext(),users);
                mRecyclerView.setAdapter(mUserRecyclerAdapter);

            }
        });

        return root;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
//        viewModel.getArticles().observe(this, new Observer<List<User>>() {
//            @Override
//            public void onChanged(@Nullable List<User> users) {
//                System.out.println("0000000000000000");
//                System.out.println("user: "+ users.get(0).getName());
//                System.out.println("user: "+ users.get(1).getName());
//                System.out.println("user: "+ users.get(2).getName());
//                System.out.println("user: "+ users.get(3).getName());
//                mRecyclerView.setAdapter(new UserRecyclerAdapter(getContext(),users));
//            }
//        });
//    }

}