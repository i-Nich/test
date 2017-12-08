package ssau.lab_3.DBModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class Photo
{
    private int id;
    private int record_id;
    private byte[] image;

    public int getId() {
        return id;
    }
    public int getRecord_id() {
        return record_id;
    }
    public byte[] getImage() {
        return image;
    }

    public Photo(int record_id, byte[] image) {
        this.record_id = record_id;
        this.image = image;
    }

    public Photo(int id, int record_id, byte[] image) {
        this.id = id;
        this.record_id = record_id;
        this.image = image;
    }

    public static long insert(DBHelper dbHelper, Photo photo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.RECORD_ID, photo.record_id);
        cv.put(DBHelper.IMAGE, photo.image);
        photo.id = (int) db.insert(dbHelper.TB_PHOTO, null, cv);
        return photo.id;
    }

    public static Photo[] getPhotos(DBHelper dbHelper, int rec_id){
        Photo[] photos = getPhotos(dbHelper);
        ArrayList<Photo> p = new ArrayList<>();
        for (Photo photo: photos){
            if (photo.record_id == rec_id){
                p.add(photo);
            }
        }
        return p.toArray(new Photo[0]);

    }

    public static Photo[] getPhotos(DBHelper dbHelper) {
        ArrayList<Photo> photos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_PHOTO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    pID = cursor.getColumnIndex(DBHelper.ID),
                    prID = cursor.getColumnIndex(DBHelper.RECORD_ID),
                    pIMG = cursor.getColumnIndex(DBHelper.IMAGE);
            do {
                Photo photo = new Photo(
                        cursor.getInt(pID),
                        cursor.getInt(prID),
                        cursor.getBlob(pIMG)
                );
                photos.add(photo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return photos.toArray(new Photo[0]);
    }

    public void delete(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TB_PHOTO, DBHelper.ID + "=" + id, null);
    }
}