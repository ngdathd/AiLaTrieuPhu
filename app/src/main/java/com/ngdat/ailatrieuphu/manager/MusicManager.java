package com.ngdat.ailatrieuphu.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.ngdat.ailatrieuphu.R;

import java.util.ArrayList;
import java.util.Random;

public class MusicManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String STATE_MUSIC = "MUSIC";
    private static final String STATE_SOUND = "SOUND";
    public static final int PLAYER_IDLE = -1;
    public static final int PLAYER_PLAY = 1;
    public static final int PLAYER_PAUSE = 2;
    public static final int PLAYER_STOP = 3;
    private int stateS, stateBg;

    public static final int[] LUAT_CHOI = {R.raw.luatchoi_b, R.raw.luatchoi_c};
    public static final int[] READY = {R.raw.ready, R.raw.ready_b, R.raw.ready_c};
    public static final int[] SOUND_5050 = {R.raw.sound5050, R.raw.sound5050_2};
    public static final int[] ANS_NOW = {R.raw.ans_now1, R.raw.ans_now2, R.raw.ans_now3};
    public static final int[] VUOT_MOC_1 = {R.raw.chuc_mung_vuot_moc_01_0, R.raw.chuc_mung_vuot_moc_01_1};
    public static final int[] ANS_A = {R.raw.ans_a, R.raw.ans_a2};
    public static final int[] ANS_B = {R.raw.ans_b, R.raw.ans_b2};
    public static final int[] ANS_C = {R.raw.ans_c, R.raw.ans_c2};
    public static final int[] ANS_D = {R.raw.ans_d, R.raw.ans_d2};
    public static final int[] TRUE_A = {R.raw.true_a, R.raw.true_a2};
    public static final int[] TRUE_B = {R.raw.true_b, R.raw.true_b2, R.raw.true_b3};
    public static final int[] TRUE_C = {R.raw.true_c, R.raw.true_c2, R.raw.true_c3};
    public static final int[] TRUE_D = {R.raw.true_d2, R.raw.true_d3};
    public static final int[] LOSE = {R.raw.lose, R.raw.lose2};
    public static final int[] LOSE_A = {R.raw.lose_a, R.raw.lose_a2};
    public static final int[] LOSE_B = {R.raw.lose_b, R.raw.lose_b2};
    public static final int[] LOSE_C = {R.raw.lose_c, R.raw.lose_c2};
    public static final int[] LOSE_D = {R.raw.lose_d, R.raw.lose_d2};
    public static final int[] QUEST_1 = {R.raw.ques1, R.raw.ques1_b};
    public static final int[] QUEST_2 = {R.raw.ques2, R.raw.ques2_b};
    public static final int[] QUEST_3 = {R.raw.ques3, R.raw.ques3_b};
    public static final int[] QUEST_4 = {R.raw.ques4, R.raw.ques4_b};
    public static final int[] QUEST_5 = {R.raw.ques5, R.raw.ques5_b};
    public static final int[] QUEST_6 = {R.raw.ques6};
    public static final int[] QUEST_7 = {R.raw.ques7, R.raw.ques7_b};
    public static final int[] QUEST_8 = {R.raw.ques8, R.raw.ques8_b};
    public static final int[] QUEST_9 = {R.raw.ques9, R.raw.ques9_b};
    public static final int[] QUEST_10 = {R.raw.ques10};
    public static final int[] QUEST_11 = {R.raw.ques11};
    public static final int[] QUEST_12 = {R.raw.ques12};
    public static final int[] QUEST_13 = {R.raw.ques13};
    public static final int[] QUEST_14 = {R.raw.ques14};
    public static final int[] QUEST_15 = {R.raw.ques15};

    private MediaPlayer mediaPlayer;
    private MediaPlayer bgMusic;
    private SharedPreferences preferences;
    private Context c;
    private boolean stateMusic;
    private boolean stateSound;
    private ArrayList<int[]> arrIdQues;

    public MusicManager(Context contex) {
        this.c = contex;
        fillArrIdsRawQues();
        getPreferenceSetting();
    }

    public int[] getIdsLoseCase(int lose) {
        if (lose == 1) {
            return LOSE_A;
        } else if (lose == 2) {
            return LOSE_B;
        } else if (lose == 3) {
            return LOSE_C;
        } else {
            return LOSE_D;
        }
    }

    public void setStateMusic(boolean stateMusic) {
        this.stateMusic = stateMusic;
    }

    public void setStateSound(boolean stateSound) {
        this.stateSound = stateSound;
    }

    private void fillArrIdsRawQues() {
        arrIdQues = new ArrayList<>();
        arrIdQues.add(QUEST_1);
        arrIdQues.add(QUEST_2);
        arrIdQues.add(QUEST_3);
        arrIdQues.add(QUEST_4);
        arrIdQues.add(QUEST_5);
        arrIdQues.add(QUEST_6);
        arrIdQues.add(QUEST_7);
        arrIdQues.add(QUEST_8);
        arrIdQues.add(QUEST_9);
        arrIdQues.add(QUEST_10);
        arrIdQues.add(QUEST_11);
        arrIdQues.add(QUEST_12);
        arrIdQues.add(QUEST_13);
        arrIdQues.add(QUEST_14);
        arrIdQues.add(QUEST_15);
    }

    public int[] getIdsRaw(int lv) {
        return arrIdQues.get(lv - 1);
    }

    public boolean getStateMusic() {
        return stateMusic;
    }

    public boolean getStateSound() {
        return stateSound;
    }


    private void getPreferenceSetting() {
        preferences = PreferenceManager.getDefaultSharedPreferences(c);
        boolean stateMusic = preferences.getBoolean(STATE_MUSIC, true);
        boolean stateSound = preferences.getBoolean(STATE_SOUND, true);

        setStateMusic(stateMusic);
        setStateSound(stateSound);
    }


    public void setting(boolean stateMusic, boolean stateSound) {

        setStateMusic(stateMusic);
        setStateSound(stateSound);

        preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(STATE_MUSIC, stateMusic);
        editor.putBoolean(STATE_SOUND, stateSound);
        editor.apply();
    }

    public void play(int idSound, MediaPlayer.OnCompletionListener completionListener) {
        if (!getStateSound()) {
            if (completionListener == null) {
                return;
            }
            completionListener.onCompletion(null);
            return;
        }

        stop();
        stateS = PLAYER_IDLE;
        mediaPlayer = MediaPlayer.create(c, idSound);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(completionListener);
        if (completionListener == null) {
            mediaPlayer.setOnCompletionListener(this);
        }
        mediaPlayer.setOnPreparedListener(this);
    }

    public void play(int[] idSound, MediaPlayer.OnCompletionListener completionListener) {
        if (!getStateSound()) {
            completionListener.onCompletion(null);
            return;
        }
        stop();
        stateS = PLAYER_IDLE;
        mediaPlayer = MediaPlayer.create(c, idSound[new Random().nextInt(idSound.length)]);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(completionListener);
        if (completionListener == null) {
            mediaPlayer.setOnCompletionListener(this);
        }
        mediaPlayer.setOnPreparedListener(this);
    }

    public void playBgMusic(int idSound) {
        if (!getStateMusic()) {
            return;
        }
        stopBgMusic();
        stateBg = PLAYER_IDLE;
        bgMusic = new MediaPlayer();
        bgMusic = MediaPlayer.create(c, idSound);
        bgMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
        bgMusic.setLooping(true);
        bgMusic.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                if (stateBg == PLAYER_IDLE) {
                    mp.start();
                    stateBg = PLAYER_PLAY;
                }
            }
        });

    }

    public void pauseBgMusic() {
        if (stateBg == PLAYER_PLAY) {
            bgMusic.pause();
            stateBg = PLAYER_PAUSE;
        }
    }

    public void pauseSound() {
        if (stateS == PLAYER_PLAY) {
            mediaPlayer.pause();
            stateS = PLAYER_PAUSE;
        }
    }

    public void resumeBgMusic() {
        if (stateBg == PLAYER_PAUSE && stateS != PLAYER_PLAY) {
            bgMusic.start();
            stateBg = PLAYER_PLAY;
        }
    }

    public void resumeSound() {
        if (stateS == PLAYER_PAUSE) {
            mediaPlayer.start();
            stateS = PLAYER_PLAY;
        }
    }

    public void stopBgMusic() {
        if (stateBg == PLAYER_PLAY || stateBg == PLAYER_PAUSE) {
            bgMusic.release();
            bgMusic = null;
            stateBg = PLAYER_IDLE;
        }
    }


    public void stop() {
        if (stateS == PLAYER_PLAY || stateS == PLAYER_PAUSE) {
            mediaPlayer.release();
            mediaPlayer = null;
            stateS = PLAYER_IDLE;
        }
        pauseBgMusic();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (stateS == PLAYER_IDLE) {
            mp.start();
            stateS = PLAYER_PLAY;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.release();
        mediaPlayer = null;
        stateS = PLAYER_IDLE;
    }
}