package com.example.semesterproject;

import android.content.Context;
import android.content.Intent;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semesterproject.Model.UploadEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class customViewHolder extends RecyclerView.Adapter<customViewHolder.customViewHolderadapter> {
    private Context mContext;
    private List<UploadEvent> list;

    public customViewHolder(Context context, List<UploadEvent> alist) {
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
        Toast.makeText(mContext, uploadObject.mKey, Toast.LENGTH_SHORT).show();
        customViewHolder.Message.setText(uploadObject.getMessage());
        customViewHolder.Date.setText(uploadObject.getDate());
        Picasso.get().load(uploadObject.getImageUri())
                .fit().placeholder(R.drawable.upload_image_icon)
                .into(customViewHolder.Image);

        inflateMenu(customViewHolder, i);


    }

    private void inflateMenu(customViewHolderadapter obj, final int position) {

        final PopupMenu menu = new PopupMenu(mContext, obj.Image);
        menu.inflate(R.menu.menu_opt);

        obj.Image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                menu.show();
                return true;
            }
        });

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_update :
                        {
                            Intent intent = new Intent(mContext, Update_Announcement_Event.class);

                            break;
                        }
                    case R.id.item_delete :
                        {
                            FirebaseDatabase.getInstance().getReference("Exams Datesheets").child("Event").child(list.get(position).mKey).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        break;
                    }
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class customViewHolderadapter extends RecyclerView.ViewHolder{
        TextView Message, Date;
        ImageView Image;
        View mview;

        public customViewHolderadapter(View itemView) {
            super(itemView);
            mview = itemView;
            Message = mview.findViewById(R.id.textView_msg);
            Date = mview.findViewById(R.id.date);
            Image = mview.findViewById(R.id.imageRecieved);
        }

//        @Override
//        public void onClick(View v)
//        {
//            if(mListener != null)
//            {
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION)
//                {
//                    mListener.OnItemClick(position);
//                }
//            }
//        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Action");
//            MenuItem updateEvent = menu.add(Menu.NONE, 1,1,"UPDATE");
//            MenuItem deleteEvent = menu.add(Menu.NONE, 2,2,"DELETE");
//
//            updateEvent.setOnMenuItemClickListener(this);
//            deleteEvent.setOnMenuItemClickListener(this);
//        }

//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            if(mListener != null)
//            {
//                int position = getAdapterPosition();
//                if(position != RecyclerView.NO_POSITION)
//                {
//                   switch (item.getItemId())
//                   {
//                       case 1:
//                           mListener.UpdateClick(position);
//                           return true;
//                       case 2:
//                           mListener.DeleteClick(position);
//                           return true;
//                   }
//                }
//            }
//            return false;
//        }
    }

//    public interface onItemClickListener {
//        void OnItemClick(int position);
//
//        void UpdateClick(int position);
//
//        void DeleteClick(int position);
//    }
//
//    public void setOnItemClickListener(onItemClickListener listener) {
//        mListener = listener;
//    }

}

