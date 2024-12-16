package com.ngdat.ailatrieuphu.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.ngdat.ailatrieuphu.R;

public class AudienceDialog extends Dialog {
    private TextView present[];
    private TextView barVote[];
    private int indexTrue, index1, index2, index3;
    private ArrayList<Integer> values;
    private Random random;
    private Handler handler;
    private Runnable runnable;
    private int i = 0, max;
    private String pst;

    public AudienceDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.audience_layout);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        max = findViewById(R.id.rl_1).getLayoutParams().height * 4 / 7;

        random = new Random();
        values = new ArrayList<>();

        present = new TextView[4];
        present[0] = (TextView) findViewById(R.id.tv_pesent_1);
        present[1] = (TextView) findViewById(R.id.tv_pesent_2);
        present[2] = (TextView) findViewById(R.id.tv_pesent_3);
        present[3] = (TextView) findViewById(R.id.tv_pesent_4);

        barVote = new TextView[4];
        barVote[0] = (TextView) findViewById(R.id.bar_1);
        barVote[1] = (TextView) findViewById(R.id.bar_2);
        barVote[2] = (TextView) findViewById(R.id.bar_3);
        barVote[3] = (TextView) findViewById(R.id.bar_4);

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btn_close).setVisibility(View.GONE);

    }

    public void voteAnswer() {

        final ViewGroup.LayoutParams layoutParams = barVote[indexTrue].getLayoutParams();
        final ViewGroup.LayoutParams layoutParams1 = barVote[index1].getLayoutParams();
        final ViewGroup.LayoutParams layoutParams2 = barVote[index2].getLayoutParams();
        final ViewGroup.LayoutParams layoutParams3 = barVote[index3].getLayoutParams();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                i += 2;
                layoutParams.height = i;
                pst = (int)(((float)i / max) * 100) + "%";

                barVote[indexTrue].setLayoutParams(layoutParams);
                present[indexTrue].setText(pst);

                if (i <= values.get(1)) {
                    layoutParams1.height = i;
                    barVote[index1].setLayoutParams(layoutParams1);
                    present[index1].setText(pst);
                }
                if (i <= values.get(2)) {
                    layoutParams2.height = i;
                    barVote[index2].setLayoutParams(layoutParams2);
                    present[index2].setText(pst);
                }
                if (i <= values.get(3)) {
                    layoutParams3.height = i;
                    barVote[index3].setLayoutParams(layoutParams3);
                    present[index3].setText(pst);
                }
                if (i < values.get(0))
                    handler.postDelayed(runnable, 1);
                else {
                    int pst1, pst2, pst3, pst4;

                    pst1 = (int) (((float)values.get(0) / max) * 100);
                    pst2 = (int) (((float)values.get(1) / max) * 100);
                    pst3 = (int) (((float)values.get(2) / max) * 100);
                    pst4 = 100 - (pst3 + pst1 + pst2);

                    present[indexTrue].setText(pst1 + "%");
                    present[index1].setText(pst2 + "%");
                    present[index2].setText(pst3 + "%");
                    present[index3].setText(pst4 + "%");
                    findViewById(R.id.btn_close).setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(runnable);
    }

    public void prepareVote(int trueCase, String cs) {
        indexTrue = trueCase - 1;
        if (cs != "") {
            values.add(Math.round((float) random.nextInt(max / 2) + (max / 2) + 1));
            values.add(0);
            values.add(0);
            values.add(max - values.get(0));

            index1 = Integer.parseInt(String.valueOf(cs.charAt(0)));
            index2 = Integer.parseInt(String.valueOf(cs.charAt(1)));

            for (int i = 0; i < 4; i++) {
                if (i != index1 && i != index2 && i != indexTrue) {
                    index3 = i;
                    return;
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (i != indexTrue) {
                    index1 = i;
                    for (int j = i + 1; j < 4; j++) {
                        if (j != indexTrue) {
                            index2 = j;
                            for (int k = j + 1; k < 4; k++) {
                                if (k != indexTrue) {
                                    index3 = k;
                                    values.add(Math.round((float) random.nextInt(max * 3 / 4)));
                                    values.add(random.nextInt(max - values.get(0)));
                                    values.add(random.nextInt(max - values.get(0) - values.get(1)));
                                    values.add(max - ((values.get(0) + values.get(1)) + values.get(2)));
                                    Collections.sort(values);
                                    Collections.reverse(values);

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
