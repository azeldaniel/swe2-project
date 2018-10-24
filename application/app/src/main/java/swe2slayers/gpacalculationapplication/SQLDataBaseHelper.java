package swe2slayers.gpacalculationapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Handles the db creation
public class SQLDataBaseHelper extends SQLiteOpenHelper {
    static final String Drop="DROP TABLE IF EXISTS";
    public SQLDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int ver) {
        super(context, name, factory, ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //error catching to minimize risk of crashing
        try{
            //CreateDB is in GpaAdapter its a string holding the schemas
            db.execSQL(GpaAdapter.createGPA);
            db.execSQL(GpaAdapter.CreateYear);
            db.execSQL(GpaAdapter.CreateCourse);
            db.execSQL(GpaAdapter.CreateSemester);
            db.execSQL(GpaAdapter.CreateAssess);
        }catch (Exception failToCreate){
            Log.e("Database Creation:", "Failed to create db");
        }
    }

    @Override
    //if current db is outdated this gets the bew one after deleting old
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Version old", "onUpgrade: getting new Version ");
        //drops db
        db.execSQL(Drop+" gpa");
        db.execSQL(Drop+" year");
        db.execSQL(Drop+" course");
        db.execSQL(Drop+" semester");
        db.execSQL(Drop+" assessment");
        //calls function above to create db
        onCreate(db);
    }
}
