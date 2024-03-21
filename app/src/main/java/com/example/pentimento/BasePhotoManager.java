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
    protected PhotoAdapter viewAdapter;
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

        // Add photo to gallery to load placeholder
        addToGallery(newPhoto);
        stManager.getImageById(newPhoto.getId(), new StorageActionResult<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Photo downloaded - set it to the photo to be displayed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                newPhoto.setPhoto(bitmap);
                viewAdapter.notifyDataSetChanged();
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

    public void deleteFromGallery(Photo photoToDel) {
        gallery.remove(photoToDel);
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

    public Photo getPhotoById(String id) {
        for (Photo photo : gallery) {
            if (photo.getId().equals(id)) {
                return photo;
            }
        }

        // no such photo id
        return null;
    }

    public void cleanGallery() {
        gallery.clear();
        viewAdapter.notifyDataSetChanged();
    }
}
