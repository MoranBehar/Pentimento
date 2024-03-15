package com.example.pentimento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.UserHolder> {

    private Context context;
    private ArrayList<User> users;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_list, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserHolder holder, int position) {
        User user = users.get(position);
        holder.setDetails(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Create UserHolder Class
    class UserHolder extends RecyclerView.ViewHolder {
        private TextView userName;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
        }

        void setDetails(User user) {
            userName.setText(user.getName());
        }
    }
}