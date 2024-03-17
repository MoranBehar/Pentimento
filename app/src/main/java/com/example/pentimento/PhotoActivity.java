package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PhotoActivity extends PhotoActivityMenusClass implements View.OnClickListener {

    private static final String TAG = PhotoActivity.class.getSimpleName();
    private Photo photo;

    ImageView ivPhoto, secretIcon;
    TextView secretMsg;
    Boolean isSecretHidden;
    GalleryManager gm;
    String secretMessageText;

    ImageButton btn_photoToolbar_add;

    private DBManager dbManager;

    private BottomSheetDialog bottomSheetShare;
    private ListView lvAlbumsListView;
    private ArrayList<Album> albums;

    private AlbumAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        ivPhoto = findViewById(R.id.ivPhoto);
        gm = GalleryManager.getInstance();
        dbManager = DBManager.getInstance();


        // Get the image resource position from the intent
        int imageSrcPosition = getIntent().getIntExtra("imagePosition", -1);
        if (imageSrcPosition != -1) {

            //get the image src
            photo = gm.getImageByPosition(imageSrcPosition);

            // Set the image resource to the ImageView
            ivPhoto.setImageBitmap(photo.getPhoto());
        }

        // Init photo area
        initPhotoArea();

        addToAlbumBtn();
    }

    private void addToAlbumBtn() {
        btn_photoToolbar_add = findViewById(R.id.btn_photoToolbar_add);
        btn_photoToolbar_add.setOnClickListener(this);
    }

    private void extractSecretMessage() {
        secretMessageText = "Moran, this message is for your eyes only. Please do not share this with the enemy.";
    }

    protected int getLayoutId() {
        return R.layout.activity_photo;
    }

    private void initPhotoArea() {
        secretIcon = (ImageView) findViewById(R.id.ivHasSecretIcon);
        secretMsg = (TextView) findViewById(R.id.tvSecretMsg);
        isSecretHidden = true;

        secretIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSecretMessage();
            }
        });

        secretMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSecretMessage();
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSecretHidden) toggleSecretMessage();
            }
        });



        // Get secret
        extractSecretMessage();

        // Show scrambled text
        secretMsg.setText(incrementChars(secretMessageText, 1));

    }

    private void toggleSecretMessage() {

        float startFade = 1f;
        float endFade = 0.4f;
        float startTextSize = 0f;
        float endTextSize = 24f;


        if (!isSecretHidden) {
            startFade = 0.4f;
            endFade = 1f;
            startTextSize = 24f;
            endTextSize = 0f;
        }

        isSecretHidden = !isSecretHidden;

        // Fade in/out photo
        ValueAnimator photoAnimator = ValueAnimator.ofFloat(startFade, endFade);
        photoAnimator.setDuration(600);
        photoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ivPhoto.setAlpha((float) animation.getAnimatedValue());
            }
        });

        // Zoom in/out message
        ValueAnimator secretAnimator = ValueAnimator.ofFloat(startTextSize, endTextSize);
        secretAnimator.setDuration(600);
        secretAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                secretMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, animatedValue);
            }
        });

        photoAnimator.start();
        secretAnimator.start();
        scrambleSecretText();
    }

    private void scrambleSecretText() {

        final Handler handler = new Handler();
        final Runnable updateTask = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < 25) {
                    int randomInt = (int)(Math.random() * 32) - 16;
                    secretMsg.setText(incrementChars(secretMessageText, randomInt));
                    count++;
                    // Schedule the next run
                    handler.postDelayed(this, 50); // Adjust the delay as needed
                } else {
                    secretMsg.setText(secretMessageText);
                }
            }
        };

        handler.post(updateTask); // Start the updates
    }

    private String incrementChars(String input, int offset) {

        StringBuilder result = new StringBuilder();

        // Iterate through each character in the input string
        for (int i = 0; i < input.length(); i++) {
            // Get the current character
            char currentChar = input.charAt(i);
            char nextChar = currentChar;

            // Convert the current character to its ASCII value and increment by 1
            if (!Character.isSpaceChar(currentChar)) {
                nextChar = (char)(currentChar + offset);
            }

            // Append the next character to the result
            result.append(nextChar);
        }

        // Convert the StringBuilder back to a string and return it
        return result.toString();
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_share_fragment) {
                        sharePhoto();
                    }
                    else if (itemId == R.id.nav_secret_fragment) {
                        secretManger();
                    }
                    else if (itemId == R.id.nav_favorite_fragment) {
                        favToggle();
                    }
                    else if (itemId == R.id.nav_delete_fragment) {
                        deletePhoto();
                    }

                    return true;
                }
            };

    private void deletePhoto() {
        checkBeforeDeleting();
    }

    private void favToggle() {
        Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
    }

    private void secretManger() {
        Toast.makeText(this, "secret", Toast.LENGTH_SHORT).show();
    }

    private void sharePhoto() {
        new SharePhoto(this, photo);
    }


    private void checkBeforeDeleting() {
        AlertDialog.Builder confirmDeletingPhoto = new AlertDialog.Builder(this);

        confirmDeletingPhoto.setIcon(R.drawable.baseline_delete_24);
        confirmDeletingPhoto.setTitle("Delete this photo");
        confirmDeletingPhoto.setMessage("Are you sure you want to delete this photo from you gallery?");
        confirmDeletingPhoto.setCancelable(false);

        confirmDeletingPhoto.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDeletingProcess();
            }
        });

        confirmDeletingPhoto.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDeletingPhoto.create().show();
    }

    private void startDeletingProcess() {
    }

    @Override
    public void onClick(View v) {
        if(v == btn_photoToolbar_add){

            createBottomSheet();
            loadAlbumsList();
            bottomSheetShare.show();
        }
    }

    private void addPhotoToAlbum(String albumId, String photoId){
        dbManager.addPhotoToAlbum(albumId, photoId);
    }

    private void createBottomSheet() {

        // Create bottom sheet
        View bottomSheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_albums, null);
        bottomSheetShare = new BottomSheetDialog(this);
        bottomSheetShare.setContentView(bottomSheetView);

        // Manage albums list
        lvAlbumsListView = bottomSheetShare.findViewById(R.id.lvAlbumsListView);

        albums =  new ArrayList<>();
        adapter = new AlbumAdapter(this, albums);

        lvAlbumsListView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener userClickListener() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedAlbum = albums.get(position);
                dbManager.getAlbumById(selectedAlbum.getId(), new DBActionResult() {
                    @Override
                    public void onSuccess(Object data) {
                        //TODO
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                bottomSheetShare.cancel();
            }
        };

    }


    private void loadAlbumsList() {
        dbManager.getUserAlbums(new DBActionResult<ArrayList>() {
            @Override
            public void onSuccess(ArrayList data) {
                albums.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}