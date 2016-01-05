package ncrc.nise.ajou.ac.kr.opa.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * Create DB and Table
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String TAG = "DBHelper";

    static final String DB_PATH = "/sdcard/";
    static final String DB_NAME = "opa.db";
    static final int DB_VERSION = 1;

    static final String TABLE_USER = "user";
    static final String TABLE_DAILY_MANBO = "daily_manbo";
    static final String TABLE_DAILY_FOOD = "daily_food";

    Context context;
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // called when database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create TABLE_USER
            db.execSQL("create table user (" +
                    "_id integer primary key autoincrement, " +
                    "height real, weight real, age integer, sex integer);");

            // Create TABLE_DAILY_MANBO
            db.execSQL("create table daily_manbo (" +
                    "_id integer primary key autoincrement, " +
                    "date TEXT, manbo integer, run integer);");

            // Create TABLE_DAILY_FOOD
            db.execSQL("create table daily_food (" +
                    "_id integer primary key autoincrement, " +
                    "date TEXT, type TEXT, foodname TEXT, kcal real);");

            Log.i("DB", "onCreate!");
            this.db = db;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* When DB Version changes, it is called. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Typically do ALTER TABLE statements, but...we're just in development,
        // so:
        db.execSQL("drop table if exists " + TABLE_USER); // drops the old database
        db.execSQL("drop table if exists " + TABLE_DAILY_MANBO); // drops the old database
        db.execSQL("drop table if exists " + TABLE_DAILY_FOOD); // drops the old database
        Log.d(TAG, "onUpdated");
        onCreate(db); // run onCreate to get new database
    }

    // insert data into table TABLE_USER
    public void insert(double height, double weight, int age, int sex) {
        ContentValues values = new ContentValues();
        values.put("height", height);
        values.put("weight", weight);
        values.put("age", age);
        values.put("sex", sex);

        db = getWritableDatabase();
        db.insert(TABLE_USER, null, values);
    }

    // insert data into table TABLE_DAILY_MANBO
    public void insert(String date, int manbo, int run) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("manbo", manbo);
        values.put("run", run);
        db = getWritableDatabase();
        db.insert(TABLE_DAILY_MANBO, null, values);
    }
    // insert data into table TABLE_DAILY_FOOD
    public void insert(String date, String type, String foodname, double kcal) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("type", type);
        values.put("foodname", foodname);
        values.put("kcal", kcal);

        db = getWritableDatabase();
        db.insert(TABLE_DAILY_FOOD, null, values);
    }

    public void deleteDailyFood(int id) {
        db.delete(TABLE_DAILY_FOOD, "_id=?", new String[]{Integer.toString(id)});
        Log.i("db", id + " 정상적으로 삭제되었습니다.");
    }
}
