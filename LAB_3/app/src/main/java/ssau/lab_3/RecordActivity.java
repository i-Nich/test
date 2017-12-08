package ssau.lab_3;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ssau.lab_3.Adapters.PhotoAdapter;
import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Photo;
import ssau.lab_3.DBModel.Record;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvStartTime, tvEndTime, tvTimeInterval;
    EditText etDescription;
    Button btnPhoto, btnSave,btnGalary;
    Spinner sCategories;
    ListView lvPhotos;
    String TAG = "MY_TAG";
    long categoryID;
    Date startTime;
    Date endTime;
    Date interval;
    DBHelper dbHelper;
    ArrayList<Bitmap> photos = new ArrayList<>();
    ArrayList<Photo> photoArrayList = new ArrayList<>();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY_REQUEST = 2;
    Record record;
    int longKlickItemID;
    ArrayList<String> namesCateg;
    ArrayAdapter<String> categAdapter;
    Category[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_record);
        setTitle("Запись");

        sCategories = (Spinner)findViewById(R.id.sCategories);
        namesCateg=new ArrayList<>();
        categories=Category.getCategories(dbHelper);
        for (int i=0;i<categories.length;i++)
        {
            namesCateg.add(i,categories[i].getName());
        }
        categoryID = getIntent().getIntExtra("id", 0);
        if (categoryID == 0){
            int rec_id = getIntent().getIntExtra("rec_id", 0);
            record = Record.getRecord(dbHelper, rec_id);
            categoryID=record.getCategory_id();
        }
        categAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,namesCateg);
        sCategories.setAdapter(categAdapter);
        int pos=0;
        for (int i=0;i<categories.length;i++)
        {
            if(categories[i].getId()==categoryID) pos=i;
        }
        sCategories.setSelection(pos);

        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvStartTime.setOnClickListener(this);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvEndTime.setOnClickListener(this);
        tvTimeInterval = (TextView) findViewById(R.id.tvTimeInterval);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnGalary= (Button) findViewById(R.id.btnGalary);
        lvPhotos = (ListView)findViewById(R.id.lvPhotos);

        initTimes();
        fillFromRecord();
        showTimes();
        registerForContextMenu(lvPhotos);
    }
    @Override
    protected void onResume() {
        super.onResume();
        fillImagesList();
    }
    private void fillFromRecord() {
        if (record != null) {
            startTime = new Date(record.getStart_time());
            endTime = new Date(record.getEnd_time());
            interval = new Date(record.getTime_interval());
            etDescription.setText(record.getDescription());
            Photo[] ps = Photo.getPhotos(dbHelper, record.getId());
            for (Photo photo : ps) {
                photos.add(BitmapFactory.decodeByteArray(photo.getImage(), 0, photo.getImage().length));
                photoArrayList.add(photo);
            }
        }
    }
    private void fillImagesList(){
        PhotoAdapter adapter = new PhotoAdapter(this, photos.toArray(new Bitmap[0]));
        lvPhotos.setAdapter(adapter);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void dispatchTakePictureFromGalaryIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    photos.add(imageBitmap);
                    if (this.record != null) {
                        savePhoto(this.record, imageBitmap);
                    }
                }
                break;
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try
                    {
                        InputStream input = this.getContentResolver().openInputStream(selectedImage);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(input, null, options);
                        input.close();
                        int l = options.inSampleSize;
                        options.inSampleSize=calculateInSampleSize(options,options.outWidth/4,options.outHeight/4);
                        options.inJustDecodeBounds=false;
                        input = this.getContentResolver().openInputStream(selectedImage);
                        Bitmap imageBitmap=BitmapFactory.decodeStream(input, null, options);
                        input.close();
                        options.inSampleSize=l;
                        //Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        photos.add(imageBitmap);

                        if (this.record != null) {
                            savePhoto(this.record, imageBitmap);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    private void initTimes() {
        Calendar c = Calendar.getInstance();
        startTime = c.getTime();
        endTime =new Date(startTime.getTime() + 3600000);
    }
    private void showTimes(){
        DateFormat df = new SimpleDateFormat("HH:mm");
        if (startTime.getTime() >= endTime.getTime()){
            Toast.makeText(this, "Время окончания должно быть больше начала!", Toast.LENGTH_LONG).show();
            endTime.setTime(startTime.getTime() + 60000);
        }
        tvStartTime.setText(df.format(startTime));
        tvEndTime.setText(df.format(endTime));
        long end=endTime.getTime();
        long start=startTime.getTime();
        long interv=end-start;
        interval = new Date(interv-Calendar.getInstance().getTimeZone().getRawOffset());//поправка на часовой пояс
        tvTimeInterval.setText(df.format(interval));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartTime:
                setStartTime();
                break;
            case R.id.tvEndTime:
                setEndTime();
                break;
            case R.id.btnPhoto:
                dispatchTakePictureIntent();
                break;
            case R.id.btnGalary:
                dispatchTakePictureFromGalaryIntent();
                break;
            case R.id.btnSave:
                if (etDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Введите описание!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (this.record != null) {
                    this.record = new Record(
                            this.record.getId(),
                            categoryID,//sCategories.getSelectedItemPosition()+1,//this.record.getCategory_id(),
                            startTime.getTime(),
                            endTime.getTime(),
                            interval.getTime(),
                            etDescription.getText().toString());
                    this.record.update(dbHelper);
                } else {
                    Record record = new Record(
                            categoryID,//sCategories.getSelectedItemPosition()+1,
                            startTime.getTime(),
                            endTime.getTime(),
                            interval.getTime(),
                            etDescription.getText().toString());
                    Record.insert(dbHelper, record);
                    if (record.getId() < 1) {
                        Toast.makeText(getApplicationContext(), "Не удалось добавить запись!", Toast.LENGTH_LONG).show();
                    } else {
                        savePhotos(record);
                    }
                }
                dbHelper.close();
                finish();
                break;
        }
    }
    private void savePhotos(Record record) {
        for (Bitmap bitmap : photos) {
            savePhoto(record, bitmap);
        }
    }
    private void savePhoto(Record record, Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] img = stream.toByteArray();
        Photo photo = new Photo(record.getId(), img);
        Photo.insert(dbHelper, photo);
        photoArrayList.add(photo);
    }
    private void  setStartTime(){
        TimePickerDialog dialog = new TimePickerDialog(
                this                ,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 23 && minute == 59){
                            minute = 58;
                            Toast.makeText(getApplicationContext(), "Время начала не может быть больше 23:58!", Toast.LENGTH_LONG).show();
                        }
                        startTime.setHours(hourOfDay);
                        startTime.setMinutes(minute);
                        showTimes();
                    }
                }, startTime.getHours(), startTime.getMinutes(), true);
        dialog.show();
    }
    private void setEndTime(){
        TimePickerDialog dialog = new TimePickerDialog(
                this                ,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.setHours(hourOfDay);
                        endTime.setMinutes(minute);
                        showTimes();
                    }
                }, endTime.getHours(), endTime.getMinutes(), true);
        dialog.show();
    }
    @Override
    protected void onStop() {
        //dbHelper.close();
        super.onStop();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,0,Menu.NONE,"Удалить");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        longKlickItemID=info.position;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                if(record!=null)
                {
                    photoArrayList.get(longKlickItemID).delete(dbHelper);
                    photos.remove(longKlickItemID);
                    photoArrayList.remove(longKlickItemID);
                    fillImagesList();
                }
                else
                {
                    photos.remove(longKlickItemID);
                    fillImagesList();
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}