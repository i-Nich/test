package ssau.lab_3.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ssau.lab_3.DBModel.Category;
import ssau.lab_3.R;
import ssau.lab_3.icon.IconManager;


public class CategoryAdapter extends ArrayAdapter<Category> {
    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, Category[] categories) {
        super(context, R.layout.category_list_item, categories);
        this.context = context;
        this.categories = new ArrayList<Category>(Arrays.asList(categories));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvCategory);
        ImageView icon = (ImageView) rowView.findViewById(R.id.iconView);

        textView.setText(categories.get(position).getName());
        icon.setImageResource(IconManager.getIcon(categories.get(position).getIcon_id()));
        rowView.setTag(categories.get(position));
        return rowView;
    }

    @Override
    public void remove(@Nullable Category object) {
        categories.remove(object);


    }

}