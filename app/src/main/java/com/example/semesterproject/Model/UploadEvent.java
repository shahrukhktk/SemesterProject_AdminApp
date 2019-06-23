package com.example.semesterproject.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.UploadTask;

public class UploadEvent
{
    private String mMessage;
    private String mImageUri;
    private String date;
    private String mKey;

    public UploadEvent()
    {

    }

    public UploadEvent(String Message, String Image, String date)
    {
        if(Message.trim().equals(""))
        {
            Message = "No Event Message";
        }
        this.mMessage = Message;
        this.mImageUri = Image;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
