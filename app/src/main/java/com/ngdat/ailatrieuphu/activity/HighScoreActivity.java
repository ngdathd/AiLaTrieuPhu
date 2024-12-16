package com.ngdat.ailatrieuphu.activity;

import android.app.ListActivity;
import android.os.Bundle;

import com.ngdat.ailatrieuphu.App;
import com.ngdat.ailatrieuphu.R;
import com.ngdat.ailatrieuphu.adapter.HighScoreAdapter;
import com.ngdat.ailatrieuphu.manager.DatabaseManager;

public class HighScoreActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        DatabaseManager databaseManager = new DatabaseManager(App.getContext());
        getListView().setAdapter(new HighScoreAdapter(databaseManager.getHighScore()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getMusicPlayer().pauseBgMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getMusicPlayer().resumeBgMusic();

    }
}
