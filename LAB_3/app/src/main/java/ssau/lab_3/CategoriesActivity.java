package ssau.lab_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ssau.lab_3.Adapters.CategoryAdapter;
import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvCategories;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        lvCategories = (ListView) findViewById(R.id.lvCategories);
        lvCategories.setOnItemClickListener(this);
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Category[] categories = Category.getCategories(dbHelper);
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        lvCategories.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) view.getTag();
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra("id", category.getId());
        intent.putExtra("name", category.getName());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        dbHelper.close();
        super.onStop();
    }
}