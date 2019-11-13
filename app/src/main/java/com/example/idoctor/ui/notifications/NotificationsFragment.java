package com.example.idoctor.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.idoctor.R;
import com.example.idoctor.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

// Profile
public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ImageView mProfile;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mDescription;
    private Button mButton;
    private static final int RESULT_IMAGE=102;
    private ProgressDialog progressDialog ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);

        //Progess bar
        progressDialog = new ProgressDialog(getActivity());
        //
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // declare view to display
//        final TextView textView = root.findViewById(R.id.text_notifications);
        mProfile = root.findViewById(R.id.image_profile);
        mUserName = root.findViewById(R.id.user_name);
        mEmail = root.findViewById(R.id.user_email);

        //setup description text
        mDescription = root.findViewById(R.id.user_description);
        mDescription.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        if(width>400){
//            mDescription.setWidth(400);
//        } else{
//            mDescription.setWidth(width);
//        }


        //declare button and make event on click
        mButton = root.findViewById(R.id.onDescriptionChange);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("I'M FUKING HERE",mDescription.getText().toString());
                if(mDescription.getText()!=null){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase
                            .getReference().child("User").child(notificationsViewModel.getUserURL().getUid()).child("description");

                    databaseReference.setValue(mDescription.getText().toString());
                    Toast.makeText(getActivity(),"Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        notificationsViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Glide.with(root.getContext()).load(user.getPhotoURL()).into(mProfile);
                mUserName.setText(validName(user.getName()));
                mEmail.setText(user.getEmail());
                mDescription.setText(user.getDescription());
            }
        });

        //on click image to change avatar
        mProfile.setClickable(true);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,RESULT_IMAGE);
            }

        });
        return root;
    }
    private String validName(String name){
        String[] temp = name.split("\\s+");
        int size = temp.length;
        String result ="";
        for(int i =1;i<size;i++){
            result+=" "+temp[i];
            if(i==(size-1)){
                result+=" "+temp[0];
            }
        }
        return result;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            if (data!=null) {
                Uri uri = data.getData();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try {
                    BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                    options.inJustDecodeBounds = false;
                    Bitmap image = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    mProfile.setImageBitmap(image);
                    //upload to data storage, firebase
                    uploadStorage(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadStorage(final Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://idoctor-f023f.appspot.com");
        final StorageReference storageReference = storageRef.child("images-avatar/"+notificationsViewModel.getUserURL().getUid());
        //
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase
                                .getReference().child("User").child(notificationsViewModel.getUserURL().getUid()).child("photoURL");

                        databaseReference.setValue(uri.toString());

                    }
                });


//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//
//                Log.i("I'M FUKING HERE","AAAAAAAAAAAAAAAAAAAAAAAAA");
//
//                if(uriTask.isSuccessful()){
//
//                    String generatedFilePath = uriTask.getResult().toString();
//
//
//
//                }
//
//                Log.i("I'M FUKING HERE","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");

                progressDialog.dismiss();

                Toast.makeText(getActivity(),"Uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded"+(int)progress+"%");
                    }
                })
        ;
    }

}