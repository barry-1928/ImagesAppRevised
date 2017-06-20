package com.example.dell.imagesapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by dell on 16-06-2017.
 */

public class ListDetails {
    Uri image_uri;
    String caption;
    ListDetails(String path, String caption, int decider) {
        switch (decider) {
            case 0:
                image_uri = Uri.parse(path);
                break;
            case 1:
                File file = new File(path);
                image_uri = Uri.fromFile(file);
                if (!(file.exists())) {
                    Log.d("find", "File could not be opened!");
                }
                break;
            case 2:
                image_uri = Uri.parse(path);
                break;
        }
        this.caption = caption;
    }
}
