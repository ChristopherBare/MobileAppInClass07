package com.christopherbare.inclass07;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);

        TextView title = convertView.findViewById(R.id.articleTitle);
        TextView authorName = convertView.findViewById(R.id.authorName);
        TextView releaseDate = convertView.findViewById(R.id.releaseDate);
        ImageView image = convertView.findViewById(R.id.image);


        //set the data from the contact object
        title.setText(article.getTitle());
        authorName.setText(article.getAuthorName());
        releaseDate.setText(article.getReleaseDate());

        //image
        if (!article.getImageURL().isEmpty())
        Picasso.get().load(article.getImageURL()).into(image);

        return convertView;
    }
}
