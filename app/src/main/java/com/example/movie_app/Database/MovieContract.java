package com.example.movie_app.Database;

import android.provider.BaseColumns;

public class MovieContract {
    private MovieContract() {}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "privateerMovie";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSTER_PATH = "";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
}
