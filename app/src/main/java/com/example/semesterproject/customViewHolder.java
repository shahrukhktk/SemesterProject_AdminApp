package com.example.semesterproject;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semesterproject.Model.UploadEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class customViewHolder extends RecyclerView.Adapter<customViewHolder.customViewHolderadapter>
{
    private Context mContext;
    private List<UploadEvent> list;
    private onItemClickListener mListener;

    public customViewHolder(Context context, List<UploadEvent> alist)
    {
        mContext = context;
        list = alist;
    }

    @Override
    public customViewHolderadapter onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_recyclerview_cardview, viewGroup, false);
        return new customViewHolderadapter(view);
    }

    @Override
    public void onBindViewHolder(final customViewHolderadapter customViewHolder, int i) {
        UploadEvent uploadObject = list.get(i);
        customViewHolder.Message.setText(uploadObject.getMessage());
        customViewHolder.Date.setText(uploadObject.getDate());
        Picasso.get().load(uploadObject.getImageUri())
                .fit().placeholder(R.drawable.upload_image_icon)
                .into(customViewHolder.Image);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class customViewHolderadapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
    , MenuItem.OnMenuItemClickListener {
        TextView Message, Date;
        ImageView Image;
        View mview;

        public customViewHolderadapter(View itemView) {
            super(itemView);
            mview = itemView;
            Message = mview.findViewById(R.id.textView_msg);
            Date = mview.findViewById(R.id.date);
            Image = mview.findViewById(R.id.imageRecieved);

            mview.setOnClickListener(this);
            mview.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mListener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Action");
            MenuItem updateEvent = menu.add(Menu.NONE, 1,1,"UPDATE");
            MenuItem deleteEvent = menu.add(Menu.NONE, 2,2,"DELETE");

            updateEvent.setOnMenuItemClickListener(this);
            deleteEvent.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                   switch (item.getItemId())
                   {
                       case 1:
                           mListener.UpdateClick(position);
                           return true;
                       case 2:
                           mListener.DeleteClick(position);
                           return true;
                   }
                }
            }
            return false;
        }
    }

    public interface onItemClickListener
    {
        void OnItemClick(int position);
        void UpdateClick(int position);
        void DeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        mListener = listener;
    }

}

