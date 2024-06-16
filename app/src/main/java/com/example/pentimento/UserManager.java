package com.example.pentimento;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class UserManager {

    private static UserManager instance;
    private User loggedInUser;
    private Album userFavAlbum;


    private DBManager dbManager;
    private FirebaseAuth auth;


    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public UserManager() {
        dbManager = DBManager.getInstance();
        auth = FirebaseAuth.getInstance();

        setLoggedInUser();
        setUserFavAlbum();
    }

    public void setLoggedInUser() {

        String uid = auth.getUid();

        dbManager.getUserById(uid, new DBManager.DBActionResult<User>() {
            @Override
            public void onSuccess(User myUser) {
                loggedInUser = myUser;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void setUserFavAlbum() {

        String uid = auth.getUid();

        //get album fav(type 2) - if exist add to album, if not - create and add
        dbManager.getAlbumByType(2, uid, new DBManager.DBActionResult<Album>() {
            @Override
            public void onSuccess(Album favAlbum) {
                userFavAlbum = favAlbum;
            }

            @Override
            public void onError(Exception e) {
                // favorite album doesn't exist - create (type 2 - fav)
                Album favoriteAlbum = new Album(uid, "Favorites", 2);
                dbManager.createAlbum(favoriteAlbum, new DBManager.DBActionResult<String>() {
                    @Override
                    public void onSuccess(String albumId) {

                         dbManager.getAlbumById(albumId, new DBManager.DBActionResult<Album>() {
                             @Override
                             public void onSuccess(Album album) {
                                 userFavAlbum = album;
                             }

                             @Override
                             public void onError(Exception e) {
                                 Log.e("userManager", "onError: get album", e);

                             }
                         });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("userManager", "onError: create album", e);
                    }
                });
            }
        });
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Album getUserFavAlbum() {
        return userFavAlbum;
    }
}
