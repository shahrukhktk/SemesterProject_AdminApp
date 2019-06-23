package com.example.semesterproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Dashboard_Page extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__page);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Dashboard_Page.this, Admin_Login.class));
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public void DatesheetLayout(View view)
    {
        startActivity(new Intent(Dashboard_Page.this, DatesheetsSection.class));
    }

    public void TimetableLayout(View view)
    {
        startActivity(new Intent(Dashboard_Page.this, TimetableSection.class));
    }

    public void OtherAnnouncementLayout(View view)
    {
        startActivity(new Intent(Dashboard_Page.this, OtherAnnouncementSection.class));
    }

    public void DeveloperDetailsLayout(View view)
    {
        startActivity(new Intent(Dashboard_Page.this, DeveloperDetailsSection.class));
    }
}

