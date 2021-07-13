package com.example.microsoftclone.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//Adapter to hold messages

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.messageViewHolder> {
    List<Message> messages;
    Context context;
    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context=context;
    }

    int SENDER=0;
    int RECEIVER=1;

    @NonNull
    @NotNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,int viewType) {
        if(viewType==SENDER){
            View v= LayoutInflater.from(context).inflate(R.layout.receiver_msg,parent,false);
            return new messageViewHolder(v);
        }
        else{
            View v= LayoutInflater.from(context).inflate(R.layout.sendermsg,parent,false);
            return new messageViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return SENDER;
        else
            return RECEIVER;

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.messageViewHolder holder, int position) {
        String timeStamp = messages.get(holder.getAdapterPosition()).getTimestamp();
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String datetime= DateFormat.format("dd/MM hh:mm aa",calendar).toString();
           holder.displaymsg.setText(messages.get(position).getMsg());
           holder.timestamp.setText(datetime);

           //to display timestamp of msg on click on message and disappearing on double click
           holder.displaymsg.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(holder.timestamp.getVisibility()==View.GONE)
                       holder.timestamp.setVisibility(View.VISIBLE);
                   else
                       holder.timestamp.setVisibility(View.GONE);

           }});

}


    public class messageViewHolder extends RecyclerView.ViewHolder {
        TextView displaymsg,timestamp;
        public messageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            displaymsg=itemView.findViewById(R.id.displaymsg);
            timestamp=itemView.findViewById(R.id.timestamp);
        }
    }

   }

