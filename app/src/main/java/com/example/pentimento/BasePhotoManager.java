package com.example.pentimento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class BasePhotoManager {

    protected FirebaseAuth fbAuth;
    protected FirebaseFirestore fbDB;
    protected StorageManager stManager;

    private ArrayList<Photo> gallery;
    private PhotoAdapter viewAdapter;

    private Consumer<String> errorCallBack;

    protected static String TAG = "BasePhotoManager";


    BasePhotoManager() {
        initGalleryManager();
    }

    private void initGalleryManager() {
        stManager = StorageManager.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();

        gallery = new ArrayList<Photo>();

        loadPhotos();
    }

    protected abstract void loadPhotos();

    public void setErrorCallBack(Consumer<String> callBack) {
        errorCallBack = callBack;
    }

    public ArrayList<Photo> getPhotosList() {
        return gallery;
    }

    public void setPhotoAdapter(PhotoAdapter adapter) {
        viewAdapter = adapter;
    }

    protected void getImageById(Photo newPhoto) {
        stManager.getImageById(newPhoto.getId(), new StorageActionResult<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                newPhoto.setPhoto(bitmap);
                addToGallery(newPhoto);
            }

            @Override
            public void onError(Exception e) {
                notifyError(e.getMessage());
            }
        });
    }

    public void addToGallery(Photo newPhoto) {
        gallery.add(newPhoto);
        viewAdapter.notifyDataSetChanged();
    }

    private void notifyError(String errorMessage) {
        if (errorCallBack != null) {
            errorCallBack.accept(errorMessage);
        }
    }

    public Photo getPhotoByPosition(int position) {
        return gallery.get(position);
    }


}
