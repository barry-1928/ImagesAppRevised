package com.example.dell.imagesapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 16-06-2017.
 */

public class Holder {
    ImageView imageView;
    TextView textView;

    Holder(View view) {
        imageView = (ImageView) view.findViewById(R.id.image_id);
        textView = (TextView) view.findViewById(R.id.caption_id);
    }
}
