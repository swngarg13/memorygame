package com.colormemory.Adapters;

/**
 * Created by sawangarg on 12/01/17.
 */

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.colormemory.IScoreUpdater;
import com.colormemory.R;
import com.colormemory.models.CardModal;
import android.os.Handler;
import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.CardHolder> {
    private ArrayList<CardModal> cards;
    private int score = 0, flipped_id = -1, flipped_id2 = -1;
    private IScoreUpdater updater;
    private Context context;
    private boolean flip = true;
    public GameAdapter(Context context, ArrayList<CardModal> cards, IScoreUpdater updater) {
        this.context = context;
        this.cards = cards;
        this.updater = updater;
    }

    public void setNewData(ArrayList<CardModal> cards){
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_game, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        // card is already flipped
       if (cards.get(position).isflipped()){
           holder.imageView.setBackgroundResource(cards.get(position).getImage()); //card chosen already
       }else{
           holder.imageView.setBackgroundResource(R.mipmap.card_bg); // card still to be chosen
       }

        // card removed from board or card already matched
       if (cards.get(position).isRemoveFromBoard()){
           holder.imageView.setImageResource(R.mipmap.tick);
       }else{
           holder.imageView.setImageResource(android.R.color.transparent);
       }

        holder.imageView.setRotationY(0);
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                if (flip && !cards.get(position).isflipped() && !cards.get(position).isRemoveFromBoard() && position != flipped_id){
                    flip = false;
                    ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.flip);
                    anim.setTarget(holder.imageView);
                    anim.setDuration(1500);
                    anim.setRepeatCount(0);
                    anim.start();
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (flipped_id == -1) {     // first card is flipped in any round
                                flipped_id = position;
                                cards.get(position).setIsflipped(true);
                                notifyDataSetChanged();
                                flip = true;
                            }else{      // 2nd card is flipper in any round
                                if (cards.get(flipped_id).getImage() == cards.get(position).getImage()){    // if a match is found
                                    score += 2;
                                    cards.get(flipped_id).setRemoveFromBoard(true);
                                    cards.get(flipped_id).setIsflipped(true);
                                    cards.get(position).setRemoveFromBoard(true);
                                    cards.get(position).setIsflipped(true);
                                    Toast.makeText(context, "Match found", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();

                                    flipped_id = -1;
                                    flipped_id2 = -1;
                                    flip = true;

                                    if (isGameEnded())   // all match are found, add score to database
                                        updater.updateScore(score);
                                }else{      // if a match not found
                                    score -= 1;
                                    flipped_id2 = position;
                                    cards.get(flipped_id).setIsflipped(true);
                                    cards.get(position).setIsflipped(true);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Match not found", Toast.LENGTH_SHORT).show();
                                    // take pause of one second when match is not found
                                    takePause();
                                }
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                }
           }
       });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public CardHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_card);
        }
    }

    private void takePause(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cards.get(flipped_id).setIsflipped(false);
                cards.get(flipped_id2).setIsflipped(false);
                flipped_id = -1;
                flipped_id2 = -1;
                notifyDataSetChanged();
                flip = true;
            }
        }, 1000);
    }

    private boolean isGameEnded(){
        boolean ended = true;
        for (CardModal modal : cards){
            if (!modal.isflipped()){
                return false;
            }
        }
        return ended;
    }
}
