package ncrc.nise.ajou.ac.kr.opa.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class OpaDataDBAdapter {
    static final String TAG = "OpaDataDBAdapter";

    static final String TABLE_FOOD = "food";
    static final String TABLE_ACTIVITY = "activity";

    private Context context;
    private SQLiteDatabase db;

    public OpaDataDBAdapter(Context context) {
        this.context = context;
        this.open();
    }

    /* Open DB */
    private void open() {
        try {
            db = (new OpaDataDBHelper(context).getWritableDatabase());
            Log.e("aafw", "aafw");
        } catch(SQLiteException e) {
            db = (new OpaDataDBHelper(context).getReadableDatabase());
        }
    }

    /* Close DB */
    public void Close() {
        db.close();
    }

    public Cursor readFood(String type) {
        Log.i("OpaDataDBAdapter", type);
        Cursor c = db.query(TABLE_FOOD, //table name
                new String[]{"rowid _id", "name", "type", "kcal"}, //colum 명세
                "type = ?", //where
                new String[]{type}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

        return c;
    }

    public Cursor readActivity(String type) {
        Cursor c = db.query(TABLE_ACTIVITY, //table name
                new String[]{"_id", "name", "type", "place", "strength", "kcal10min50kg", "kcal10min60kg", "kcal10min70kg"}, //colum 명세
                "type = ?", //where
                new String[]{type}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_id" + " DESC" //order by
        );

        return c;
    }
}
