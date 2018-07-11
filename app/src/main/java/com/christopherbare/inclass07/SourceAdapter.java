package com.christopherbare.inclass07;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SourceAdapter extends ArrayAdapter<Source> {

    public SourceAdapter(Context context, int resource, List<Source> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Source source = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_source_item, parent, false);

        TextView name = convertView.findViewById(R.id.name);
        TextView id = convertView.findViewById(R.id.sourceID);

        //set the data from the contact object
        name.setText(source.getName());
        id.setText(source.getId());
        id.setVisibility(View.GONE);

        return convertView;
    }
}
