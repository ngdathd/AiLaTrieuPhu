package com.ngdat.ailatrieuphu.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.ngdat.ailatrieuphu.R;

public class MoneyLayout extends RelativeLayout {
    private TextView tvLevel[];
    private int width;

    public MoneyLayout(Context context) {
        super(context);
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void findViewByIds() {
        tvLevel = new TextView[15];

        tvLevel[0] = (TextView) findViewById(R.id.tv_level_1);
        tvLevel[1] = (TextView) findViewById(R.id.tv_level_2);
        tvLevel[2] = (TextView) findViewById(R.id.tv_level_3);
        tvLevel[3] = (TextView) findViewById(R.id.tv_level_4);
        tvLevel[4] = (TextView) findViewById(R.id.tv_level_5);
        tvLevel[5] = (TextView) findViewById(R.id.tv_level_6);
        tvLevel[6] = (TextView) findViewById(R.id.tv_level_7);
        tvLevel[7] = (TextView) findViewById(R.id.tv_level_8);
        tvLevel[8] = (TextView) findViewById(R.id.tv_level_9);
        tvLevel[9] = (TextView) findViewById(R.id.tv_level_10);
        tvLevel[10] = (TextView) findViewById(R.id.tv_level_11);
        tvLevel[11] = (TextView) findViewById(R.id.tv_level_12);
        tvLevel[12] = (TextView) findViewById(R.id.tv_level_13);
        tvLevel[13] = (TextView) findViewById(R.id.tv_level_14);
        tvLevel[14] = (TextView) findViewById(R.id.tv_level_15);

        setClickable(false);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) getLayoutParams();
        params.width = width;
        setLayoutParams(params);

    }

    public MoneyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = context.getResources().getDisplayMetrics().widthPixels;

    }

    public void setBackGroundLevel(int level) {
        tvLevel[level - 1].setBackgroundResource(R.drawable.player_image_money_curent);
        if (level - 2 >= 0) {
            if (level - 1 == 5 || level - 1 == 10 || level - 1 == 15) {
                tvLevel[level - 2].setBackgroundResource(R.drawable.player_image_money_milestone);
                return;
            }
            tvLevel[level - 2].setBackground(null);
        }
    }

    public String getMoney(int lv) {

        return tvLevel[lv - 1].getText().toString();
    }
}
