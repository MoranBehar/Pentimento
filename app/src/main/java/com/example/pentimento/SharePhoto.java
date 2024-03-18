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
    private UsersAdapter friendListAdapter;
    private ArrayList<User> friendsList;
    private DBManager dbManager;
    public SharePhoto(Activity activity, Photo photo) {
        this.myActivity = activity;
        this.photoToShare = photo;

        dbManager = DBManager.getInstance();

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
        friendsList =  new ArrayList<>();

        // Configure friends list view (RecyclerView)
        friendsListView = bottomSheetShare.findViewById(R.id.friendsListView);
        friendsListView.setLayoutManager(new LinearLayoutManager(bottomSheetShare.getContext()));
        friendListAdapter = new UsersAdapter(myActivity, friendsList, userClickListener);
        friendsListView.setAdapter(friendListAdapter);
        friendsListView.addItemDecoration(new DividerItemDecoration(myActivity, LinearLayoutManager.VERTICAL));

        // Configure share buttons listeners
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
        dbManager.sharePhotoToUser(photoId,toUserId);
        Toast.makeText(bottomSheetShare.getContext(), "Shared", Toast.LENGTH_SHORT).show();
    }

    private void loadFriendsList() {

        dbManager.getFriends(new DBActionResult<ArrayList>() {
            @Override
            public void onSuccess(ArrayList data) {
                friendsList.addAll(data);
                friendListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });
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
    }



}
