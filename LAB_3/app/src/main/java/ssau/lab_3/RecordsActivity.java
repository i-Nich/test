package ssau.lab_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ssau.lab_3.Adapters.RecordAdapter;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Photo;
import ssau.lab_3.DBModel.Record;

public class RecordsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener/*, AdapterView.OnItemLongClickListener*/ {
    DBHelper dbHelper;
    ListView lvRecords;
    Record[] records;
    Record selectedRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        setTitle("Записи");
        lvRecords = (ListView) findViewById(R.id.lvRecords);
        dbHelper = new DBHelper(this);
        lvRecords.setOnItemClickListener(this);
        registerForContextMenu(lvRecords);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecords();
    }

    private void fillRecords(){
        records = Record.getRecords(dbHelper);
        RecordAdapter adapter = new RecordAdapter(this, records);
        lvRecords.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        selectedRecord = (Record) ((AdapterView.AdapterContextMenuInfo) menuInfo).targetView.getTag();
        menu.add(0, 0, 0, "Изменить");
        menu.add(0, 1, 0, "Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == 0) {
            Intent intent = new Intent(this, RecordActivity.class);
            intent.putExtra("rec_id", selectedRecord.getId());
            startActivity(intent);
        } else {
            if (selectedRecord != null) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedRecord.delete(dbHelper);
                        fillRecords();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", null);
                alert.show();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record) view.getTag();
        if (record != null) {
            Photo[] photos = Photo.getPhotos(dbHelper, record.getId());
            if (photos.length > 0) {
                Intent intent = new Intent(this, PhotosActivity.class);
                intent.putExtra("id", record.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Нет фотографий", Toast.LENGTH_LONG).show();
            }
        }
    }



//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        selectedRecord = (Record) view.getTag();
//
//    }
}