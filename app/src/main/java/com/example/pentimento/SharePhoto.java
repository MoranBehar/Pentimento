package com.example.pentimento;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class SharePhoto {

    private Activity myActivity;
    private BottomSheetDialog bottomSheetShare;
    private RecyclerView friendsListView;
    private UsersAdapter adapter;
    private ArrayList<User> friendsList;
    public SharePhoto(Activity activity) {
        this.myActivity = activity;

        View bottomSheetView = myActivity.getLayoutInflater().inflate(R.layout.bottom_sheet_share, null);
        bottomSheetShare = new BottomSheetDialog(myActivity);
        bottomSheetShare.setContentView(bottomSheetView);
        bottomSheetShare.show();

        friendsListView = myActivity.findViewById(R.id.friendsListView);
        friendsListView.setLayoutManager(new LinearLayoutManager(myActivity));
        friendsList =  new ArrayList<>();
        adapter = new UsersAdapter(myActivity, friendsList);
        friendsListView.setAdapter(adapter);
        friendsListView.addItemDecoration(new DividerItemDecoration(myActivity, LinearLayoutManager.VERTICAL));

        createFriendsList();
    }

    private void createFriendsList() {
        User user1 = new User("a@a.com", "user1", "050-12345456", 50);
        User user2 = new User("b@b.com", "user2", "050-12345456", 20);
        friendsList.add(user1);
        friendsList.add(user2);

    }
}
