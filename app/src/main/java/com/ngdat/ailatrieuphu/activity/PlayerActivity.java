package com.ngdat.ailatrieuphu.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.Random;

import com.ngdat.ailatrieuphu.App;
import com.ngdat.ailatrieuphu.R;
import com.ngdat.ailatrieuphu.dialogs.AudienceDialog;
import com.ngdat.ailatrieuphu.dialogs.CallDialog;
import com.ngdat.ailatrieuphu.dialogs.NoticeDialog;
import com.ngdat.ailatrieuphu.dialogs.ScoreDialog;
import com.ngdat.ailatrieuphu.layout.MoneyLayout;
import com.ngdat.ailatrieuphu.manager.DatabaseManager;
import com.ngdat.ailatrieuphu.manager.MusicManager;
import com.ngdat.ailatrieuphu.model.Question;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseManager databaseManager;
    private DrawerLayout drawerLayout;
    private MoneyLayout layoutMoney;
    private LinearLayout layoutPlay;
    private NoticeDialog noticeDialog;
    private ScoreDialog scoreDialog;
    private AudienceDialog audienceDialog;
    private CallDialog callDialog;
    private Runnable runnable;
    private Runnable runnableTimer;
    private Handler handler;
    private Random random;
    private DrawerLayout.DrawerListener drawerListener;
    private ArrayList<Question> questions;
    private TextView tvTimer, tvMoney, tvCase[], tvQuestion, tvLevel;
    private ImageButton btnCall, btnAudience, btnStop, btn5050, btnChange;
    private ProgressBar pgTimer;
    private ImageView ivPlayer;
    private Button btnHide;
    private boolean isPlaying;
    private boolean isReady;
    private int timer;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        noticeDialog = new NoticeDialog(this);
        scoreDialog = new ScoreDialog(this);
        audienceDialog = new AudienceDialog(this);
        callDialog = new CallDialog(this);

        findViewByIds();
        setEvents();
        loadRules();
    }

    public PlayerActivity() {
        handler = new Handler();
        random = new Random();
        isPlaying = false;
        isReady = false;
        databaseManager = new DatabaseManager(App.getContext());
        questions = new ArrayList<>();
        questions.addAll(databaseManager.get15Questions());
        tvCase = new TextView[4];
        level = 1;
        runnableTimer = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) timer--;
                tvTimer.setText(timer + "");
                if (timer == 0) {
                    isPlaying = false;
                    App.getMusicPlayer().play(R.raw.out_of_time, null);
                    noticeDialog.setNotification("Hết giờ !", "Đóng", null, null);
                    noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            saveScore(false);
                        }
                    });
                    noticeDialog.show();
                    return;
                }
                handler.postDelayed(runnableTimer, 1000);
            }
        };
    }

    private void findViewByIds() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_player);
        ivPlayer = (ImageView) findViewById(R.id.iv_player);
        btnHide = (Button) findViewById(R.id.btn_hide);
        pgTimer = (ProgressBar) findViewById(R.id.pg_timer);
        btn5050 = (ImageButton) findViewById(R.id.btn_5050);
        btnAudience = (ImageButton) findViewById(R.id.btn_audience);
        btnChange = (ImageButton) findViewById(R.id.btn_change);
        btnCall = (ImageButton) findViewById(R.id.btn_call);
        btnStop = (ImageButton) findViewById(R.id.btn_stop);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvCase[0] = (TextView) findViewById(R.id.tv_case_a);
        tvCase[1] = (TextView) findViewById(R.id.tv_case_b);
        tvCase[2] = (TextView) findViewById(R.id.tv_case_c);
        tvCase[3] = (TextView) findViewById(R.id.tv_case_d);
        layoutMoney = (MoneyLayout) findViewById(R.id.layout_money);
        layoutMoney.findViewByIds();
        layoutPlay = (LinearLayout) findViewById(R.id.ln_play);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.removeDrawerListener(drawerListener);
                App.getMusicPlayer().stop();
                getNewQuestion();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };

    }

    private void setEvents() {
        tvCase[0].setOnClickListener(this);
        tvCase[1].setOnClickListener(this);
        tvCase[2].setOnClickListener(this);
        tvCase[3].setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnAudience.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btn5050.setOnClickListener(this);
        ivPlayer.setOnClickListener(this);
        btnHide.setOnClickListener(this);
        layoutPlay.setVisibility(View.GONE);
        tvMoney.setText("0");
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setClickAble(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (isPlaying) setClickAble(true);
                if (!isReady) {
                    handler.removeCallbacks(runnable);
                    startGame();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void loadRules() {
        drawerLayout.openDrawer(GravityCompat.START);
        App.getMusicPlayer().play(MusicManager.LUAT_CHOI, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                noticeDialog.setCancelable(false);
                noticeDialog.setNotification("Bạn đã sẵn sàng chơi với chúng tôi ?", "Sẵn sàng", "Bỏ qua", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_cancle:
                                noticeDialog.dismiss();
                                stopThread();
                                finish();
                                break;
                            case R.id.btn_ok:
                                noticeDialog.dismiss();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                App.getMusicPlayer().stop();
                                startGame();
                                break;
                            default:
                                break;
                        }
                    }
                });
                if (mp == null) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            noticeDialog.show();
                        }
                    };
                    handler.postDelayed(runnable, 3000);
                } else {
                    App.getMusicPlayer().play(MusicManager.READY, null);
                    noticeDialog.show();
                }
            }

        });
    }

    private void startGame() {
        isReady = true;
        App.getMusicPlayer().play(R.raw.gofind, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                layoutMoney.setBackGroundLevel(level);
                drawerLayout.openDrawer(GravityCompat.START);
                App.getMusicPlayer().play(MusicManager.QUEST_1, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        App.getMusicPlayer().stop();
                        if (mp == null) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    playGame();
                                }
                            }, 3000);
                        } else {
                            playGame();
                        }
                    }
                });
            }
        });
    }

    public void playGame() {
        drawerLayout.closeDrawer(GravityCompat.START);
        layoutPlay.setVisibility(View.VISIBLE);
        App.getMusicPlayer().playBgMusic(R.raw.background_music);
        setQuestion();
        handler.post(runnableTimer);
    }

    private void setQuestion() {
        Question ques = questions.get(level - 1);
        tvCase[0].setEnabled(true);
        tvCase[1].setEnabled(true);
        tvCase[2].setEnabled(true);
        tvCase[3].setEnabled(true);
        tvCase[0].setBackgroundResource(R.drawable.player_answer_background_normal);
        tvCase[1].setBackgroundResource(R.drawable.player_answer_background_normal);
        tvCase[2].setBackgroundResource(R.drawable.player_answer_background_normal);
        tvCase[3].setBackgroundResource(R.drawable.player_answer_background_normal);
        tvLevel.setText("Câu: " + ques.getLevel());
        tvQuestion.setText(ques.getQuestion());
        tvCase[0].setText("A: " + ques.getCaseA());
        tvCase[1].setText("B: " + ques.getCaseB());
        tvCase[2].setText("C: " + ques.getCaseC());
        tvCase[3].setText("D: " + ques.getCaseD());
        App.getMusicPlayer().resumeBgMusic();
        timer = 30;
        pgTimer.setVisibility(View.VISIBLE);

        runnable = new Runnable() {
            @Override
            public void run() {
                isPlaying = true;
                setClickAble(true);
            }
        };
        handler.postDelayed(runnable, 1200);
    }

    private void getNewQuestion() {
        if (level == 15) {
            noticeDialog.setNotification("Chúc mừng bạn đã vượt qua 15 câu hỏi", "Đóng", null, null);
            noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    saveScore(false);
                }
            });
            noticeDialog.show();
            App.getMusicPlayer().play(R.raw.best_player, null);
            return;
        }
        level++;
        layoutMoney.setBackGroundLevel(level);
        drawerLayout.openDrawer(GravityCompat.START);
        App.getMusicPlayer().play(App.getMusicPlayer().getIdsRaw(level), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                if (mp == null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.closeDrawer(GravityCompat.START);
                            setQuestion();
                        }
                    }, 2000);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    setQuestion();
                }
                if (level == 5) {
                    App.getMusicPlayer().play(R.raw.important, null);
                    App.getMusicPlayer().playBgMusic(R.raw.background_music_b);
                } else if (level == 10) {
                    App.getMusicPlayer().play(R.raw.important, null);
                    App.getMusicPlayer().playBgMusic(R.raw.background_music_c);
                }
            }
        });
    }

    private void checkAnswer(final View v, int[] ans, final int[] trueAns, final int id) {
        setClickAble(false);
        isPlaying = false;
        pgTimer.setVisibility(View.GONE);
        v.setBackgroundResource(R.drawable.player_answer_background_selected);
        App.getMusicPlayer().play(ans, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                if (mp == null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getTrueAnswer() == id) {
                                answerTrue(v, trueAns);
                            } else {
                                answerFalse(v, App.getMusicPlayer().getIdsLoseCase(getTrueAnswer()));
                            }
                        }
                    }, 2000);
                } else {
                    if (getTrueAnswer() == id) {
                        if (level == 5 || level == 10 || level == 15) {
                            App.getMusicPlayer().play(MusicManager.ANS_NOW, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    App.getMusicPlayer().stop();
                                    answerTrue(v, trueAns);
                                }
                            });
                        } else {
                            answerTrue(v, trueAns);
                        }
                    } else {
                        answerFalse(v, App.getMusicPlayer().getIdsLoseCase(getTrueAnswer()));
                    }
                }
            }
        });
    }

    private void answerFalse(final View v, int[] loseAnswer) {
        v.setBackgroundResource(R.drawable.player_answer_background_wrong);
        tvCase[getTrueAnswer() - 1].setBackgroundResource(R.drawable.player_answer_background_true);
        tvCase[getTrueAnswer() - 1].startAnimation(AnimationUtils.loadAnimation(App.getContext(), R.anim.fade_loop));
        App.getMusicPlayer().play(loseAnswer, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                if (mp == null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveScore(false);
                        }
                    }, 3000);
                } else {
                    saveScore(false);
                }
            }
        });
    }

    private void answerTrue(final View v, int[] trueAnswer) {
        tvMoney.setText(layoutMoney.getMoney(level));
        v.setBackgroundResource(R.drawable.player_answer_background_true);
        v.startAnimation(AnimationUtils.loadAnimation(App.getContext(), R.anim.fade_loop));
        App.getMusicPlayer().play(trueAnswer, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getMusicPlayer().stop();
                if (mp == null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getNewQuestion();
                        }
                    }, 2000);
                } else {
                    if (level == 5) {
                        drawerLayout.openDrawer(GravityCompat.START);
                        drawerLayout.addDrawerListener(drawerListener);
                        App.getMusicPlayer().play(MusicManager.VUOT_MOC_1,null);
                    } else if (level == 10) {
                        drawerLayout.openDrawer(GravityCompat.START);
                        drawerLayout.addDrawerListener(drawerListener);
                        App.getMusicPlayer().play(R.raw.chuc_mung_vuot_moc_02_0, null);
                    } else {
                        getNewQuestion();
                    }
                }
            }
        });
    }

    private void setClickAble(boolean b) {
        tvCase[0].setClickable(b);
        tvCase[1].setClickable(b);
        tvCase[2].setClickable(b);
        tvCase[3].setClickable(b);
        btnStop.setClickable(b);
        btnAudience.setClickable(b);
        btn5050.setClickable(b);
        btnCall.setClickable(b);
        btnChange.setClickable(b);
    }

    public int getTrueAnswer() {

        return questions.get(level - 1).getTrueCase();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_case_a:
                checkAnswer(v, MusicManager.ANS_A, MusicManager.TRUE_A, 1);
                break;
            case R.id.tv_case_b:
                checkAnswer(v, MusicManager.ANS_B, MusicManager.TRUE_B, 2);
                break;
            case R.id.tv_case_c:
                checkAnswer(v, MusicManager.ANS_C, MusicManager.TRUE_C, 3);
                break;
            case R.id.tv_case_d:
                checkAnswer(v, MusicManager.ANS_D, MusicManager.TRUE_D, 4);
                break;
            case R.id.btn_change:
                noticeDialog.setNotification("Bạn thực sự muốn đổi câu hỏi ?", "Đồng ý", "Hủy bỏ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.btn_ok) {
                            questions.set(level - 1, databaseManager.getQuestionByLevel(level));
                            tvTimer.setText(30 + "");
                            setQuestion();
                            btnChange.setEnabled(false);
                        }
                        noticeDialog.dismiss();
                    }
                });
                noticeDialog.show();
                break;
            case R.id.btn_5050:
                isPlaying = false;
                setClickAble(false);
                btn5050.setEnabled(false);

                App.getMusicPlayer().play(MusicManager.SOUND_5050, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        App.getMusicPlayer().stop();
                        App.getMusicPlayer().resumeBgMusic();
                        int count = 0;
                        int b = 0;
                        setClickAble(true);
                        while (count < 2) {
                            int temp = random.nextInt(4) + 1;
                            if (temp != getTrueAnswer() && temp != b) {
                                b = temp;
                                tvCase[b - 1].setEnabled(false);
                                tvCase[b - 1].setBackgroundResource(R.drawable.player_answer_background_hide);
                                tvCase[b - 1].setText("");
                                count++;
                                if (count == 2) isPlaying = true;
                            }
                        }
                    }
                });
                break;
            case R.id.btn_stop:
                stopGame();
                break;
            case R.id.iv_player:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_hide:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.btn_call:
                btnCall.setEnabled(false);
                isPlaying = false;
                setClickAble(false);
                callDialog.setTrueAnswer(questions.get(level - 1).getTrueCase());
                App.getMusicPlayer().play(R.raw.help_call, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        App.getMusicPlayer().stop();
                        callDialog.show();
                        App.getMusicPlayer().play(R.raw.help_callb, null);
                        callDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                App.getMusicPlayer().stop();
                                isPlaying = true;
                                setClickAble(true);
                                App.getMusicPlayer().resumeBgMusic();
                            }
                        });
                    }
                });
                break;
            case R.id.btn_audience:
                btnAudience.setEnabled(false);
                isPlaying = false;
                setClickAble(false);
                String cs = "";
                for (int i = 0; i < tvCase.length; i++) {
                    if (!tvCase[i].isEnabled()) {
                        cs += i;
                    }
                }
                audienceDialog.prepareVote(questions.get(level - 1).getTrueCase(), cs);
                audienceDialog.show();
                App.getMusicPlayer().play(R.raw.khan_gia, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        App.getMusicPlayer().stop();
                        audienceDialog.voteAnswer();
                        App.getMusicPlayer().play(R.raw.hoi_y_kien_chuyen_gia_01b, null);
                        audienceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                App.getMusicPlayer().stop();
                                isPlaying = true;
                                setClickAble(true);
                                App.getMusicPlayer().resumeBgMusic();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    public void saveScore(boolean stopGame) {
        App.getMusicPlayer().stopBgMusic();
        stopThread();
        setClickAble(false);
        if (level > 1) {
            if (stopGame) {
                scoreDialog.setScore(tvMoney.getText().toString());
            } else {
                if (level < 5) {
                    scoreDialog.setScore("200,000");
                } else if (level < 10) {
                    scoreDialog.setScore("2,000,000");
                } else if (level < 15) {
                    scoreDialog.setScore("22,000,000");
                } else {
                    scoreDialog.setScore("150,000,000");
                }
            }
            scoreDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    App.getMusicPlayer().play(MusicManager.LOSE, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            App.getMusicPlayer().stop();
                            finish();
                            App.getMusicPlayer().playBgMusic(R.raw.bgmusic);
                        }
                    });
                }
            });
            scoreDialog.show();
        } else {
            App.getMusicPlayer().play(MusicManager.LOSE, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    App.getMusicPlayer().stop();
                    finish();
                    App.getMusicPlayer().playBgMusic(R.raw.bgmusic);
                }
            });
        }
    }

    public void stopGame() {
        noticeDialog.setCancelable(true);
        noticeDialog.setNotification("Bạn thực sự muốn dừng cuộc chơi ?", "Đồng ý", "Hủy bỏ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_ok) {
                    handler.removeCallbacks(runnable);
                    saveScore(true);
                }
                noticeDialog.dismiss();
            }
        });
        noticeDialog.show();
    }

    public void stopThread() {
        isPlaying = false;
        Thread.currentThread().interrupt();
    }

    @Override
    protected void onPause() {
        App.getMusicPlayer().pauseBgMusic();
        App.getMusicPlayer().pauseSound();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlaying) App.getMusicPlayer().resumeBgMusic();
        App.getMusicPlayer().resumeSound();
    }

    @Override
    public void onBackPressed() {
        if (Thread.currentThread().isAlive()) {
            if (!isPlaying) return;
            stopGame();
        } else {
            super.onBackPressed();
        }
    }
}
