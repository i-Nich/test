package ssau.lab_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ssau.lab_3.DBModel.Category;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.icon.IconManager;
import ssau.lab_3.icon.IconSelectActivity;

public class AddCategoryActivity extends AppCompatActivity{
    int ic_id=1;
    int cat_id;
    ImageView iconSelected;
    EditText categoryName;
    EditText categoryDescr;
    int op;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        iconSelected=(ImageView) findViewById(R.id.imageIconSelected);
        categoryName = (EditText) findViewById(R.id.editText3);
        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        op= intent.getIntExtra("operatio",2);
        switch (op)
        {
            case 0:break;
            case 1:
                String[] category = intent.getStringArrayExtra("category");
                cat_id=Integer.parseInt(category[0]);
                ic_id=Integer.parseInt(category[1]);
                iconSelected.setImageResource(IconManager.getIcon(ic_id));
                categoryName.setText(category[2]);
                break;
        }
    }
    public void onIconSelectClick(View view){
        Intent intentAdd = new Intent(this,IconSelectActivity.class);
        startActivityForResult(intentAdd,1);

    }
    public void onAddClick(View view)
    {
        switch (op)
        {
            case 0:
                Category.insertCategory(dbHelper,new Category(Category.getMaxId(dbHelper)+1,ic_id,categoryName.getText().toString()));
                break;
            case 1:
                Category.update(dbHelper,cat_id,ic_id,categoryName.getText().toString());
                break;
        }
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        ic_id = data.getIntExtra("ic_number",0);
        iconSelected.setImageResource(IconManager.getIcon(ic_id));
    }
    @Override
    protected void onStop() {
        dbHelper.close();
        super.onStop();
    }
}
