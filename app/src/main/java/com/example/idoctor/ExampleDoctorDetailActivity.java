package com.example.idoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ExampleDoctorDetailActivity extends AppCompatActivity {

    Button button;
    TextView doctorID, text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_doctor_detail);

        button = (Button)findViewById(R.id.button);

        text1 = findViewById(R.id.textView);
        text2 = findViewById(R.id.textView2);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid()
                .equalsIgnoreCase(""+text1.getText().toString())){
            doctorID = text2;
        }else{
            doctorID = text1;
        }



        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(v.getContext(),ChatMainActivity.class);
                sendIntent.putExtra("DOCTORID", ""+doctorID.getText().toString());
                startActivity(sendIntent);
            }
        });
    }
}
