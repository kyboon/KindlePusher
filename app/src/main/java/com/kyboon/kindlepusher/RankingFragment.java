package com.kyboon.kindlepusher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kyboon.kindlepusher.DataAdapters.BookAdapter;
import com.kyboon.kindlepusher.DataTypes.Book;
import com.kyboon.kindlepusher.DataTypes.Ranking;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment implements BookAdapter.IBookAdapter {

    BookAdapter bookAdapter;

    ChipGroup chipGroupGender;
    ChipGroup chipGroupRanking;
    ChipGroup chipGroupTime;

    ProgressBar progressBar;

    List<Ranking> maleRankings = new ArrayList<>();
    List<Ranking> femaleRankings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ranking, container, false);

        chipGroupGender = rootView.findViewById(R.id.chipGroupGender);
        chipGroupRanking = rootView.findViewById(R.id.chipGroupRanking);
        chipGroupTime = rootView.findViewById(R.id.chipGroupTime);
        progressBar = rootView.findViewById(R.id.progress_circular);

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

        for (String str : Constants.RANKING_NAMES) {
            chipGroupRanking.addView(makeChip(str));
        }
        chipGroupRanking.setOnCheckedChangeListener(chipListener);

        for (String str : Constants.RANKING_TIME) {
            chipGroupTime.addView(makeChip(str));
        }
        chipGroupTime.setOnCheckedChangeListener(chipListener);
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

            for (int j = 0; j < chipGroup.getChildCount(); ++j) {
                Chip chip = (Chip) chipGroup.getChildAt(j);
                chip.setClickable(!chip.isChecked());
            }

            if (chipGroupGender.getCheckedChipId() == View.NO_ID || chipGroupRanking.getCheckedChipId() == View.NO_ID || chipGroupTime.getCheckedChipId() == View.NO_ID)
                return;

            List<Ranking> selectedGender;
            if (((Chip) chipGroupGender.getChildAt(0)).isChecked())
                selectedGender = maleRankings;
            else
                selectedGender = femaleRankings;

            String selectedRanking = "";
            for (int j = 0; j < chipGroupRanking.getChildCount(); ++j) {
                Chip chip = (Chip) chipGroupRanking.getChildAt(j);
                if (chip.isChecked())
                    selectedRanking = chip.getText().toString();
            }
            selectedRanking += "榜";

            int selectedTime = 0;
            for (int j = 0; j < chipGroupTime.getChildCount(); ++j) {
                Chip chip = (Chip) chipGroupTime.getChildAt(j);
                if (chip.isChecked())
                    selectedTime = j;
            }

            String selectedRankingId = "";
            for (Ranking ranking: selectedGender) {
                if (ranking.shortTitle.equals(selectedRanking)) {
                    if (selectedTime == 1 && ranking.monthRank != null && !ranking.monthRank.isEmpty())
                        selectedRankingId = ranking.monthRank;
                    else if (selectedTime == 2 && ranking.totalRank != null && !ranking.totalRank.isEmpty())
                        selectedRankingId = ranking.totalRank;
                    else
                        selectedRankingId = ranking.id;
                }
            }
            Log.d("debuggg", selectedRankingId);
            getRankingResult(selectedRankingId);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && bookAdapter != null && maleRankings.size() == 0 && femaleRankings.size() == 0)
            getRankings();
    }

    private void getRankings() {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getRankings(new ApiHelperCallback<NovelApi.RankingsWrapper>() {
            @Override
            public void onResult(NovelApi.RankingsWrapper result) {
                progressBar.setVisibility(View.GONE);
                maleRankings.clear();
                femaleRankings.clear();
                for (Ranking ranking : result.male) {
                    if (ranking.collapse == false) {
                        maleRankings.add(ranking);
                    }
                }

                for (Ranking ranking : result.female) {
                    if (ranking.collapse == false) {
                        femaleRankings.add(ranking);
                    }
                }

                ((Chip)chipGroupGender.getChildAt(0)).setChecked(true);
                ((Chip)chipGroupRanking.getChildAt(0)).setChecked(true);
                ((Chip)chipGroupTime.getChildAt(0)).setChecked(true);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getRankingResult(String id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getRankingResult(id, new ApiHelperCallback<List<Book>>() {
            @Override
            public void onResult(List<Book> result) {
                progressBar.setVisibility(View.GONE);
                bookAdapter.setBookList(result);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void selectedBook(String id, final View sharedImageView, final View sharedTextView) {
        progressBar.setVisibility(View.VISIBLE);
        ApiHelper.getInstance().getBook(id, new ApiHelperCallback<Book>() {
            @Override
            public void onResult(Book result) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), BookSummaryActivity.class);
                intent.putExtra("BOOK", result);
                Pair<View, String> p1 = Pair.create(sharedImageView, "cover");
                Pair<View, String> p2 = Pair.create(sharedTextView, "title");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), p1, p2);

                startActivity(intent, options.toBundle());
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
