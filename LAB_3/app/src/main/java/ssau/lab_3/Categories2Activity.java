package ssau.lab_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ssau.lab_3.Adapters.CategoryAdapter;
import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;

public class Categories2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvCategories;
    DBHelper dbHelper;
    int longKlickItemID;
    CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //добавление категорий
        Intent intentAdd = new Intent(this,AddCategoryActivity.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startAddCategoryActiviry(0);
            }
        });

        lvCategories = (ListView) findViewById(R.id.lvCategories);
        lvCategories.setOnItemClickListener(this);
        //lvCategories.setOnCreateContextMenuListener(this);
        dbHelper = new DBHelper(this);
        registerForContextMenu(lvCategories);
        setTitle("Категории");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Category[] categories = Category.getCategories(dbHelper);
        adapter = new CategoryAdapter(this, categories);
        lvCategories.setAdapter(adapter);
    }
    public void fill(){
        Category[] categories = Category.getCategories(dbHelper);
        adapter = new CategoryAdapter(this, categories);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,0,Menu.NONE,"Изменить");
        menu.add(Menu.NONE,1,Menu.NONE,"Удалить");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //Toast toast = Toast.makeText(this, info.position+"", Toast.LENGTH_SHORT);
        //toast.show();
        longKlickItemID=info.position;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                startAddCategoryActiviry(1,adapter.getItem(longKlickItemID));
                break;
            case 1:
                adapter.getItem(longKlickItemID).delete(dbHelper);
                fill();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onStop() {
        dbHelper.close();
        super.onStop();
    }
    public void startAddCategoryActiviry(int operation)
    {
        Intent intent = new Intent(this,AddCategoryActivity.class);
        switch (operation)
        {
            case 0:
                intent.putExtra("operatio",0);
                break;
            case 1:
                intent.putExtra("operatio",1);
                break;
        }
        startActivity(intent);
    }
    public void startAddCategoryActiviry(int operation,Category category)
    {
        Intent intent = new Intent(this,AddCategoryActivity.class);
        switch (operation)
        {
            case 0:
                intent.putExtra("operatio",0);
                break;
            case 1:
                intent.putExtra("operatio",1);
                intent.putExtra("category",new String[]{category.getId()+"",category.getIcon_id()+"",category.getName()});
                break;
        }
        startActivity(intent);
    }

}
