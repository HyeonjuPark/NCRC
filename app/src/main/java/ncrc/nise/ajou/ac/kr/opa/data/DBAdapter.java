package ncrc.nise.ajou.ac.kr.opa.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBAdapter {
    static final String TABLE_USER = "user";
    static final String TABLE_DAILY_MANBO = "daily_manbo";
    static final String TABLE_DAILY_FOOD = "daily_food";

    private Context context;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        this.open();
    }

    /* Open DB */
    private void open() {
        try {
            db = (new DBHelper(context).getWritableDatabase());
        } catch(SQLiteException e) {
            db = (new DBHelper(context).getReadableDatabase());
        }
    }

    /* Close DB */
    public void Close() {
        db.close();
    }

    /* Insert User Data */
    public void insertUser(double height, double weight, int age, int sex) {
        try {
            ContentValues values = new ContentValues();
            values.put("height", height);
            values.put("weight",weight);
            values.put("age", age);
            values.put("sex", sex);
            db.insert(TABLE_USER, null, values);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /* Insert Scheduler Data */
    public void insertScheduler(String date, int manbo) {
        try {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("manbo", manbo);
            db.insert(TABLE_DAILY_MANBO, null, values);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /* Read User Info */
    public Cursor read_user_info() {
        Cursor c = db.query(TABLE_USER, //table name
                new String[] {"_id","height","weight", "age", "sex"}, //colum 명세
                null, //where
                null, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

        return c;
    }


    public Cursor readManbo(String date) {
        Cursor c = db.query(TABLE_DAILY_MANBO, //table name
                new String[] {"_id","date","manbo"}, //colum 명세
                "date = ?", //where
                new String[] {date}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

        return c;
      //return db.rawQuery("SELECT * FROM " + tableNameScheduler + " WHERE date = ?",new String[]{"data"});
    }

    public void updateManbo(String date, int manbo, int run) {
        try {
            db.execSQL("UPDATE " + TABLE_DAILY_MANBO + " SET manbo = '" + manbo + "', run = '" + run + "' WHERE date = '" + date + "';");
            Log.i("DBAdapter", "manbo and run is inserted to DB");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /* Read User Info */
    public Cursor readDailyFood(String date, String type) {
        Cursor c = db.query(TABLE_DAILY_FOOD, //table name
                new String[] {"_id","foodname", "kcal"}, //colum 명세
                "date = ? and type = ?", //where
                new String[] {date, type}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

        Log.i("table", date + "" + type + "");
        return c;
    }

    /* Insert Scheduler Data */
    public void insertDailyFood(String date, String type, String foodname, double kcal) {
        try {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("type", type);
            values.put("foodname", foodname);
            values.put("kcal", kcal);
            db.insert(TABLE_DAILY_FOOD, null, values);

            Log.i("table", date + "" + type + "" + foodname + "" +kcal);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor sumDailyFoodKcal(String date, String type) {
        Cursor c = db.rawQuery("select sum(kcal) from " + TABLE_DAILY_FOOD + " where date = ? and type = ?;", new String[] {date, type});
        c.moveToFirst();
        Log.i("sumDailyFoodKcal", c.getDouble(0) +"");
        return c;
    }

    /* Read Specific Food Type List */
    public Cursor readFoodList(String foodType) {
        Cursor c = db.query(TABLE_DAILY_FOOD, //table name
                new String[] {"_id","foodname", "kcal"}, //colum 명세
                "date = ? and type = ?", //where
                new String[] {foodType, foodType}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

//        Log.i("table", date + "" + type + "");
        return c;
    }


    public void deleteDailyFood(int id) {
        deleteDailyFood(id);
    }


}
