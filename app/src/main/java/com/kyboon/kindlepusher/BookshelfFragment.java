package com.kyboon.kindlepusher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookshelfFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookshelf, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        final TextView tv = view.findViewById(R.id.testTV);
//        ApiHelper.getInstance().search("地下城", new ApiHelperCallback<String>() {
//            @Override
//            public void onResult(String result) {
//                tv.setText(result);
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//        recyclerView = view.findViewById(R.id.rvBookshelf);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new BookshelfAdapter());
        super.onViewCreated(view, savedInstanceState);
    }
}
