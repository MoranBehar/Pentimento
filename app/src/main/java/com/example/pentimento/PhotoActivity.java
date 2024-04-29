package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import java.util.Locale;

public class PhotoActivity
        extends PhotoActivityMenusClass
        implements View.OnClickListener, editPhotoNameDialogFragment.DialogListener {

    private static final String TAG = PhotoActivity.class.getSimpleName();
    private Photo photo;

    ImageView ivPhoto, secretIcon, speakIcon;
    FrameLayout speakIconContainer;
    TextView secretMsg, tvPhotoTitle, tvPhotoOwner;
    Boolean isSecretHidden;
    GalleryManager galleryManager;
    String secretMessageText;

    ImageButton btn_photoToolbar_add, btn_photoToolbar_edit;
    TextToSpeech ttsEngine;

    private DBManager dbManager;
    private StorageManager storageManager;

    private FirebaseAuth auth;

    private BottomSheetDialog bottomSheetAlbum, bottomSheetEdit;
    private ListView lvAlbumsListView;
    private ArrayList<Album> albums;

    private AlbumAdapter adapter;
    private SecretManger secretManger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
//        bottomNav.setItemIconTintList(null);


        ivPhoto = findViewById(R.id.ivPhoto);
        tvPhotoTitle = findViewById(R.id.tvPhotoTitle);
        tvPhotoOwner = findViewById(R.id.tvPhotoOwner);
        galleryManager = GalleryManager.getInstance();
        dbManager = DBManager.getInstance();
        storageManager = StorageManager.getInstance();
        auth = FirebaseAuth.getInstance();

        // Init
        loadPhoto();
        setPhoto();
        configureTTS();
        setAddToAlbumBtn();
        setEditPhotoBtn();

        // Log view
        dbManager.addLogEntry(photo.getId(), 1);
    }


    protected int getLayoutId() {
        return R.layout.activity_photo;
    }

    private void loadPhoto() {

        // Read params
        int photoPosition = getIntent().getIntExtra("imagePosition", -1);
        String selectedPhotoId = getIntent().getStringExtra("photoId");
        String source = getIntent().getStringExtra("source");

        // If the source is a shared photo - load it from the shared manager
        if (source != null && source.equals("shared")) {
            photo = SharedPhotosManager.getInstance().getPhotoById(selectedPhotoId);
            photo.load(new Photo.actionCallback() {
                @Override
                public void onSuccess() {
                    tvPhotoTitle.setText(photo.getTitle());
                }
            });
            return;
        }

        // If the source is the main gallery - load from it
        if (photoPosition != -1) {
            photo = galleryManager.getPhotoByPosition(photoPosition);
        } else {
            if (selectedPhotoId != null) {
                photo = galleryManager.getPhotoById(selectedPhotoId);
            }
        }
    }

    private void setPhoto() {

        // Set the image resource to the ImageView
        ivPhoto.setImageBitmap(photo.getPhoto());

        // Set other fields
        tvPhotoTitle.setText(photo.getTitle());

        DBManager.getInstance().getUserById(photo.getOwnerId(), new DBManager.DBActionResult<User>() {
            @Override
            public void onSuccess(User owner) {
                tvPhotoOwner.setText(owner.getName());
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "No such photo owner");
            }
        });

        initPhotoArea();
    }

    private void initPhotoArea() {
        secretIcon = (ImageView) findViewById(R.id.ivHasSecretIcon);
        secretMsg = (TextView) findViewById(R.id.tvSecretMsg);
        speakIcon = (ImageView) findViewById(R.id.ivPlayMessage);
        speakIconContainer = (FrameLayout) findViewById(R.id.flHasSecretIconContainer);

        isSecretHidden = true;

        secretIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSecretMessage();
            }
        });

        speakIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttsStartStop();
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


        // Init secret
        secretManger = new SecretManger(this, photo, new SecretManger.secretResult() {
            @Override
            public void onSecretAdded(String msg) {
                secretMessageText = msg;
                checkSecretMessage();

                // Log secret added
                DBManager.getInstance().addLogEntry(photo.getId(), 4);

            }

            @Override
            public void onSecretDeleted() {
                recreate();
            }
        });
        extractSecretMessage();
    }


    private void setAddToAlbumBtn() {
        btn_photoToolbar_add = findViewById(R.id.btn_photoToolbar_add);
        btn_photoToolbar_add.setOnClickListener(this);
    }

    private void setEditPhotoBtn() {
        btn_photoToolbar_edit = findViewById(R.id.btn_photoToolbar_edit);
        btn_photoToolbar_edit.setOnClickListener(this);
    }

    private void extractSecretMessage() {
        secretMessageText = secretManger.getSecretMsgFromPhoto();
        checkSecretMessage();
    }
    private void toggleSecretMessage() {

        if (secretMessageText == null ) {
            extractSecretMessage();
        }

        // If there is no message, nothing to do
        if (secretMessageText == null ) return;

        float startFade = 1f;
        float endFade = 0.4f;
        float startTextSize = 0f;
        float endTextSize = 24f;


        if (!isSecretHidden) {
            startFade = 0.4f;
            endFade = 1f;
            startTextSize = 24f;
            endTextSize = 0f;
        } else {
            // Log secret view
            DBManager.getInstance().addLogEntry(photo.getId(), 3);

        }

        speakIcon.setVisibility(View.INVISIBLE);

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
        final Runnable scrambleTask = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < 25) {
                    int randomInt = (int) (Math.random() * 32) - 16;
                    secretMsg.setText(incrementChars(secretMessageText, randomInt));
                    count++;
                    // Set the delay to the next text scramble
                    handler.postDelayed(this, 50);
                } else {
                    secretMsg.setText(secretMessageText);
                    if (!isSecretHidden) {
                        speakIcon.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        // Start the scramble
        handler.post(scrambleTask);
    }

    private void checkSecretMessage() {
        if (secretMessageText != null) {
            secretMsg.setText(incrementChars(secretMessageText, 1));
            speakIconContainer.setVisibility(View.VISIBLE);
        }
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
                nextChar = (char) (currentChar + offset);
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

                    } else if (itemId == R.id.nav_secret_fragment) {
                        openSecretMenu();

                    } else if (itemId == R.id.nav_favorite_fragment) {
                        favToggle();

                    } else if (itemId == R.id.nav_delete_fragment) {
                        deletePhoto();
                    }

                    return true;
                }
            };

    private void deletePhoto() {
        checkBeforeDeleting();
    }

    private void favToggle() {
        String uid = auth.getUid();


        //get album fav(type 2) - if exist add to album, if not - create and add
        dbManager.getAlbumByType(2, uid, new DBManager.DBActionResult<Album>() {
            @Override
            public void onSuccess(Album favAlbum) {
                //adding photo to fav album
                addPhotoToAlbum(favAlbum, photo.getId());
                setFavButtonIcon();

                UIAlerts.InfoAlert("Favorite",
                        "Photo was added to your favorites album",
                        PhotoActivity.this);
            }

            @Override
            public void onError(Exception e) {
                // favorite album doesn't exist - create (type 2 - fav)
                Album favAlbum = new Album(uid, "Favorites", 2);
                dbManager.createAlbum(favAlbum, new DBManager.DBActionResult<String>() {
                    @Override
                    public void onSuccess(String albumId) {
                        //adding photo to fav album
                        favAlbum.setId(albumId);
                        favAlbum.setTitle("Favorites");
                        addPhotoToAlbum(favAlbum, photo.getId());

                        Toast.makeText(PhotoActivity.this,
                                "Photo added to favorites album", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

        Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
    }

    private void setFavButtonIcon() {
        BottomNavigationItemView btnFav = findViewById(R.id.nav_delete_fragment);
        btnFav.setChecked(true);
    }

    private void openSecretMenu() {

//        Button addSecret = findViewById(R.id.btn_add_secret);
//        Button editSecret = findViewById(R.id.btn_edit_secret);
//        Button deleteSecret = findViewById(R.id.btn_delete_secret);
//
//        if(secretManger.getSecretMsgFromPhoto() == null)
//        {
//            //no msg in the photo
//            addSecret.setEnabled(true);
//            editSecret.setEnabled(false);
//            deleteSecret.setEnabled(false);
//        }
//        else
//        {
//            //there is a msg in the photo
//            addSecret.setEnabled(false);
//            editSecret.setEnabled(true);
//            deleteSecret.setEnabled(true);
//        }

        secretManger.showBottomSheetDialog();
    }

    private void sharePhoto() {
        new SharePhoto(this, photo);
    }


    private void checkBeforeDeleting() {
        AlertDialog.Builder confirmDeletingPhoto = new AlertDialog.Builder(this, R.style.DefaultDialogStyle);

        confirmDeletingPhoto.setIcon(R.drawable.baseline_delete_24);
        confirmDeletingPhoto.setTitle("Delete this photo");
        confirmDeletingPhoto.setMessage("Are you sure you want to delete this photo from you gallery?");
        confirmDeletingPhoto.setCancelable(false);

        confirmDeletingPhoto.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photo.delete(new Photo.actionCallback() {
                    @Override
                    public void onSuccess() {
                        // remove from gallery
                        galleryManager.deleteFromGallery(photo);

                        // remove from active album
                        AlbumPhotosManager.getInstance().deleteFromGallery(photo);

                        // back to gallery
                        finish();
                    }
                });

            }
        });

        confirmDeletingPhoto.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDeletingPhoto.create().show();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_photoToolbar_add) {
            createBottomSheetAlbums();
            loadAlbumsList();
            bottomSheetAlbum.show();
        }
        if (v == btn_photoToolbar_edit) {
            createBottomSheetEdit();
            bottomSheetEdit.show();
        }
    }

    private void createBottomSheetEdit() {

        // Create bottom sheet
        View bottomSheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_edit_photo, null);
        bottomSheetEdit = new BottomSheetDialog(this);
        bottomSheetEdit.setContentView(bottomSheetView);

        // define the listener
        ViewGroup viewGroup = (ViewGroup) bottomSheetView;
        setupBottomSheetDialogButtonsListenerEdit(viewGroup, bottomSheetEdit);
    }

    private void setupBottomSheetDialogButtonsListenerEdit(ViewGroup viewGroup, BottomSheetDialog bottomSheetEdit) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            // Only set listeners to Buttons
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    if (v.getId() == R.id.btn_edit_name) {
                        openEditPhotoNameDialogFragment();
                    }
                    else if (v.getId() == R.id.btn_black_and_white_filter) {
                        setBlackAndWhiteFiler();
                    }

                    //set the photo by the changes
                    setPhoto();

                    // Dismiss the BottomSheetDialog
                    bottomSheetEdit.dismiss();
                });
            }
        }
    }

    private void setBlackAndWhiteFiler() {
        String msg;
        Bitmap bitmapWithFilter;

        //check for secret msg
        if(secretManger.getSecretMsgFromPhoto() != null)
        {
            //msg exist - extract
            msg = secretManger.getSecretMsgFromPhoto();

            //set filter on photo
            ImageProcessor imageProcessor = new ImageProcessor();
            bitmapWithFilter = imageProcessor.grayScale(photo.getPhoto(), 50);

            //set photo bitmap with the new bitmap
            photo.setPhoto(bitmapWithFilter);

            //msg exist - implement in photo
            secretManger.addSecretMsgToPhoto(bitmapWithFilter, photo, msg);
        }

        else
        {
            //set filter on photo
            ImageProcessor imageProcessor = new ImageProcessor();
            bitmapWithFilter = imageProcessor.grayScale(photo.getPhoto(), 50);

            //set photo bitmap with the new bitmap
            photo.setPhoto(bitmapWithFilter);
        }

        storageManager.updateImageInStorage(photo, new StorageActionResult() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onError(Exception e) {
                Log.d("editPhotoFilter", "onError() returned: " + photo);
            }
        });
    }

    private void openEditPhotoNameDialogFragment() {
        editPhotoNameDialogFragment dialogFragment = new editPhotoNameDialogFragment();

        FragmentActivity fragmentActivity = (FragmentActivity) PhotoActivity.this;

        // Set the listener
        dialogFragment.setDialogListener(this::onDialogDataReturn);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "YourDialogFragment");
    }

    @Override
    public void onDialogDataReturn(String photoName) {
        photo.setTitle(photoName);

        dbManager.updatePhotoTitle(photo);

        //set the ui text to the updated name
        tvPhotoTitle.setText(photoName);
    }

    private void addPhotoToAlbum(Album album, String photoId) {
        dbManager.addPhotoToAlbum(album, photoId);
        AlbumPhotosManager.getInstance().reloadAlbum(album);
    }

    private void createBottomSheetAlbums() {

        // Create bottom sheet
        View bottomSheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_albums, null);
        bottomSheetAlbum = new BottomSheetDialog(this);
        bottomSheetAlbum.setContentView(bottomSheetView);

        // Manage albums list
        lvAlbumsListView = bottomSheetAlbum.findViewById(R.id.lvAlbumsListView);

        albums = new ArrayList<>();
        adapter = new AlbumListAdapter(this, albums);

        lvAlbumsListView.setAdapter(adapter);
        lvAlbumsListView.setOnItemClickListener(albumClickListener());
    }

    private AdapterView.OnItemClickListener albumClickListener() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album selectedAlbum = albums.get(position);
                dbManager.getAlbumById(selectedAlbum.getId(), new DBManager.DBActionResult() {
                    @Override
                    public void onSuccess(Object data) {
                        addPhotoToAlbum(selectedAlbum, photo.getId());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });

                bottomSheetAlbum.cancel();
            }
        };

    }


    private void loadAlbumsList() {
        dbManager.getUserAlbums(new DBManager.DBActionResult<ArrayList>() {
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

    // Text-To-Speech
    private void configureTTS() {
        ttsEngine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                    // Set language to English
                    ttsEngine.setLanguage(Locale.US);

                    ttsEngine.setOnUtteranceProgressListener(new UtteranceProgressListener() {

                        @Override
                        public void onStart(String utteranceId) {
                            speakIcon.setImageResource(R.drawable.baseline_stop_circle_24);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            speakIcon.setImageResource(R.drawable.baseline_play_circle_24);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d(TAG, "Failed reading text");
                        }

                        @Override
                        public void onStop(String utteranceId, boolean interrupted) {
                            speakIcon.setImageResource(R.drawable.baseline_play_circle_24);
                        }

                    });
                } else {
                    Log.d(TAG, "Failed creating Text-To-Speech engine");
                }
            }
        });
    }

    // Start or stop the TTS engine
    private void ttsStartStop() {
        if (ttsEngine != null && ttsEngine.isSpeaking()) {
            ttsEngine.stop();
        } else {
            ttsEngine.speak(secretMessageText, TextToSpeech.QUEUE_FLUSH, null, "UniqueId");
        }
    }

    @Override
    // When killing the activity we must stop the TTS engine from speaking
    public void onDestroy() {
        if (ttsEngine != null) {
            ttsEngine.stop();
            ttsEngine.shutdown();
        }
        super.onDestroy();
    }

}