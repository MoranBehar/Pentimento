package com.example.pentimento;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AlbumsManager {

    private static String TAG = "AlbumsManager";

    private ArrayList<Album> albums;
    private AlbumAdapter viewAdapter;
    private DBManager dbManager;

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

    private void initAlbumsManager() {
        albums = new ArrayList<Album>();
        dbManager = DBManager.getInstance();

        dbManager.getUserAlbums(new DBManager.DBActionResult<ArrayList>() {
            @Override
            public void onSuccess(ArrayList data) {
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

    public Album getAlbumByPosition(int position) {
        return albums.get(position);
    }


}
