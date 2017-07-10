package ngdat.ailatrieuphu.activity;

import android.app.ListActivity;
import android.os.Bundle;

import ngdat.ailatrieuphu.App;
import ngdat.ailatrieuphu.R;
import ngdat.ailatrieuphu.adapter.HighScoreAdapter;
import ngdat.ailatrieuphu.manager.DatabaseManager;

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
