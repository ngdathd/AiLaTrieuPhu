package ngdat.ailatrieuphu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ngdat.ailatrieuphu.App;
import ngdat.ailatrieuphu.R;
import ngdat.ailatrieuphu.model.HighScore;

public class HighScoreAdapter extends BaseAdapter {
    private ArrayList<HighScore> highScores;

    public HighScoreAdapter(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    @Override
    public int getCount() {
        return highScores == null ? 0 : highScores.size();
    }

    @Override
    public HighScore getItem(int position) {
        return highScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.high_score_view, parent, false);

        holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tvRank = (TextView) convertView.findViewById(R.id.tv_rank);
        holder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);

        String score = new DecimalFormat("###,###").format(getItem(position).getScore());
        holder.tvScore.setText(score + " VNĐ");
        holder.tvName.setText(getItem(position).getName());

        if (position == 0) {
            holder.tvRank.setBackgroundResource(R.drawable.rank_1);
            convertView.setBackgroundColor(Color.parseColor("#9C27B0"));
            holder.tvName.setTextColor(Color.parseColor("#FF9800"));
        } else if (position == 1) {
            holder.tvRank.setBackgroundResource(R.drawable.rank_2);
            convertView.setBackgroundColor(Color.parseColor("#009688"));
            holder.tvName.setTextColor(Color.parseColor("#00E676"));
        } else if (position == 2) {
            holder.tvRank.setBackgroundResource(R.drawable.rank_3);
            convertView.setBackgroundColor(Color.parseColor("#03A9F4"));
            holder.tvName.setTextColor(Color.parseColor("#9C27B0"));
        } else {
            holder.tvRank.setText(position + 1 + "");
        }

        return convertView;
    }

    private class Holder {
        TextView tvName, tvRank, tvScore;
    }
}
