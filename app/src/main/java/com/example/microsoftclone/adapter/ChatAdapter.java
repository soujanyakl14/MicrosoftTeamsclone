package com.example.microsoftclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.model.message;
import com.example.microsoftclone.model.user;
import com.example.microsoftclone.utilities.PreferenceManager;
import com.example.microsoftclone.utilities.constants;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.webrtc.ContextUtils.getApplicationContext;

public class ChatAdapter extends RecyclerView.Adapter {
    List<message> messages;
    Context context;
    PreferenceManager preferenceManager=new PreferenceManager(context.getApplicationContext());
    public ChatAdapter(List<message> messages,Context context) {
        this.messages = messages;
        this.context=context;
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
        context=parent.getContext();
        if(viewType==SENDER){
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.sendermsg,parent,false);
            return new senderViewHolder(v);
        }
        else{
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_msg,parent,false);
            return new receiverViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getId()==preferenceManager.getString(constants.KEY_USER_ID))
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
            receivertext=itemView.findViewById(R.id.receivertext);

        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
       public TextView sendertext;
        public senderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            sendertext=itemView.findViewById(R.id.senderText);
        }

    }}

