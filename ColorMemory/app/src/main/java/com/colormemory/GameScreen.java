package com.colormemory;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colormemory.Adapters.GameAdapter;
import com.colormemory.database.DatabaseHandler;
import com.colormemory.models.CardModal;
import com.colormemory.models.ScoreModal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

public class GameScreen extends Activity implements IScoreUpdater{

    private int[] images = {R.mipmap.colour1, R.mipmap.colour2, R.mipmap.colour3, R.mipmap.colour4, R.mipmap.colour5, R.mipmap.colour6, R.mipmap.colour7, R.mipmap.colour8};
    private RecyclerView rView;
    private ArrayList<CardModal> cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        GridLayoutManager lLayout = new GridLayoutManager(this, 4);

        rView = (RecyclerView)findViewById(R.id.list);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        startNewGame();

        findViewById(R.id.iv_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), HighScores.class));
            }
        });
    }

    private void startNewGame(){
        if (cards == null){
            // create 4*4 = 16 position, 2 position for each card
            cards = new ArrayList<>();
            for(int i = 0; i < images.length; i++){
                CardModal card = new CardModal();
                card.setImage(images[i]);
                card.setIsflipped(false);
                card.setRemoveFromBoard(false);

                CardModal card1 = new CardModal();
                card1.setImage(images[i]);
                card1.setIsflipped(false);
                card1.setRemoveFromBoard(false);

                cards.add(card);
                cards.add(card1);
            }
        }else{
            for (CardModal modal :cards){
                modal.setIsflipped(false);
                modal.setRemoveFromBoard(false);
            }
        }
        Collections.shuffle(cards);    // shuffle cards and distribute on the game board.

        // set up game board
        if (rView.getAdapter() == null) {
            GameAdapter rcAdapter = new GameAdapter(this, cards, this);
            rView.setAdapter(rcAdapter);
        }else{
            GameAdapter rcAdapter = (GameAdapter) rView.getAdapter();
            rcAdapter.setNewData(cards);
        }
    }

    @Override
    public void updateScore(final int userp){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Score : " + userp);

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Name of player");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (TextUtils.isEmpty(name.trim()))
                    name = "Anonymous";
                ScoreModal modal = new ScoreModal();
                modal.setName(name);
                modal.setScore(userp);
                modal.setTime(new Date().toString());
                new DatabaseHandler(GameScreen.this).addScore(modal);
                startNewGame();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (TextUtils.isEmpty(name.trim()))
                    name = "Anonymous";
                ScoreModal modal = new ScoreModal();
                modal.setName(name);
                modal.setScore(userp);
                modal.setTime(new Date().toString());
                new DatabaseHandler(GameScreen.this).addScore(modal);
                dialog.cancel();
                startNewGame();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
