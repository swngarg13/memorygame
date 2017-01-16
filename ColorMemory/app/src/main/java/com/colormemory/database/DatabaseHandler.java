package com.colormemory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.colormemory.models.ScoreModal;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
   private static final int DATABASE_VERSION = 1;  
   private static final String DATABASE_NAME = "colormemory";
    private static final String TABLE_SCORES = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DATE = "playedat";
   
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }  
   
    // Creating Tables  
    @Override  
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"  
                + KEY_SCORE + " TEXT, " + KEY_DATE + " TEXT)";
        db.execSQL(CREATE_SCORE_TABLE);
    }  
   
    // Upgrading database  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        // Drop older table if existed  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
   
        // Create tables again  
        onCreate(db);  
    }  
   
     // code to add the new score
     public void addScore(ScoreModal modal) {
        SQLiteDatabase db = this.getWritableDatabase();  
   
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, modal.getName()); // Winner Name
        values.put(KEY_SCORE, modal.getScore()); // Winner Score
         values.put(KEY_DATE, modal.getTime()); // Played at
   
        // Inserting Row  
        db.insert(TABLE_SCORES, null, values);
        db.close(); // Closing database connection  
    }  

    // code to get all scores in a list view
    public List<ScoreModal> getAllScores() {
        List<ScoreModal> scorelist = new ArrayList<>();
        // Select All Query  
        String selectQuery = "SELECT * FROM " + TABLE_SCORES + " order by " + KEY_SCORE + " desc";
   
        SQLiteDatabase db = this.getWritableDatabase();  
        Cursor cursor = db.rawQuery(selectQuery, null);
   
        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {  
            do {  
                ScoreModal modal = new ScoreModal();
                modal.setId(Integer.parseInt(cursor.getString(0)));
                modal.setName(cursor.getString(1));
                modal.setScore(Integer.parseInt(cursor.getString(2)));
                modal.setTime(cursor.getString(3));
                // Adding score to list
                scorelist.add(modal);
            } while (cursor.moveToNext());  
        }  
   
        // return scores list
        return scorelist;
    }  

    // Getting scores Count
    public int getScoresCount() {
        String countQuery = "SELECT * FROM " + TABLE_SCORES;
        SQLiteDatabase db = this.getReadableDatabase();  
        Cursor cursor = db.rawQuery(countQuery, null);  
        cursor.close();  
   
        // return count  
        return cursor.getCount();  
    }  
   
}  