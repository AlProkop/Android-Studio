package fi.lamk.gmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "markers.db";
    private static String TABLE_NAME = "markers_table";
    private static String COLUMN_TITLE = "title";
    private static String COLUMN_LATITUID = "lat";
    private static String COLUMN_LONGITUDE = "lon";
    //SQLiteDatabase db = this.getWritableDatabase();


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME + " (title text primary key, " +
                "lat real, lon real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+ TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String title, Double lat, Double lng){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_LATITUID, lat);
        contentValues.put(COLUMN_LONGITUDE, lng);

        long res = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return res;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public int deleteMarker(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        int delRow = db.delete(TABLE_NAME, COLUMN_TITLE + "=?", new String[]{title} );
        db.close();
        return delRow;
    }


}
