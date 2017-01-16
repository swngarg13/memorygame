package com.colormemory.Adapters;

/**
 * Created by sawangarg on 12/01/17.
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.colormemory.IScoreUpdater;
import com.colormemory.R;
import com.colormemory.models.CardModal;
import com.colormemory.models.ScoreModal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreHolder> {
    private List<ScoreModal> scores;
    public ScoreAdapter(List<ScoreModal> scores) {
        this.scores = scores;
    }

    @Override
    public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_scores, parent, false);

        return new ScoreHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScoreHolder holder, final int position) {
        holder.rank.setText(String.valueOf(position + 1));
        holder.winner.setText(scores.get(position).getName());
        holder.score.setText(String.valueOf(scores.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreHolder extends RecyclerView.ViewHolder{
        private TextView rank, winner, score;
        public ScoreHolder(View view) {
            super(view);
            rank = (TextView) view.findViewById(R.id.tv_rank);
            winner = (TextView) view.findViewById(R.id.tv_winner);
            score = (TextView) view.findViewById(R.id.tv_score);
        }
    }

}
