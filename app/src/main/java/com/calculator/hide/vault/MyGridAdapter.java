package com.calculator.hide.vault;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter {

    List<String> items;

    public MyGridAdapter(@NonNull Context context, List<String> items) {
        super(context,R.layout.grid_item, items);
        this.items = items;
    }

  @Override
    public View getView(int position, View convertView, ViewGroup parent) {

          if (convertView == null) {
              LayoutInflater inflater = LayoutInflater.from(getContext());
              convertView = inflater.inflate(R.layout.grid_item, null);
          }
          RequestOptions req = new RequestOptions();
          req.diskCacheStrategy(DiskCacheStrategy.RESOURCE);


          TextView text = (TextView) convertView.findViewById(R.id.textView);
          String txt = items.get(position).substring(items.get(position).lastIndexOf(".") + 1);
          text.setText(items.get(position).substring(items.get(position).lastIndexOf("/") + 1));
          ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
          ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView4);
          File file = new File(items.get(position));

          if (file.isDirectory())
              imageView.setImageResource(R.drawable.folder1);                               //for folder

          else if (txt.toLowerCase().equals("mp3") || txt.toLowerCase().equals("wma") ||        //for music
                  txt.toLowerCase().equals("wav") || txt.toLowerCase().equals("mid")) {
              imageView.setImageResource(R.drawable.mp3);
          } else if (txt.toLowerCase().equals("jpg") || txt.toLowerCase().equals("jpeg") ||      //for videos and images
                  txt.toLowerCase().equals("png")) {
              if (file.exists()) {

                  Glide.with(getContext())
                          .load(new File(items.get(position))).apply(req).apply(RequestOptions.circleCropTransform())
                          .into(imageView);
              }
          } else if (txt.toLowerCase().equals("mp4") || txt.toLowerCase().equals("avi") ||
                  txt.toLowerCase().equals("flv") || txt.toLowerCase().equals("mkv")) {
              if (file.exists()) {

                  Glide.with(getContext())
                          .load(new File(items.get(position))).apply(req).apply(RequestOptions.circleCropTransform())
                          .into(imageView);

                  imageView1.setImageResource(R.drawable.play);
              }
          } else if (txt.toLowerCase().equals("gif")) {
              Glide.with(getContext())
                      .load(new File(items.get(position))).apply(req).apply(RequestOptions.circleCropTransform())
                      .into(imageView);

          } else {
              Glide.with(getContext())
                      .load(R.drawable.file1).apply(RequestOptions.circleCropTransform())
                      .into(imageView);

          }
          return convertView;
      }
}