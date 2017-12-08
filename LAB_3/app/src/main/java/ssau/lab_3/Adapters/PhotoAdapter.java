package ssau.lab_3.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import ssau.lab_3.R;

public class PhotoAdapter extends ArrayAdapter<Bitmap> {
    Context context;
    Bitmap[] photos;

    public PhotoAdapter(Context context, Bitmap[] photos) {
        super(context, R.layout.photo_list_item, photos);
        this.context = context;
        this.photos = photos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.photo_list_item, parent, false);
        ImageView image = (ImageView) rowView.findViewById(R.id.ivImage);
        image.setImageBitmap(photos[position]);
        return rowView;
    }
}