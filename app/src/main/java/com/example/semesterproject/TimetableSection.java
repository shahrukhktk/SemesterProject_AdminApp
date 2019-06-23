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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.semesterproject.Model.UploadEvent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class TimetableSection extends AppCompatActivity {

    //Get Image from gallery
    final Integer Select_File = 1;
    private ImageView imageView;
    private Uri imageUri;
    private EditText eventMessageEditText;
    private ProgressBar mProgressBar;

    DatabaseReference dbReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Toolbar toolbar = findViewById(R.id.toolbarsemester);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CLASSES TIMETABLE");

        //Get Reference to DATABASE
        storageReference = FirebaseStorage.getInstance().getReference("Timetable").child("Event");
        dbReference = FirebaseDatabase.getInstance().getReference("Timetable").child("Event");

        eventMessageEditText = (EditText) findViewById(R.id.messagebox_field);
        imageView = (ImageView) findViewById(R.id.image_selected);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

    }

    private String getfileExt(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //This method will store the data into firebase
    public void PostButton(View view)
    {
        if(imageUri != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getfileExt(imageUri));
            UploadTask uploadTask = fileReference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setProgress(0);
                            }
                        }, 5000);
                        Toast.makeText(TimetableSection.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                        String imageDownloadUrl = downloadUri.toString();
                        String date = DateFormat.getDateInstance().format(new Date());

                        UploadEvent uploadObj = new UploadEvent(eventMessageEditText.getText().toString().trim(),
                                imageDownloadUrl , date);

                        String UploadId = dbReference.push().getKey();
                        dbReference.child(UploadId).setValue(uploadObj);

                        eventMessageEditText.setText(null);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void RecyclerViewData(View v)
    {
        startActivity(new Intent(TimetableSection.this, RetrieveData_TimeTable.class));
    }

    // This method will get a picture from gallery
    public void uploadImageButton(View view)
    {
        final CharSequence[] selection = {"Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(TimetableSection.this);
        builder.setTitle("Upload Image");

        builder.setItems(selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                if(selection[position].equals("Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select image"), Select_File);
                    Toast.makeText(TimetableSection.this, "Image is ready to post", Toast.LENGTH_SHORT).show();
                }
                else if(selection[position].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }

        }).show();

    }

    // This method is used to set the selected image from the gallery into the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        imageView = (ImageView)findViewById(R.id.image_selected);

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == Select_File && data != null && data.getData() != null)
            {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
                imageView.setImageURI(imageUri);
            }
        }
    }

}
