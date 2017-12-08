package ssau.lab_3.DBModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Category {
    private int id;
    private int icon_id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Category(int id, int icon_id, String name) {
        this.id = id;
        this.icon_id = icon_id;
        this.name = name;
    }
    public int getIcon_id() {
        return icon_id;
    }
    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public static Category getCategory(DBHelper dbHelper,int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            int
                    cID = cursor.getColumnIndex(DBHelper.ID),
                    cICON_ID=cursor.getColumnIndex(DBHelper.ICON_ID),
                    cName = cursor.getColumnIndex(DBHelper.NAME);
            do
            {
                Category category = new Category(cursor.getInt(cID),cursor.getInt(cICON_ID),cursor.getString(cName));
                if (category.id == id)
                {
                    cursor.close();
                    return category;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }
    public static Category[] getCategories(DBHelper dbHelper) {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            int
                    cID = cursor.getColumnIndex(DBHelper.ID),
                    cICON_ID=cursor.getColumnIndex(DBHelper.ICON_ID),
                    cName = cursor.getColumnIndex(DBHelper.NAME);
            do
            {
                Category category = new Category(cursor.getInt(cID),cursor.getInt(cICON_ID),cursor.getString(cName));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories.toArray(new Category[0]);
    }
    public static void insertCategory(DBHelper dbHelper, Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ID,category.getId());
        cv.put(DBHelper.ICON_ID, category.getIcon_id());
        cv.put(DBHelper.NAME,category.getName());
        db.insert(DBHelper.TB_CATEGORY, null, cv);
    }
    public void delete(DBHelper dbHelper) {
        Record[] records = Record.getRecords(dbHelper,this);
        for (Record record:records)
        {
            record.delete(dbHelper);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TB_CATEGORY, DBHelper.ID + "=" + id, null);
    }

   public static void update(DBHelper dbHelper,int id_cat,int im_id,String name_cat){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ID, id_cat);
        cv.put(DBHelper.ICON_ID, im_id);
        cv.put(DBHelper.NAME, name_cat);
        db.update(DBHelper.TB_CATEGORY, cv, DBHelper.ID + "=" + id_cat, null);
    }
    public static int getMaxId(DBHelper dbHelper)
    {
        Category[]categories=getCategories(dbHelper);
        return categories[categories.length-1].getId();
    }
}