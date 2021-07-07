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
import com.example.microsoftclone.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> Users;
    public UserAdapter(List<User> users,Context context) {
        this.Users = users;
        this.context=context;
    }
    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_view,parent,false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.UserViewHolder holder, int position) {
 holder.setdata(Users.get(position));
 holder.itemView.setOnClickListener(v -> {
     Intent chat = new Intent(context,chatActivity.class);
     chat.putExtra("FCM_TOKEN",Users.get(position).getToken());
     chat.putExtra("FIRST_NAME",Users.get(position).getFirstname());
     chat.putExtra("LAST_NAME",Users.get(position).getLastname());
     chat.putExtra("USER_ID",Users.get(position).getId());
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
        public void setdata(User user){
            textUsericon.setText(String.format("%s%s",user.getFirstname().charAt(0),user.getLastname().charAt(0)));
            textUsername.setText(String.format("%s %s",user.getFirstname(),user.getLastname()));
            textEmail.setText(user.getEmail());
        }
    }
}
