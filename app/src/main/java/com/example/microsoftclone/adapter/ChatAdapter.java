package com.example.microsoftclone.adapter;

import android.content.Context;
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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    List<Message> messages;
    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
    }

    int SENDER=0;
    int RECEIVER=1;



//
//    @NonNull
//    @NotNull
//    @Override
//    public senderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        View v= LayoutInflater.from(context).inflate(R.layout.sendermsg,parent,false);
//         return new senderViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.senderViewHolder holder, int position) {
//        holder.sendertext.setText(position);
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//    class senderViewHolder extends RecyclerView.ViewHolder {
//       public TextView sendertext;
//        public senderViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//            sendertext=itemView.findViewById(R.id.senderText);
//        }
//
//    public ChatAdapter(List<message> messages,Context context) {
//        this.messages = messages;
//        this.context=context;
//    }

//
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,int viewType) {
        if(viewType==SENDER){
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_msg,parent,false);
            return new senderViewHolder(v);
        }
        else{
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.sendermsg,parent,false);
            return new receiverViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getId()== FirebaseAuth.getInstance().getCurrentUser().getUid())
            return SENDER;
        else
            return RECEIVER;

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass()==senderViewHolder.class)
            ( (senderViewHolder)holder).sendertext.setText(messages.get(position).getMsg());
        else
            ( (receiverViewHolder)holder).receivertext.setText(messages.get(position).getMsg());

}


    public class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receivertext;
        public receiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            receivertext=itemView.findViewById(R.id.senderText);

        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
       public TextView sendertext;
        public senderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            sendertext=itemView.findViewById(R.id.receivertext);
        }

    }}

