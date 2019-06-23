package com.example.semesterproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Feedback_Activity extends AppCompatActivity {

    private EditText to, subject, feedback_Msg;
    private FloatingActionButton btn_send_feedback;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_);

        Toolbar toolbar = findViewById(R.id.toolbarsemester);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FEEDBACK");

        to = (EditText)findViewById(R.id.To_feedback_edittext_id);
        subject = (EditText)findViewById(R.id.subject_feedback_edittext_id);
        feedback_Msg = (EditText)findViewById(R.id.message_feedback_edittext_id);
//        to.setEnabled(false);

        cancel = (Button)findViewById(R.id.btn_cancel);
        btn_send_feedback = (FloatingActionButton)findViewById(R.id.send_feedback_btn);

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard_Page.class));
            }
        });
    }

    private void sendFeedback()
    {
        String feedback_Reciepent = to.getText().toString().trim();
        String feedback_Subject = subject.getText().toString().trim();
        String feedback_Message = feedback_Msg.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, feedback_Reciepent);
        intent.putExtra(Intent.EXTRA_SUBJECT, feedback_Subject);
        intent.putExtra(Intent.EXTRA_TEXT, feedback_Message);

        intent.setType("message/rfc22");
        startActivity(intent.createChooser(intent,"Choose an email client"));
    }
}
