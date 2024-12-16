package com.ngdat.ailatrieuphu.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ngdat.ailatrieuphu.App;
import com.ngdat.ailatrieuphu.R;
import com.ngdat.ailatrieuphu.activity.HighScoreActivity;
import com.ngdat.ailatrieuphu.activity.PlayerActivity;
import com.ngdat.ailatrieuphu.activity.SettingsActivity;
import com.ngdat.ailatrieuphu.dialogs.AboutDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bg_circle_rotate);
        (view.findViewById(R.id.bg_circle_anim)).setAnimation(animation);
        view.findViewById(R.id.btn_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_play).setOnClickListener(this);
        view.findViewById(R.id.btn_high_score).setOnClickListener(this);
        view.findViewById(R.id.btn_about).setOnClickListener(this);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                App.getMusicPlayer().playBgMusic(R.raw.bgmusic);
            }
        }, 2500);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getMusicPlayer().resumeBgMusic();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getMusicPlayer().pauseBgMusic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_play:
                Intent intent1 = new Intent(getContext(), PlayerActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_high_score:
                Intent intent2 = new Intent(getContext(), HighScoreActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_about:
                AboutDialog aboutDialog = new AboutDialog(getContext());
                aboutDialog.show();
                break;
            default:
                break;
        }
    }
}
