package com.example.pentimento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.UserHolder> {

    private Context context;
    private ArrayList<User> users;
    private OnItemClickListener listener;

    public UsersAdapter(Context context, ArrayList<User> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_list, parent, false);
        return new UserHolder(view, listener);
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

        public UserHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                        listener.onItemClick(position);
                    }
                }
            });
        }

        void setDetails(User user) {
            userName.setText(user.getName());
        }

    }
}