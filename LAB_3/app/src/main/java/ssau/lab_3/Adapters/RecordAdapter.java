package ssau.lab_3.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ssau.lab_3.DBModel.Record;
import ssau.lab_3.R;


public class RecordAdapter extends ArrayAdapter<Record> {
    Context context;
    Record[] records;

    public RecordAdapter(Context context, Record[] records) {
        super(context, R.layout.records_list_item,records);
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.records_list_item, parent, false);
        if (position % 2 == 0)
            rowView.setBackground(new ColorDrawable(Color.argb(255,200,200,200)));
        TextView textView = (TextView) rowView.findViewById(R.id.tvCategory);
        textView.setText(records[position].getCategory().getName());
        DateFormat tf = new SimpleDateFormat("HH:mm");
        DateFormat dtf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        Date time = new Date(records[position].getTime_interval());
        textView = (TextView) rowView.findViewById(R.id.tvInterval);
        textView.setText(tf.format(time));

        time = new Date(records[position].getStart_time());
        textView = (TextView) rowView.findViewById(R.id.tvStart);
        String text = dtf.format(time);
        textView.setText(dtf.format(time));

        time = new Date(records[position].getEnd_time());
        textView = (TextView) rowView.findViewById(R.id.tvEnd);
        textView.setText(dtf.format(time));

        textView = (TextView) rowView.findViewById(R.id.tvDescr);
        textView.setText(records[position].getDescription());

        rowView.setTag(records[position]);
        return rowView;
    }


}
