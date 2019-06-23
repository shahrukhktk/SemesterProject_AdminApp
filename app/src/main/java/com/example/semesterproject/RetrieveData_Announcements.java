package com.example.semesterproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.semesterproject.Model.UploadEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RetrieveData_Announcements extends AppCompatActivity implements customViewHolder.onItemClickListener
{

    RecyclerView mRecyclerView;
    DatabaseReference dbRefer;
    FirebaseStorage mStorage;
    private ValueEventListener mDBListener;

    Context mContext;
    private customViewHolder adapter;
    private List<UploadEvent> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data__announcements);

        fetchMethod_Announcments();

    }

    private void fetchMethod_Announcments() {

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id_recieved);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ANNOUNCEMENTS");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        adapter = new customViewHolder(RetrieveData_Announcements.this, mUploads);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(RetrieveData_Announcements.this);

        mStorage = FirebaseStorage.getInstance();
        dbRefer = FirebaseDatabase.getInstance().getReference("Announcements").child("Event");

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
                Toast.makeText(RetrieveData_Announcements.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(RetrieveData_Announcements.this, Update_Announcement_Event.class));
    }

    @Override
    public void DeleteClick(int position)
    {
        UploadEvent selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getmKey();
        StorageReference imgRef = mStorage.getReferenceFromUrl(selectedItem.getImageUri());
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbRefer.child("Event").child(selectedKey).setValue(null);
                Toast.makeText(mContext, "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error While Deleting the event { "+e.getMessage()+" }", Toast.LENGTH_SHORT).show();
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
