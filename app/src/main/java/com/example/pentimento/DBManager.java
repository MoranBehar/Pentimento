package com.example.pentimento;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DBManager {

    private static String TAG = "DBManager";
    private static DBManager instance;

    private FirebaseAuth fbAuth;
    private FirebaseFirestore fbDB;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageManager storageManager;

    //Implement Singleton
    private DBManager() {
        initDBManager();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }


    private void initDBManager() {
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        storageManager = StorageManager.getInstance();
    }

    public void connectImageToCurrentUser(String imageId) {
        connectImageToUser(imageId, fbAuth.getUid());
    }

    public void connectImageToUser(String imageId, String userId) {
        Map<String, Object> image = new HashMap<>();
        image.put("id", imageId);
        image.put("Creator", userId);

        fbDB.collection("UserPhotos").document().set(image)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void sharePhotoToUser(String photoId, String toUserId) {

        Map<String, Object> shareObject = new HashMap<>();
        shareObject.put("photoId", photoId);
        shareObject.put("sharedBy", fbAuth.getUid());
        shareObject.put("sharedTo", toUserId);
        shareObject.put("sharedOn", getCurrentDate());

        fbDB.collection("PhotoSharing").document().set(shareObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void createAlbum(String albumName, String userId) {

        Album newAlbum = new Album(userId, albumName);

        DocumentReference newAlbumRef = fbDB.collection("Albums").document();
        newAlbum.setId(newAlbumRef.getId());
        newAlbumRef.set(newAlbum)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void addPhotoToAlbum(Album album, String photoId) {

        AlbumPhoto albumPhoto = new AlbumPhoto(album.getId(), photoId);

        fbDB.collection("AlbumPhotos").document().set(albumPhoto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (album.getNumOfPhotos() == 0) {
                            album.setAlbumCoverId(photoId);
                        }
                        album.setNumOfPhotos(album.getNumOfPhotos() + 1);
                        updateAlbum(album);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    public void getUserAlbums(DBActionResult callback) {
        String uid = fbAuth.getUid();

        CollectionReference colRef = fbDB.collection("Albums");
        colRef.whereEqualTo("ownerId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Create a list of Album objects
                        ArrayList<Album> albumList = new ArrayList<>();

                        //get the albums
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Album newAlbum = document.toObject(Album.class);
                            ;
                            albumList.add(newAlbum);
                        }

                        callback.onSuccess(albumList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getAlbumById(String albumId, DBActionResult callback) {
        CollectionReference colRef = fbDB.collection("Albums");

        colRef.document(albumId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Album album = document.toObject(Album.class);
                                callback.onSuccess(album);
                            } else {
                                Log.d(TAG, "No such album");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void updateAlbum(Album albumToUpdate) {

        DocumentReference albumDocRef =
                fbDB.collection("Albums")
                        .document(albumToUpdate.getId());

        // Overwrite the document with the new Album object
        albumDocRef.set(albumToUpdate)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.d(TAG, "Album successfully updated");
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Log.w(TAG, "Album updating album", e);
                });
    }

    public void updateAlbumNumOfPhotos(String albumId, int change) {

        DocumentReference albumDocRef =
                fbDB.collection("Albums")
                        .document(albumId);

        // Overwrite the document with the new Album object
        albumDocRef.update("numOfPhotos", FieldValue.increment(change))
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.d(TAG, "Album successfully updated");
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Log.w(TAG, "Album updating album", e);
                });
    }

    public void getFriends(DBActionResult<ArrayList> callback) {

        String uid = fbAuth.getUid();

        CollectionReference colRef = fbDB.collection("users");
        colRef.whereNotEqualTo("id", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Create a list of Album objects
                        ArrayList<User> usersList = new ArrayList<User>();

                        // extract the users
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User friend = document.toObject(User.class);
                            usersList.add(friend);
                        }

                        callback.onSuccess(usersList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getUserById(String userId, DBActionResult callback) {

        CollectionReference colRef = fbDB.collection("users");

        colRef.document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                callback.onSuccess(user);
                            } else {
                                Log.d(TAG, "No such user");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    public void photoSharingRealTimeUpdates(DBActionResult<QueryDocumentSnapshot> callback) {

        CollectionReference colRef = fbDB.collection("PhotoSharing");

        colRef.whereEqualTo("sharedTo", fbAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listener failed.", e);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New photo shared: " + dc.getDocument().getData());
                                    callback.onSuccess(dc.getDocument());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Share Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }


    public void getPhotosAlbums(Photo photo, DBActionResult<ArrayList> callback) {

        String uid = fbAuth.getUid();

        CollectionReference colRef = fbDB.collection("AlbumPhotos");
        colRef.whereNotEqualTo("photoId", photo.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Create a list of Album objects
                        ArrayList<String> albumsList = new ArrayList<String>();

                        // extract the users
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> row = document.getData();
                            String albumId = row.get("albumId").toString();
                            albumsList.add(albumId);
                        }

                        callback.onSuccess(albumsList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void deletePhotoFromAlbum(Photo photoToDelete, Album albumToDeleteFrom) {

        CollectionReference colRef = fbDB.collection("AlbumPhotos");

        // Get all the documents associating album to photo
        colRef
                .whereEqualTo("albumId", albumToDeleteFrom.getId())
                .whereEqualTo("photoId", photoToDelete.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //  Delete this document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fbDB.collection("AlbumPhotos").document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void deletePhotoSharing(Photo photoToUnShare) {

        CollectionReference colRef = fbDB.collection("PhotoSharing");

        // Get all the documents associating album to photo
        colRef
                .whereEqualTo("photoId", photoToUnShare.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //  Delete this document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fbDB.collection("PhotoSharing").document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void deletePhotoFromOwner(Photo photoToRemove) {

        CollectionReference colRef = fbDB.collection("UserPhotos");

        // Get all the documents associating album to photo
        colRef
                .whereEqualTo("id", photoToRemove.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //  Delete this document
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fbDB.collection("UserPhotos").document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void getAlbumsByPhotoId(Photo photoInAlbum, DBActionResult<String> callBack) {

        String albumId = "";

        CollectionReference colRef = fbDB.collection("AlbumPhotos");
        colRef.whereEqualTo("photoId", photoInAlbum.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> row = document.getData();

                            String albumId = row.get("albumId").toString();
                        }

                        callBack.onSuccess(albumId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
