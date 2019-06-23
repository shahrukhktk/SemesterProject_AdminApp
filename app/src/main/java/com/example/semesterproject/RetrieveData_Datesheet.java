package com.example.semesterproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.semesterproject.Model.UploadEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetrieveData_Datesheet extends AppCompatActivity implements customViewHolder.onItemClickListener
{

    RecyclerView mRecyclerView;
    private DatabaseReference dbRefer;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private ValueEventListener mDBListener;

    Context mContext;

    private customViewHolder adapter;
    private List<UploadEvent> mUploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_datesheet);

        fetchMethod_Datesheet();
    }

    private void fetchMethod_Datesheet() {

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id_recieved);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DATESHEET SECTION");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        adapter = new customViewHolder(RetrieveData_Datesheet.this, mUploads);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(RetrieveData_Datesheet.this);

        mStorage = FirebaseStorage.getInstance();
        dbRefer = FirebaseDatabase.getInstance().getReference("Exams Datesheets").child("Event");

        mDBListener = dbRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadEvent uploadedpic = postSnapshot.getValue(UploadEvent.class);
                    uploadedpic.setmKey(dataSnapshot.getKey());
                    mUploads.add(uploadedpic);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RetrieveData_Datesheet.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    public void OnItemClick(int position)
    {
        Toast.makeText(mContext, "OnItemClick" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void UpdateClick(int position)
    {
        Toast.makeText(mContext, "UPDATE" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DeleteClick(int position) {
        UploadEvent selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getmKey();
        StorageReference imgRef = mStorage.getReferenceFromUrl(selectedItem.getImageUri());
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbRefer.child(selectedKey).setValue(null);
                Toast.makeText(mContext, "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dbRefer.removeEventListener(mDBListener);
    }
}