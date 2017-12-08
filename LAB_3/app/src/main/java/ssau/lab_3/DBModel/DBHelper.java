package ssau.lab_3.DBModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "times";

    public static final String TB_CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String NAME = "name";
    public static final String ICON_ID = "icon_id";

    public static final String TB_RECORD = "record";
    public static final String RECORD_ID = "record_id";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String TIME_INTERVAL = "time_interval";

    public static final String TB_PHOTO = "photo";
    public static final String IMAGE = "image";

    public static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + TB_CATEGORY + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    ICON_ID + " INTEGER NOT NULL," +
                    NAME + " TEXT NOT NULL);";

    public static final String CREATE_RECORD_TABLE =
            "CREATE TABLE "+ TB_RECORD + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CATEGORY_ID + " INTEGER NOT NULL," +
                    START_TIME + " INTEGER NOT NULL," +
                    END_TIME + " INTEGER NOT NULL," +
                    DESCRIPTION + " TEXT NOT NULL," +
                    TIME_INTERVAL + " INTEGER NOT NULL);";

    public static final String CREATE_PHOTO_TABLE =
            "CREATE TABLE " + TB_PHOTO + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RECORD_ID + " INTEGER NOT NULL," +
                    IMAGE + " BLOB);";

    public static final String ADD_CATEGORIES =
            "INSERT INTO " + TB_CATEGORY + " (" +
                    ID + ", " +
                    ICON_ID + ", " +
                    NAME + ") VALUES (1, 1, 'Работа'), (2, 2, 'Обед'), (3, 3, 'Отдых'), (4, 4, 'Уборка'), (5, 5, 'Сон');";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
        db.execSQL(CREATE_PHOTO_TABLE);
        db.execSQL(ADD_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}