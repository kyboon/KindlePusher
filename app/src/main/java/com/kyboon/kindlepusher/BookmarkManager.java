package com.kyboon.kindlepusher;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kyboon.kindlepusher.DataTypes.Bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface BookmarkManagerCallback {
    void onSuccess();
    void onError();
}

public class BookmarkManager {

    private static BookmarkManager instance;
    private CollectionReference bookshelfReference;
    private static HashMap<String, Bookmark> bookmarkHashMap = new HashMap<>();

    static BookmarkManager getInstance() {
        if (instance == null)
            return new BookmarkManager();
        else
            return instance;
    }

    private BookmarkManager() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build());
        bookshelfReference = firebaseFirestore.collection("Users").document(userId).collection("Bookshelf");
    }

    public void addOrUpdateBookmark(Bookmark bookmark, BookmarkManagerCallback callback) {
        for (Map.Entry<String, Bookmark> entry: bookmarkHashMap.entrySet()) {
            if (entry.getValue().bookId == bookmark.bookId) {
                updateBookmark(entry.getKey(), bookmark, callback);
                return;
            }
        }
        addBookmark(bookmark, callback);
    }

    private void updateBookmark(String id, Bookmark bookmark, BookmarkManagerCallback callback) {

    }

    private void addBookmark(final Bookmark bookmark, final BookmarkManagerCallback callback) {
        Log.v("BookmarkManager","Adding bookmark for book with id: " + bookmark.bookId);
        final DocumentReference documentReference = bookshelfReference.document();
        documentReference.set(bookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    bookmarkHashMap.put(documentReference.getId(), bookmark);
                    Log.v("BookmarkManager","Added bookmark, id: " + documentReference.getId());
                    callback.onSuccess();
                } else {
                    Log.w("BookmarkManager", "addBookmark() task unsuccessful");
                    callback.onError();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("BookmarkManager", "addBookmark() Error: " + e.getMessage());
                callback.onError();
            }
        });
    }

    public List<Bookmark> getBookmarks() {
        List<Bookmark> bookmarks = new ArrayList<>();
        for (Bookmark bookmark: bookmarkHashMap.values()) {
            bookmarks.add(bookmark);
        }
        return bookmarks;
    }

    public void syncBookmarks(final BookmarkManagerCallback callback) {
        Log.v("BookmarkManager", "Syncing bookmarks");
        bookshelfReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookmarkHashMap.clear();
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                        bookmarkHashMap.put(documentSnapshot.getId(), documentSnapshot.toObject(Bookmark.class));
                    }
                    callback.onSuccess();
                } else {
                    Log.w("BookmarkManager", "syncBookmarks() task unsuccessful");
                    callback.onError();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("BookmarkManager", "syncBookmarks() Error: " + e.getMessage());
                callback.onError();
            }
        });
    }
}
