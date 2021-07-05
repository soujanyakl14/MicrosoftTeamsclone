package com.example.microsoftclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microsoftclone.R;
import com.example.microsoftclone.activities.chatActivity;
import com.example.microsoftclone.model.user;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<user> Users;
    public UserAdapter(List<user> users) {
        this.Users = users;
    }
    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view,parent,false);
        context= parent.getContext();
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.UserViewHolder holder, int position) {
 holder.setdata(Users.get(position));
 holder.itemView.setOnClickListener(v -> {
     Intent chat = new Intent(context,chatActivity.class);
     chat.putExtra("FCM_TOKEN",Users.get(position).token);
     chat.putExtra("FIRST_NAME",Users.get(position).firstname);
     chat.putExtra("LAST_NAME",Users.get(position).lastname);
     chat.putExtra("USER_ID",Users.get(position).uid);
     context.startActivity(chat);

 });
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

      class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView textUsericon,textUsername,textEmail;


        public UserViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textUsericon= itemView.findViewById(R.id.textUsericon);
            textUsername= itemView.findViewById(R.id.textUsername);
            textEmail= itemView.findViewById(R.id.textEmail);
        }
        void setdata(user User){
            textUsericon.setText(User.firstname.charAt(0)+User.lastname.substring(0,1));
            textUsername.setText(String.format("%s %s",User.firstname,User.lastname));
            textEmail.setText(User.email);
        }
    }
}
