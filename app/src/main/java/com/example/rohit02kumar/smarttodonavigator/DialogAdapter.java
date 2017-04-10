package com.example.rohit02kumar.smarttodonavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohit02kumar.smarttodonavigator.database.Event;
import com.google.android.gms.vision.text.Text;

import java.util.List;

/**
 * Created by rohit02.kumar on 4/10/2017.
 */
public class DialogAdapter extends ArrayAdapter<Event>  {
    List<Event> events;
    Context mContext;

    public DialogAdapter(Context context, List<Event> events) {
        super(context, R.layout.item,events);
        this.events = events;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtFrom;
        TextView txtTo;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event=events.get(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtFrom = (TextView) convertView.findViewById(R.id.from);
            viewHolder.txtTo = (TextView) convertView.findViewById(R.id.to);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(event.getmEventName());
        viewHolder.txtType.setText(event.getmEvenType());
        viewHolder.txtFrom.setText(event.getmFromDate());
        viewHolder.txtTo.setText(event.getmToDate());

        return convertView;
    }
}
