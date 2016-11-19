package cl.telematica.android.certamen3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by neo_free on 18/11/2016.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NoticiasDB";
    private String sqlString = "CREATE TABLE noticias (id TEXT, title TEXT, link TEXT, author TEXT, publishedDate TEXT, Content TEXT, image TEXT)";


    public Database(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'noticias'");
        onCreate(db);
    }
}
