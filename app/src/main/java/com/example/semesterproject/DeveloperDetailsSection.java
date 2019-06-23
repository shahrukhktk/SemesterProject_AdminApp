package com.example.semesterproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.semesterproject.Model.UploadEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class DeveloperDetailsSection extends AppCompatActivity {

    ImageButton dev_picture, showDetails, hideDetails;
    LinearLayout dev__details;
    TextView name, skills, education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        Toolbar toolbar = findViewById(R.id.toolbarsemester);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DEVELOPER DETAILS");

        name = (TextView)findViewById(R.id.dev_name);
        skills = (TextView)findViewById(R.id.dev_skills);
        education = (TextView)findViewById(R.id.dev_student);

        dev__details = (LinearLayout)findViewById(R.id.details_frame);

        dev_picture = (ImageButton)findViewById(R.id.developer_pictureButton);
        showDetails = (ImageButton)findViewById(R.id.show_details);
        hideDetails = (ImageButton)findViewById(R.id.hide_details);

        showDetails.setEnabled(true);
        hideDetails.setEnabled(false);

        dev_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeveloperDetailsSection.this, "SHAH RUKH KHAN", Toast.LENGTH_SHORT).show();
            }
        });

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(R.string.developerName);
                skills.setText(R.string.developerSkills);
                education.setText(R.string.studyingAt);

                hideDetails.setEnabled(true);
                showDetails.setEnabled(false);
            }
        });

        hideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(null);
                skills.setText(null);
                education.setText(null);

                hideDetails.setEnabled(false);
                showDetails.setEnabled(true);
            }
        });

    }

    public void FeedbackBTN(View view)
    {
        startActivity(new Intent(getApplicationContext(), Feedback_Activity.class));
    }
}
