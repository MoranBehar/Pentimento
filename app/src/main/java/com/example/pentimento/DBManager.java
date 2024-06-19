package com.example.pentimento;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public interface DBActionResult<T> {
        void onSuccess(T data);

        void onError(Exception e);
    }

    private void initDBManager() {
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        storageManager = StorageManager.getInstance();
    }

    public void savePhoto(Photo photo) {
        Map<String, Object> image = new HashMap<>();
        image.put("id", photo.getId());
        image.put("ownerId", photo.getOwnerId());
        image.put("title", photo.getTitle());
        image.put("createDate", photo.getCreateDate());
        image.put("locationLatitude", photo.getLocationLatitude());
        image.put("locationLongitude", photo.getLocationLongitude());

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

    public void updatePhotoTitle(Photo photo) {
        // Reference to the "UserPhotos" collection
        CollectionReference userPhotosRef =
                fbDB.collection("UserPhotos");

        // Create a query to find the document where the photoId
        Query query = userPhotosRef.whereEqualTo("id", photo.getId());

        // Execute the query
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the Id of the document
                                String documentId = document.getId();

                                // Update the title field in the document
                                DocumentReference docRef = userPhotosRef.document(documentId);

                                docRef.update("title", photo.getTitle())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Field updated successfully
                                                    Log.i(TAG, "onComplete: title updated");
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Failed to execute query
                            Log.i(TAG, "onComplete: title hasn't changed");
                        }
                    }
                });

    }

    public void updateAlbumTitle(Album album) {
        // Reference to the "UserPhotos" collection
        CollectionReference userAlbumsRef =
                fbDB.collection("Albums");

        // Create a query to find the document where the photoId
        Query query = userAlbumsRef.whereEqualTo("id", album.getId());

        // Execute the query
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the Id of the document
                                String documentId = document.getId();

                                // Update the title field in the document
                                DocumentReference docRef = userAlbumsRef.document(documentId);

                                docRef.update("title", album.getTitle())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Field updated successfully
                                                    Log.i(TAG, "onComplete: title updated");
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Failed to execute query
                            Log.i(TAG, "onComplete: title hasn't changed");
                        }
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

    public void createAlbum(Album newAlbum, DBActionResult<String> callback) {

        DocumentReference newAlbumRef = fbDB.collection("Albums").document();
        newAlbum.setId(newAlbumRef.getId());
        newAlbumRef.set(newAlbum)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onSuccess(newAlbumRef.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(null);
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
                        AlbumsManager.getInstance().changeAlbumNumOfPics(album, 1);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Exception " + e.getMessage());
                    }
                });
    }


    public void getUserAlbums(DBActionResult callback) {
        String uid = fbAuth.getUid();

        CollectionReference colRef = fbDB.collection("Albums");
        colRef.whereEqualTo("ownerId", uid)
                .orderBy("type", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Create a list of Album objects
                        ArrayList<Album> albumList = new ArrayList<>();

                        //get the albums
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Album newAlbum = document.toObject(Album.class);
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

    public void getAlbumByType(int albumType, String uid, DBActionResult<Album> callback) {
        fbDB.collection("Albums")
                .whereEqualTo("type", albumType)
                .whereEqualTo("ownerId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                //callback null - no album with that type
                                callback.onError(null);
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        // newAlbum contains the data from document
                                        Album album = document.toObject(Album.class);

                                        //callback the album info
                                        callback.onSuccess(album);
                                    }
                                }
                            }
                        } else {
                            //exception
                            Log.d(TAG, "Error getting album: ", task.getException());
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

        CollectionReference colRef = fbDB.collection("Users");
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

        if (userId == null) {
            callback.onError(new Exception("User id is null"));
            return;
        }

        CollectionReference colRef = fbDB.collection("Users");
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

    public void getPhotoById(String photoId, DBActionResult<Photo> callback) {

        if (photoId == null) {
            callback.onError(new Exception("Photo id is null"));
            return;
        }

        CollectionReference colRef = fbDB.collection("UserPhotos");
        colRef.whereEqualTo("id", photoId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                //callback null - no photo with this id
                                callback.onError(null);
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Photo photo = document.toObject(Photo.class);
                                        callback.onSuccess(photo);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }


    public void getPhotosAlbums(Photo photo, DBActionResult<ArrayList> callback) {

        // Create a list of Album objects
        ArrayList<String> albumsList = new ArrayList<String>();

        CollectionReference colRef = fbDB.collection("AlbumPhotos");
        colRef.whereEqualTo("photoId", photo.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // extract the albums
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
                        callback.onError(e);
                    }
                });
    }

    public void deletePhotoFromAlbum(String photoId, String albumId) {

        CollectionReference colRef = fbDB.collection("AlbumPhotos");

        // Get all the documents associating album to photo
        colRef
                .whereEqualTo("albumId", albumId)
                .whereEqualTo("photoId", photoId)
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

                                                getAlbumById(albumId, new DBActionResult<Album>() {
                                                    @Override
                                                    public void onSuccess(Album album) {

                                                        //set the num of photos in this album
                                                        album.setNumOfPhotos(album.getNumOfPhotos() - 1);
                                                        if (album.getNumOfPhotos() == 0) {
                                                            album.setAlbumCoverId(null);
                                                        }

                                                        updateAlbum(album);
                                                        AlbumsManager.getInstance().changeAlbumNumOfPics(album, -1);
                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.e(TAG,
                                                                "onError: didn't get album by his id",
                                                                e);
                                                    }
                                                });

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

        // Get all the UserPhoto document
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


    // Log activities in the app
    // 1 - Photo View
    // 2 - Album View
    // 3 - Secret Views
    // 4 - Secret Added
    public void addLogEntry(String itemId, int type) {

        String userId = fbAuth.getUid();

        Map<String, Object> logView = new HashMap<>();
        logView.put("itemId", itemId);
        logView.put("userId", userId);
        logView.put("viewDate", new Date());
        logView.put("type", type);

        fbDB.collection("ActivityLog").document().set(logView)
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

//    public void getTopPhotoViewed(int topX, int lastXDays, DBActionResult<List<Map.Entry<String, Integer>>> callBack) {
//
//        // Get current userId
//        String uid = fbAuth.getUid();
//
//        // Get the time range date and time
//        Date now = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(now);
//        cal.add(Calendar.DAY_OF_MONTH, -lastXDays);
//        Date daysAgo = cal.getTime();
//
//        CollectionReference colRef = fbDB.collection("ActivityLog");
//
//
//        // Query to get views in the last "lastXDays" days, grouped by photoId
//        colRef
//                .whereEqualTo("userId", uid)
//                .whereGreaterThan("viewDate", daysAgo)
////                .orderBy("viewDate", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Map<String, Integer> photoViewCounts = new HashMap<>();
//
//                        // Count the views per photoId
//                        task.getResult().forEach(
//                                document -> {
//                                    String photoId = document.getString("photoId");
//                                    photoViewCounts.put(photoId, photoViewCounts.getOrDefault(photoId, 0) + 1);
//                                });
//
//
//                        // Find the top "topX" viewed photos
//                        List<Map.Entry<String, Integer>> topViewedList = new ArrayList<>(photoViewCounts.entrySet());
//                        topViewedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
//
//                        callBack.onSuccess(topViewedList.subList(0, topX));
//
//                    } else {
//                        Log.e(TAG, "Failed getting view log data");
//                    }
//                });
//    }

    public void getLogCount(int lastXDays, int type, DBActionResult<Long> callBack) {

        // Get current userId
        String uid = fbAuth.getUid();

        // Get the time range date and time
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -lastXDays);
        Date daysAgo = cal.getTime();

        CollectionReference colRef = fbDB.collection("ActivityLog");

        AggregateQuery countQuery = colRef
                .whereEqualTo("userId", uid)
                .whereEqualTo("type", type)
                .whereGreaterThan("viewDate", daysAgo)
                .count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(
                new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the count and send back the results
                            AggregateQuerySnapshot snapshot = task.getResult();
                            callBack.onSuccess(snapshot.getCount());
                        } else {
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }

    public void getPhotoByAlbumId(String albumId, Photo photo, DBActionResult<Boolean> callBack) {


        CollectionReference colRef = fbDB.collection("AlbumPhotos");
        colRef.whereEqualTo("albumId", albumId)
                .whereEqualTo("photoId", photo.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                //not found
                                callBack.onError(null);
                            }
                            else {
                                //Photo was found in this album
                                callBack.onSuccess(true);
                            }



                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

}
