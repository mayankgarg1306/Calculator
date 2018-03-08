package com.calculator.hide.vault;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import java.util.ArrayList;


class customlist extends ArrayAdapter<String> {

    customlist(Context context, ArrayList files) {
        super(context,R.layout.listview,files);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View CustomView=convertView;
        if(CustomView==null) {
           LayoutInflater myInflater = LayoutInflater.from(getContext());
           CustomView = myInflater.inflate(R.layout.listview, null);
       }

        RequestOptions req = new RequestOptions();
        req.diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        File files=new File(getItem(position));
        String name=getItem(position).substring(getItem(position).lastIndexOf("/") + 1);

        TextView textView=(TextView)CustomView.findViewById(R.id.textView);
        ImageView imageView=(ImageView)CustomView.findViewById(R.id.imageView);

        textView.setText(name);
        String type=getItem(position).substring(getItem(position).lastIndexOf(".") + 1);

        String TAG = "hello";
        if(files.isDirectory()) {
          if(files.exists()){
              Glide.with(getContext())
                      .load(R.drawable.folder0)
                      .into(imageView);
        }
        }

        else if(type.toLowerCase().equals("jpg")||type.toLowerCase().equals("jpeg")||
                type.toLowerCase().equals("png")||type.toLowerCase().equals("mp4")||
                type.toLowerCase().equals("avi")|| type.toLowerCase().equals("flv")||
                type.toLowerCase().equals("mkv")){
                if(files.exists()){
                    Glide.with(getContext())
                            .load(new File(getItem(position))).apply(req)
                            .thumbnail(0.1f)
                            .into(imageView);
                    Log.i(TAG,"image");
                }
        }

        else if(type.toLowerCase().equals("gif")){
            Glide.with(getContext())
                    .load(new File(getItem(position))).apply(req)
                    .into(imageView);

        }
        else if(type.toLowerCase().equals("mp3")||type.toLowerCase().equals("wma")||
                type.toLowerCase().equals("wav")||type.toLowerCase().equals("mid")) {
            Glide.with(getContext())
                    .load(R.drawable.mp3)
                    .into(imageView);
        }
        else
            Glide.with(getContext())
                    .load(R.drawable.file)
                    .into(imageView);

        return CustomView;
    }

}

