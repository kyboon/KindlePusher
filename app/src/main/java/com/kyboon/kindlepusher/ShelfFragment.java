package com.kyboon.kindlepusher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kyboon.kindlepusher.Adapters.BookmarkAdapter;
import com.kyboon.kindlepusher.DataTypes.Bookmark;
import com.kyboon.kindlepusher.DataTypes.Bookshelf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShelfFragment extends Fragment implements BookmarkAdapter.IBookmarkAdapter {

    BookmarkAdapter bookmarkAdapter;
    RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shelf, container, false);
        recyclerView = rootView.findViewById(R.id.rvBookmark);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkAdapter = new BookmarkAdapter(this, getContext());
        recyclerView.setAdapter(bookmarkAdapter);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && bookmarkAdapter!=null && bookmarkAdapter.getItemCount() == 0)
            getBookmarks();
    }

    private void getBookmarks() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Data").document("Bookshelf");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
//                    Bookshelf bookshelf = (Bookshelf) documentSnapshot.getData().get("Bookshelf");
                    Bookshelf bookshelf = documentSnapshot.toObject(Bookshelf.class);
//                    List<HashMap<String, String>> bookmarkList = (List<HashMap<String, String>>) documentSnapshot.getData().get("Bookmark");
                    Log.d("debuggg", "" + bookshelf);

//                    List<Bookmark> bookmarks = new ArrayList<>();
                    bookmarkAdapter.setBookmarkList(bookshelf.getBookmarks());
                }
            }
        });
    }

    @Override
    public void selectedBook(String id, View sharedImageView, View sharedTextView) {

    }
}
