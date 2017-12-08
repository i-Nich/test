package ssau.lab_3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ssau.lab_3.DBModel.DBHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCategories, btnRecords, btnFrequentCategory, btnBiggestSumTimeByCategory, btnTimeByCategories, btnTimeDiagram;
    DBHelper dbHelper;
    Boolean permissionToWriteAccepted;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        setTitle("Меню");
        btnCategories = (Button) findViewById(R.id.btnCategories);
        btnRecords = (Button) findViewById(R.id.btnRecords);
        btnFrequentCategory = (Button) findViewById(R.id.btnFrequentCategory);
        btnBiggestSumTimeByCategory = (Button) findViewById(R.id.btnBiggestSumTimeByCategory);
        btnTimeByCategories = (Button) findViewById(R.id.btnTimeByCategories);
        btnTimeDiagram = (Button) findViewById(R.id.btnTimeDiagram);

        btnCategories.setOnClickListener(this);
        btnRecords.setOnClickListener(this);
        btnFrequentCategory.setOnClickListener(this);
        btnBiggestSumTimeByCategory.setOnClickListener(this);
        btnTimeByCategories.setOnClickListener(this);
        btnTimeDiagram.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                permissionToWriteAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == btnCategories) {
            intent = new Intent(this, Categories2Activity.class);

        } else if (v == btnRecords){
            intent = new Intent(this, RecordsActivity.class);
        } else if (v.getId() == R.id.btnTimeByCategories){
            intent = new Intent(this, CategoryTimesActivity.class);
        } else if (v.getId() == R.id.btnBiggestSumTimeByCategory){
            intent = new Intent(this, MaxTimeRecordsActivity.class);
        } else if (v.getId() == R.id.btnFrequentCategory){
            intent = new Intent(this, MaxCountRecordsActivity.class);
        } else if (v.getId() == R.id.btnTimeDiagram){
            intent = new Intent(this, CircleDiagramActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        dbHelper.close();
        super.onStop();
    }
}