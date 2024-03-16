package com.example.pentimento;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class SharePhoto {

    private Activity myActivity;
    private Photo photoToShare;
    private BottomSheetDialog bottomSheetShare;
    private RecyclerView friendsListView;
    private UsersAdapter adapter;
    private ArrayList<User> friendsList;
    public SharePhoto(Activity activity, Photo photo) {
        this.myActivity = activity;
        this.photoToShare = photo;

        createBottomSheet();
        loadFriendsList();
        bottomSheetShare.show();
    }


    private void createBottomSheet() {

        // Create bottom sheet
        View bottomSheetView = myActivity.getLayoutInflater().inflate(R.layout.bottom_sheet_share, null);
        bottomSheetShare = new BottomSheetDialog(myActivity);
        bottomSheetShare.setContentView(bottomSheetView);

        // Manage friends list
        friendsListView = bottomSheetShare.findViewById(R.id.friendsListView);
        friendsListView.setLayoutManager(new LinearLayoutManager(bottomSheetShare.getContext()));
        friendsList =  new ArrayList<>();
        adapter = new UsersAdapter(myActivity, friendsList, userClickListener);
        friendsListView.setAdapter(adapter);
        friendsListView.addItemDecoration(new DividerItemDecoration(myActivity, LinearLayoutManager.VERTICAL));

        MaterialButton emailBtn = bottomSheetShare.findViewById(R.id.btn_email);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToEmail();
            }
        });

        MaterialButton whatsAppBtn = bottomSheetShare.findViewById(R.id.btn_whatsapp);
        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWhatsApp();
            }
        });

    }

    private OnItemClickListener userClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            User selectedFriend = friendsList.get(position);
            showShareApprovalDialog(selectedFriend);
            bottomSheetShare.cancel();
        }
    };

    private void shareToWhatsApp() {
        Toast.makeText(bottomSheetShare.getContext(), "COMING SOON - Share to WhatsApp", Toast.LENGTH_SHORT).show();
    }

    private void shareToEmail() {
        Toast.makeText(bottomSheetShare.getContext(), "COMING SOON - Share to Email", Toast.LENGTH_SHORT).show();
    }

    private void shareToPentimentoUser(String photoId, String toUserId) {
        DBManager.getInstance().sharePhotoToUser(photoId,toUserId);
        Toast.makeText(bottomSheetShare.getContext(), "Shared", Toast.LENGTH_SHORT).show();
    }

    private void loadFriendsList() {
        User user1 = new User("dlkfjhdskjfghdfkjhgljkdfgh","a@a.com", "Gil Behar", "050-12345456", 50);
        User user2 = new User("dlkfjhdskjfghdfkjhgljkdfgh","b@b.com", "Moran Behar", "050-12345456", 20);
        User user3 = new User("dlkfjhdskjfghdfkjhgljkdfgh","a@a.com", "Princess234", "050-12345456", 50);
        User user4 = new User("dlkfjhdskjfghdfkjhgljkdfgh","a@a.com", "TheKing", "050-12345456", 50);

        friendsList.add(user1);
        friendsList.add(user2);
        friendsList.add(user3);
        friendsList.add(user4);
        friendsList.add(user1);
        friendsList.add(user2);
        friendsList.add(user3);
        friendsList.add(user4);
        friendsList.add(user1);
        friendsList.add(user2);
        friendsList.add(user3);
        friendsList.add(user4);
    }

    private void showShareApprovalDialog(User sharedTo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
        builder.setTitle("Sharing to a Pentimento user");
        builder.setMessage("You are about to share this picture with " + sharedTo.getName());

        // Share button
        builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareToPentimentoUser(photoToShare.getId(), sharedTo.getId());
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button shareButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        shareButton.setTextColor(ContextCompat.getColor(myActivity, R.color.black));
        Button cancelButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(ContextCompat.getColor(myActivity, R.color.black));
    }



}
