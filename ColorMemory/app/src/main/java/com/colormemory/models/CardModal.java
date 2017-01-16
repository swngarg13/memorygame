package com.colormemory.models;

import java.io.Serializable;

/**
 * Created by sawangarg on 12/01/17.
 */

public class CardModal implements Serializable {
    private int image;
    private boolean isflipped;
    private boolean removeFromBoard;

    public boolean isRemoveFromBoard() {
        return removeFromBoard;
    }

    public void setRemoveFromBoard(boolean removeFromBoard) {
        this.removeFromBoard = removeFromBoard;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isflipped() {
        return isflipped;
    }

    public void setIsflipped(boolean isflipped) {
        this.isflipped = isflipped;
    }
}
