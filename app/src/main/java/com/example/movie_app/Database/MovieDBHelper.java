package com.example.movie_app.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.movie_app.Model.MoviesModel;

import java.util.ArrayList;
import java.util.List;

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "privateer_movie.db";
    private SQLiteDatabase db;

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(MovieContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop the older tables
        db.execSQL(MovieContract.SQL_DELETE_ENTRIES);

        // Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    // CRUD

    // Create
    public void createMovie(MoviesModel.Result movie){
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.title);
        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
    }

    // Read
    @SuppressLint("Range")
    public List<MoviesModel.Result> getAllMovies(){
        List<MoviesModel.Result> movieList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null,  null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        MoviesModel.Result movie = new MoviesModel.Result();
                        movie.setId(cur.getInt(cur.getColumnIndex(MovieContract.MovieEntry._ID)));
                        movie.setTitle(cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE)));
                        movie.setPoster_path(cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH)));
                        movieList.add(movie);
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return movieList;
    }

    // Update
    public long editMovie(int movieId, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, title);

        long rowsAffected = db.update(
                MovieContract.MovieEntry.TABLE_NAME,
                values,
                MovieContract.MovieEntry._ID + " = ?",
                new String[]{String.valueOf(movieId)}
        );

        db.close();
        return rowsAffected;
    }

    // Delete
    public long deleteMovie(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(MovieContract.MovieEntry.TABLE_NAME,
                    MovieContract.MovieEntry._ID + " = ?",
                    new String[]{String.valueOf(movieId)});
        } finally {
            db.close();
        }
        return 0;
    }
}
