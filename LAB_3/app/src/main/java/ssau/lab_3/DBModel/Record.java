package ssau.lab_3.DBModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class Record {

    private int id;
    private long category_id;
    private long start_time;
    private long end_time;
    private long time_interval;
    private String description;
    private Category category;
    public Category getCategory() {
        return category;
    }
    public int getId() {
        return id;
    }
    public long getCategory_id() {
        return category_id;
    }
    public long getStart_time() {
        return start_time;
    }
    public long getEnd_time() {
        return end_time;
    }
    public long getTime_interval() {
        return time_interval;
    }
    public String getDescription() {
        return description;
    }

    public Record(long category_id, long start_time, long end_time, long time_interval, String description) {
        this.category_id = category_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.time_interval = time_interval;
        this.description = description;
    }

    public Record(int id, long category_id, long start_time, long end_time, long time_interval, String description) {
        this.id = id;
        this.category_id = (category_id == 0 ? 1 : category_id);
        this.start_time = start_time;
        this.end_time = end_time;
        this.time_interval = time_interval;
        this.description = description;
    }

    public static Record[] getMaxCountRecords(DBHelper dbHelper, long start, long end) {
        Category[] categories = Category.getCategories(dbHelper);
        Category category = null;
        long maxCount = -1;
        for (Category category1 : categories) {
            long count = getCount(dbHelper, category1, start, end);
            if (count > maxCount) {
                maxCount = count;
                category = category1;
            }
        }
        return getRecords(dbHelper, category, start, end);
    }

    public static Record[] getMaxSumInterval(DBHelper dbHelper, long start, long end) {
        Category[] categories = Category.getCategories(dbHelper);
        Category category = null;
        long maxTime = -1;
        for (Category category1 : categories) {
            long t = getSumInterval(dbHelper, category1, start, end);
            if (t > maxTime) {
                maxTime = t;
                category = category1;
            }
        }
        return getRecords(dbHelper, category, start, end);
    }

    public static long getCount(DBHelper dbHelper, Category category, long start, long end) {
        long count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] cols = new String[]{"COUNT(" + DBHelper.ID + ")"};
        String where = "(" + DBHelper.CATEGORY_ID + "=" + category.getId() + ") AND (" +
                DBHelper.START_TIME + ">=" + start + ") AND (" +
                DBHelper.END_TIME + "<=" + end + ")";
        Cursor cursor = db.query(DBHelper.TB_RECORD, cols, where, null, null, null, null);
        if (cursor.moveToFirst()) {
            count = cursor.getLong(0);
        }
        return count;
    }

    public static long getSumInterval(DBHelper dbHelper, Category category, long start, long end) {
        long time = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //String[] cols = new String[]{"SUM(" + DBHelper.TIME_INTERVAL + ")"};
        String[] cols = new String[]{DBHelper.TIME_INTERVAL};
        String where = "(" + DBHelper.CATEGORY_ID + "=" + category.getId() + ") AND (" +
                DBHelper.START_TIME + ">=" + start + ") AND (" +
                DBHelper.END_TIME + "<=" + end + ")";
        Cursor cursor = db.query(DBHelper.TB_RECORD, cols, where, null, null, null, null);
        if (cursor.moveToFirst())
        {   do
        {
            time = time+cursor.getLong(0)+ Calendar.getInstance().getTimeZone().getRawOffset();
        }
        while (cursor.moveToNext());
        }
        return time;
    }

    public static long getSumInterval(DBHelper dbHelper, Category[] categories, long start, long end) {
        long time = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //String[] cols = new String[]{"SUM(" + DBHelper.TIME_INTERVAL + ")"};
        String[] cols = new String[]{DBHelper.TIME_INTERVAL};
        String where = "";
        for (Category category : categories) {
            if (where.isEmpty()) {
                where = "(" + DBHelper.CATEGORY_ID + "=" + category.getId();
            } else {
                where += " OR " + DBHelper.CATEGORY_ID + "=" + category.getId();
            }
        }
        if (where.isEmpty()) {
            where = "(" +
                    DBHelper.START_TIME + ">=" + start + ") AND (" +
                    DBHelper.END_TIME + "<=" + end + ")";
        } else {
            where += ") AND (" +
                    DBHelper.START_TIME + ">=" + start + ") AND (" +
                    DBHelper.END_TIME + "<=" + end + ")";
        }
        Cursor cursor = db.query(DBHelper.TB_RECORD, cols, where, null, null, null, null);
        /*if (cursor.moveToFirst()) {
            time = cursor.getLong(0);
        }*/
        if (cursor.moveToFirst())
        {   do
        {
            time = time+cursor.getLong(0)+ Calendar.getInstance().getTimeZone().getRawOffset();
        }
        while (cursor.moveToNext());
        }
        return time;
    }

    public static Record[] getRecords(DBHelper dbHelper, Category category, long start, long end) {
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = "(" + DBHelper.CATEGORY_ID + "=" + category.getId() + ") AND (" +
                DBHelper.START_TIME + ">=" + start + ") AND (" +
                DBHelper.END_TIME + "<=" + end + ")";
        Cursor cursor = db.query(DBHelper.TB_RECORD, null, where, null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    rid = cursor.getColumnIndex(DBHelper.ID),
                    rcat_id = cursor.getColumnIndex(DBHelper.CATEGORY_ID),
                    rstart = cursor.getColumnIndex(DBHelper.START_TIME),
                    rend = cursor.getColumnIndex(DBHelper.END_TIME),
                    rtime = cursor.getColumnIndex(DBHelper.TIME_INTERVAL),
                    rdescr = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            do {
                Record record = new Record(
                        cursor.getInt(rid),
                        cursor.getInt(rcat_id),
                        cursor.getLong(rstart),
                        cursor.getLong(rend),
                        cursor.getLong(rtime),
                        cursor.getString(rdescr)
                );
                record.category = Category.getCategory(dbHelper, (int) record.category_id);
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records.toArray(new Record[0]);
    }

    public static Record[] getRecords(DBHelper dbHelper, Category category) {
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_RECORD, null, DBHelper.CATEGORY_ID + "=" + category.getId(), null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    rid = cursor.getColumnIndex(DBHelper.ID),
                    rcat_id = cursor.getColumnIndex(DBHelper.CATEGORY_ID),
                    rstart = cursor.getColumnIndex(DBHelper.START_TIME),
                    rend = cursor.getColumnIndex(DBHelper.END_TIME),
                    rtime = cursor.getColumnIndex(DBHelper.TIME_INTERVAL),
                    rdescr = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            do {
                Record record = new Record(
                        cursor.getInt(rid),
                        cursor.getInt(rcat_id),
                        cursor.getLong(rstart),
                        cursor.getLong(rend),
                        cursor.getLong(rtime),
                        cursor.getString(rdescr)
                );
                record.category = Category.getCategory(dbHelper, (int) record.category_id);
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records.toArray(new Record[0]);
    }

    public static Record[] getRecords(DBHelper dbHelper) {
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_RECORD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int
                    rid = cursor.getColumnIndex(DBHelper.ID),
                    rcat_id = cursor.getColumnIndex(DBHelper.CATEGORY_ID),
                    rstart = cursor.getColumnIndex(DBHelper.START_TIME),
                    rend = cursor.getColumnIndex(DBHelper.END_TIME),
                    rtime = cursor.getColumnIndex(DBHelper.TIME_INTERVAL),
                    rdescr = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            do {
                Record record = new Record(
                        cursor.getInt(rid),
                        cursor.getInt(rcat_id),
                        cursor.getLong(rstart),
                        cursor.getLong(rend),
                        cursor.getLong(rtime),
                        cursor.getString(rdescr)
                );
                record.category = Category.getCategory(dbHelper, (int) record.category_id);
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records.toArray(new Record[0]);
    }

    public static Record getRecord(DBHelper dbHelper, int rec_id) {
        Record record = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_RECORD, null, DBHelper.ID + "=" + rec_id, null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    rid = cursor.getColumnIndex(DBHelper.ID),
                    rcat_id = cursor.getColumnIndex(DBHelper.CATEGORY_ID),
                    rstart = cursor.getColumnIndex(DBHelper.START_TIME),
                    rend = cursor.getColumnIndex(DBHelper.END_TIME),
                    rtime = cursor.getColumnIndex(DBHelper.TIME_INTERVAL),
                    rdescr = cursor.getColumnIndex(DBHelper.DESCRIPTION);
            record = new Record(
                    cursor.getInt(rid),
                    cursor.getInt(rcat_id),
                    cursor.getLong(rstart),
                    cursor.getLong(rend),
                    cursor.getLong(rtime),
                    cursor.getString(rdescr)
            );
            record.category = Category.getCategory(dbHelper, (int) record.category_id);
        }
        cursor.close();
        return record;
    }

    public static long insert(DBHelper dbHelper, Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CATEGORY_ID, record.category_id);
        cv.put(DBHelper.START_TIME, record.start_time);
        cv.put(DBHelper.END_TIME, record.end_time);
        cv.put(DBHelper.TIME_INTERVAL, record.time_interval);
        cv.put(DBHelper.DESCRIPTION, record.description);
        record.id = (int) db.insert(DBHelper.TB_RECORD, null, cv);
        return record.id;
    }

    public void delete(DBHelper dbHelper) {
        Photo[] photos = Photo.getPhotos(dbHelper, id);
        for (Photo photo : photos) {
            photo.delete(dbHelper);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TB_RECORD, DBHelper.ID + "=" + id, null);
    }

    public void update(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CATEGORY_ID, category_id);
        cv.put(DBHelper.START_TIME, start_time);
        cv.put(DBHelper.END_TIME, end_time);
        cv.put(DBHelper.TIME_INTERVAL, time_interval);
        cv.put(DBHelper.DESCRIPTION, description);
        db.update(DBHelper.TB_RECORD, cv, DBHelper.ID + "=" + id, null);
    }
}