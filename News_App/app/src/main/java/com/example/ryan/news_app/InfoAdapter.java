package com.example.ryan.news_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/21/2017.
 */

public class InfoAdapter extends ArrayAdapter<Info> {

    public InfoAdapter(Context context) {
        super(context, -1, new ArrayList<Info>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView
                    = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);


        TextView author = (TextView) convertView.findViewById(R.id.author);


        TextView date = (TextView) convertView.findViewById(R.id.date);


        TextView section = (TextView) convertView.findViewById(R.id.section);


        Info currentNews = getItem(position);


        title.setText(currentNews.getTitle());


        author.setText(currentNews.getAuthor());


        date.setText(currentNews.getDate());


        section.setText(currentNews.getSection());

        return convertView;
    }
}
