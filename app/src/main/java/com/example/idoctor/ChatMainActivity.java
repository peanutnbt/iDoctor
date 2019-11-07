package com.example.idoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idoctor.model.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.CharArrayWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ChatMainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;

//    FloatingActionButton fab;
    //Add emoji
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    String doctorID;

    FloatingActionButton fab;

    ImageView image_button;

    private DatabaseReference root1,root2 ;

    private String temp_key;


    //start upload image
    private String checker = "",myUrl="";
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog loadingBar;


    //  2 function to start displayChatMessage() : run  populateView(..)
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 438 && resultCode==RESULT_OK && data!=null && data.getData()!= null){

            loadingBar.setTitle("Sending Image");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri = data.getData();

            if (!checker.equalsIgnoreCase("image")){

            }else if (checker.equalsIgnoreCase("image")){

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images file");
                System.out.println(storageReference);

                DatabaseReference userMessageKeyRef = FirebaseDatabase.getInstance().getReference().child("Messages")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(doctorID).push();


                String messPushID = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference.child(messPushID+"."+ "jpg");

                uploadTask = filePath.putFile(fileUri);

                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            Uri downloadUrl  = task.getResult();
                            System.out.println("Download URl : "+downloadUrl.toString());
                            myUrl = downloadUrl.toString();



                            //get root to insert data
                            // user's root
                            root1 = FirebaseDatabase.getInstance().getReference().child("Messages")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(doctorID);
                            //doctor's root
                            root2 = FirebaseDatabase.getInstance().getReference().child("Messages")
                                    .child(doctorID)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());




                                    ChatMessage chatMessage = new ChatMessage(myUrl,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),checker);

                                    //insert to user's root
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Messages")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(doctorID)
                                            .push()
                                            .setValue(chatMessage);

                                    //insert to doctor's root
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Messages")
                                            .child(doctorID)
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .push()
                                            .setValue(chatMessage);


                                    BubbleTextView  bubbleTextView = (BubbleTextView)findViewById(R.id.message_text);

                                    emojiconEditText.setText("");

                                    loadingBar.dismiss();



                        }
                    }
                });
            }else{
                loadingBar.dismiss();
                Toast.makeText(this,"Nothing. Error",Toast.LENGTH_LONG).show();
            }
        }
        displayChatMessage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);


        image_button = findViewById(R.id.image_button);

        loadingBar = new ProgressDialog(this);

        image_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF files",
                                "Ms Word Files"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatMainActivity.this);

                builder.setTitle("Select the file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            checker = "image";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),438);



                        }
                        if (i == 1){
                            checker = "pdf";

                        }
                        if (i == 2){
                            checker = "docx";

                        }
                    }
                });

                builder.show();

            }
        });



        //get doctorID from doctor's detail Page
        doctorID = getIntent().getStringExtra("DOCTORID");
        //start activity: display chat box
        displayChatMessage();
        //sign out button Demo
        fab = findViewById(R.id.signout_button);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChatMainActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });


        //get RelativeLayout
        activity_main = (RelativeLayout)findViewById(R.id.activity_chat_main);


        //get root to insert data
        // user's root
        root1 = FirebaseDatabase.getInstance().getReference().child("Messages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(doctorID);
        //doctor's root
        root2 = FirebaseDatabase.getInstance().getReference().child("Messages")
                .child(doctorID)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());




        //get Imojicon to insert data
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emoji_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main, emojiconEditText, emojiButton);
        emojIconActions.ShowEmojIcon();

        submitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                ChatMessage chatMessage = new ChatMessage(emojiconEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),"text");

                //insert to user's root
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Messages")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(doctorID)
                        .push()
                        .setValue(chatMessage);

                //insert to doctor's root
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Messages")
                        .child(doctorID)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push()
                        .setValue(chatMessage);


                BubbleTextView  bubbleTextView = (BubbleTextView)findViewById(R.id.message_text);


                emojiconEditText.setText("");

            }
        });



        //check if not sign in
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this,"Not sign in",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Wellcome"+FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
            //load message

            displayChatMessage();
        }



    }


    private void displayChatMessage(){
        ListView listView = findViewById(R.id.listView);


        //get root to query to
        Query query = FirebaseDatabase.getInstance().getReference().child("Messages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(doctorID);

        //The error said the constructor expected FirebaseListOptions:
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.list_item)
                .build();


        adapter = new FirebaseListAdapter<ChatMessage>(options) {

            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView messText, messUser, messTime;
                String messType;
                //get data from firebase databse
                messText = (BubbleTextView) v.findViewById(R.id.message_text);
                messUser = (TextView) v.findViewById(R.id.message_user);
                messTime = (TextView) v.findViewById(R.id.message_time);
                ImageView imageView = v.findViewById(R.id.image);
                messType = model.getType();

                messUser.setText(model.getMessUser()+"");
                //if mess is text
                if(messType.equalsIgnoreCase("text")) {
                    messText.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    messText.setText(model.getMessText() + "");


                }else{
                    if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                            .equalsIgnoreCase(messUser.getText()
                                    .toString()))                       {


                        imageView.setVisibility(View.VISIBLE);
                        messText.setVisibility(View.GONE);
                        Picasso.get().load(model.getMessText())
                                .resize(90, 90)
                                .centerCrop()
                                .into(imageView);

                    }
                }

                //format date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                messTime.setText(""+simpleDateFormat.format(new Date(model.getMessTime())));
            }
        };

        //display all adapter on listView
        listView.setAdapter(adapter);
    }
}
