package com.example.idoctor.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.idoctor.R;
import com.example.idoctor.model.User;

// Profile
public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ImageView mProfile;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // declare view to display
//        final TextView textView = root.findViewById(R.id.text_notifications);
        mProfile = root.findViewById(R.id.image_profile);
        mUserName = root.findViewById(R.id.user_name);
        mEmail = root.findViewById(R.id.user_email);
        mDescription = root.findViewById(R.id.user_description);
        notificationsViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Glide.with(root.getContext()).load(user.getPhotoURL()).into(mProfile);
                mUserName.setText(user.getName());
                mEmail.setText(user.getEmail());
                mDescription.setText(user.getDescription());
            }
        });
        return root;
    }
}