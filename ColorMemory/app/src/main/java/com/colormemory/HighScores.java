package com.colormemory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.colormemory.Adapters.ScoreAdapter;
import com.colormemory.database.DatabaseHandler;
import com.colormemory.models.ScoreModal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(lLayout);
        List<ScoreModal> scores = new DatabaseHandler(this).getAllScores();

        Collections.sort(scores, comparator);
        if (scores != null && scores.size() > 0){
            list.setAdapter(new ScoreAdapter(scores));
        }
    }

    Comparator<ScoreModal> comparator = new Comparator<ScoreModal>() {
        @Override
        public int compare(ScoreModal o1, ScoreModal o2) {
            return o1.getScore() > o2.getScore() ? -1 : 1;
        }
    };
}
