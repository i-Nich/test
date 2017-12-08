package ssau.lab_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ssau.lab_3.Adapters.PhotoAdapter;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Photo;

public class PhotosActivity extends AppCompatActivity {
    Photo[] photos;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    int rec_id;
    ListView lvPhotos;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        rec_id = getIntent().getIntExtra("id", 0);
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        dbHelper = new DBHelper(this);
        photos = Photo.getPhotos(dbHelper, rec_id);
        bitmaps.clear();
        for (Photo photo : photos) {
            byte[] p = photo.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(p, 0, p.length);
            bitmaps.add(bitmap);
        }
        setTitle("Фотки");
    }

    @Override
    protected void onResume() {
        super.onResume();
        PhotoAdapter adapter = new PhotoAdapter(this, bitmaps.toArray(new Bitmap[0]));
        lvPhotos.setAdapter(adapter);
    }
}