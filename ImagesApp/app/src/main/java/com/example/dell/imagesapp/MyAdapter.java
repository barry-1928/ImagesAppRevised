package com.example.dell.imagesapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by dell on 16-06-2017.
 */

public class MyAdapter extends BaseAdapter {

    ArrayList<ListDetails> list_details = new ArrayList<>();
    int n,i;
    Context context;
    MainActivity mainActivity;
    MyAdapter(Context context, ArrayList<String> image_paths, ArrayList<String> captions, ArrayList<Integer> decider, MainActivity mainActivity) {
        this.context = context;
        n = image_paths.size();
        for(i = 0; i < n; i++ ) {
            ListDetails temp = new ListDetails(image_paths.get(i), captions.get(i),decider.get(i));
            list_details.add(i,temp);
        }
        this.mainActivity = mainActivity;
    }

    void refresh(ArrayList<String> image_paths, ArrayList<String> captions, ArrayList<Integer> decider) {
        list_details.clear();
        n = image_paths.size();
        for(i = 0; i < n; i++ ) {
            ListDetails temp = new ListDetails(image_paths.get(i), captions.get(i),decider.get(i));
            list_details.add(i,temp);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return list_details.size();
    }

    @Override
    public Object getItem(int position) {
        return list_details.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_row,parent,false);
            holder = new Holder(row);
            row.setTag(holder);
        }
        else {
            holder = (Holder) row.getTag();
        }
        if(n>8) {
            Log.d("find","size is "+n);
        }
        ListDetails temp = list_details.get(position);
        holder.textView.setText(temp.caption);
        mainActivity.compress_image(temp.image_uri,holder);

        return row;
    }
}
