package com.example.pentimento;

import android.util.Log;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AlbumsManager {

    private static String TAG = "AlbumsManager";

    private ArrayList<Album> albums;
    private AlbumAdapter viewAdapter;
    private DBManager dbManager;

    private String lastUpdatedAlbumId;

    private static AlbumsManager instance;

    // Implement as Singleton
    private AlbumsManager() {
        initAlbumsManager();
    }

    public static AlbumsManager getInstance() {
        if (instance == null) {
            synchronized (AlbumsManager.class) {
                if (instance == null) {
                    instance = new AlbumsManager();
                }
            }
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    private void initAlbumsManager() {
        albums = new ArrayList<Album>();
        dbManager = DBManager.getInstance();

        dbManager.getUserAlbums(new DBManager.DBActionResult<ArrayList<Album>>() {
            @Override
            public void onSuccess(ArrayList<Album> data) {
                albums.addAll(data);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void setAlbumsAdapter(AlbumAdapter adapter) {
        viewAdapter = adapter;
    }

    public ArrayList<Album> getAlbumsList() {
        return albums;
    }

    public Album getAlbumById(String albumId) {
        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                return album;
            }
        }

        return null;
    }

    public void setLastUpdatedAlbumId(String lastUpdatedAlbumId) {
        this.lastUpdatedAlbumId = lastUpdatedAlbumId;
    }

    public String getLastUpdatedAlbumId() {
        return lastUpdatedAlbumId;
    }

    public void addNewAlbum(Album newAlbum)
    {
        albums.add(newAlbum);
    }

    public void changeAlbumNumOfPics(Album albumToUpdate, int delta) {
        for (Album currentAlbum : albums) {
            if (currentAlbum.getId().equals(albumToUpdate.getId())) {
                currentAlbum.setNumOfPhotos(currentAlbum.getNumOfPhotos() + delta);
                break;
            }
        }
    }

}
