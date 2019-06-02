package com.kyboon.kindlepusher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyboon.kindlepusher.DataAdapters.BookAdapter;
import com.kyboon.kindlepusher.DataTypes.Ranking;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment implements BookAdapter.IBookAdapter {

    BookAdapter bookAdapter;

    ChipGroup chipGroupGender;
    ChipGroup chipGroupRanking;
    ChipGroup chipGroupTime;

    List<Ranking> maleRankings = new ArrayList<>();
    List<Ranking> femaleRankings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ranking, container, false);

        chipGroupGender = rootView.findViewById(R.id.chipGroupGender);
        chipGroupRanking = rootView.findViewById(R.id.chipGroupRanking);
        chipGroupTime = rootView.findViewById(R.id.chipGroupTime);

        RecyclerView recyclerView = rootView.findViewById(R.id.rvBook);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookAdapter = new BookAdapter(this, getContext());
        recyclerView.setAdapter(bookAdapter);

        setUpChips();

        return rootView;
    }

    private void setUpChips() {
        for (String str : Constants.RANKING_GENDERS) {
            chipGroupGender.addView(makeChip(str));
        }
        chipGroupGender.setOnCheckedChangeListener(chipListener);
        chipGroupGender.setTag(0);

        for (String str : Constants.RANKING_NAMES) {
            chipGroupRanking.addView(makeChip(str));
        }
        chipGroupRanking.setOnCheckedChangeListener(chipListener);
        chipGroupRanking.setTag(1);

        for (String str : Constants.RANKING_TIME) {
            chipGroupTime.addView(makeChip(str));
        }
        chipGroupTime.setOnCheckedChangeListener(chipListener);
        chipGroupTime.setTag(2);
    }

    private Chip makeChip(String text) {
        Chip chip = new Chip(getContext());
        chip.setText(text);
        chip.setChipBackgroundColorResource(R.color.chip_selector);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setCheckedIconVisible(false);
        return chip;
    }

    private ChipGroup.OnCheckedChangeListener chipListener = new ChipGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(ChipGroup chipGroup, int i) {
//            switch ((int)chipGroup.getTag()) {
//                case 0:
//                    Log.d("debuggg", "gender");
//                    break;
//                case 1:
//                    Log.d("debuggg", "ranking");
//                    break;
//                case 2:
//                    Log.d("debuggg", "time");
//                    break;
//                default:
//            }
//
//            Log.d("debuggg", "chip: " + i);

            for (int j = 0; j < chipGroup.getChildCount(); ++j) {
                Chip chip = (Chip) chipGroup.getChildAt(j);
                chip.setClickable(!chip.isChecked());
            }

            if (chipGroupGender.getCheckedChipId() == View.NO_ID || chipGroupRanking.getCheckedChipId() == View.NO_ID || chipGroupTime.getCheckedChipId() == View.NO_ID)
                return;

        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && maleRankings.size() == 0 && femaleRankings.size() == 0)
            getRankings();
    }

    private void getRankings() {
        ApiHelper.getInstance().getRankings(new ApiHelperCallback<NovelApi.RankingsWrapper>() {
            @Override
            public void onResult(NovelApi.RankingsWrapper result) {
                maleRankings.clear();
                femaleRankings.clear();
                for (Ranking ranking : result.male) {
                    if (ranking.collapse == false) {
                        maleRankings.add(ranking);
                        Log.d("debuggg", ranking.shortTitle);
                    }
                }

                for (Ranking ranking : result.female) {
                    if (ranking.collapse == false) {
                        femaleRankings.add(ranking);
                        Log.d("debuggg", ranking.shortTitle);
                    }
                }

                ((Chip)chipGroupGender.getChildAt(0)).setChecked(true);
                ((Chip)chipGroupRanking.getChildAt(0)).setChecked(true);
                ((Chip)chipGroupTime.getChildAt(0)).setChecked(true);
            }

            @Override
            public void onError() {

            }
        });
    }


    @Override
    public void selectedBook(String id, View sharedImageView, View sharedTextView) {

    }
}
