package com.example.dell.imagesapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    MainActivity mainActivity;
    static final int GALLERY_INTENT_CODE = 1234;
    static final int CAMERA_INTENT_CODE = 123;
    int p;
    int i = 1;
    int n;
    Uri uri_camera;
    ListView images_and_captions_list;
    ArrayList<Integer> decider = new ArrayList<>();
    ArrayList<String> paths = new ArrayList<>();
    ArrayList<String> captions = new ArrayList<>();
    MyAdapter adapter = null;
    EditText caption_text_input;
    String path_to_be_added = null;
    int k1 = 0;
    int j = 0;
    TextView show_path;
    Button submit;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("stored_data1",MODE_PRIVATE);
        show_path = (TextView) findViewById(R.id.file_path);
        show_path.setVisibility(View.GONE);
        submit = (Button) findViewById(R.id.id_submit);
        submit.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_menu);
//        SharedPreferences.Editor ed = sharedPreferences.edit();
//        ed.clear();
//        ed.commit();
        mainActivity = this;
        getdata_after_reopening();
        caption_text_input = (EditText) findViewById(R.id.caption_text);
        caption_text_input.setVisibility(View.GONE);
        caption_text_input.setHint("You can enter the caption here");
        images_and_captions_list = (ListView) findViewById(R.id.images_and_captions_list);
        if(n == 0) {
            add_drawableimages_to_pathlist();
            add_drawableimagescaptions_to_captionlist();
        }
        adapter = new MyAdapter(this,paths,captions,decider,mainActivity);
        images_and_captions_list.setAdapter(adapter);
        //images_and_captions_list.setOnItemClickListener(this);
        images_and_captions_list.setOnItemLongClickListener(this);
    }


    void getdata_after_reopening() {
        SharedPreferences sharedPreferences = getSharedPreferences("stored_data1",MODE_PRIVATE);
        n = sharedPreferences.getInt("number_of_items",0);
        for(j = 0;j < n;j++) {
            paths.add(sharedPreferences.getString("path"+j,null));
            captions.add(sharedPreferences.getString("caption"+j,null));
            decider.add(sharedPreferences.getInt("decider"+j,0));
        }
    }


    void add_drawableimages_to_pathlist() {
        for(j = 0 ;j < 8 ; j++) {
            decider.add(0);
        }
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/ajantha_ellora");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/gangtok");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/goa");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/kashmir");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/keralabackwaters");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/mysorepalace");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/qutubminar");
        paths.add("android.resource://com.example.dell.gallerycamera/drawable/tajmahal");
    }


    void add_drawableimagescaptions_to_captionlist() {
        captions.add("AJANTHA ELLORA");
        captions.add("GANGTOK");
        captions.add("GOA");
        captions.add("KASHMIR");
        captions.add("KERALA BACKWATERS");
        captions.add("MYSORE PALACE");
        captions.add("QUTUB MINAR");
        captions.add("TAJ MAHAL");
    }


    void gallery_intent() {
        Intent openGalleryApplication = new Intent(Intent.ACTION_GET_CONTENT);
        openGalleryApplication.setType("image/*");
        startActivityForResult(Intent.createChooser(openGalleryApplication,"Select a picture"),GALLERY_INTENT_CODE);
    }


    void camera_intent() {
        File file = getExternalFilesDir("folder");
        File myFile = null;
        myFile = new File(file,"image"+i+".jpg");
        i++;
        uri_camera = Uri.fromFile(myFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri_camera);
        startActivityForResult(intent,CAMERA_INTENT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = convertMediaUriToPath(uri);
            show_path.setText("Path of the file is: " + path);
            show_path.setVisibility(View.VISIBLE);
            Log.d("find",""+path);
            path_to_be_added = path;
            decider.add(1);
            caption_text_input.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        else if(requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {
            String path = uri_camera.toString();
            show_path.setText("Path of the file is: " + path);
            show_path.setVisibility(View.VISIBLE);
            Log.d("find",""+path);
            path_to_be_added = path;
            decider.add(2);
            caption_text_input.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        else if(requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            adapter.list_details.get(p).image_uri = uri;
            adapter.notifyDataSetChanged();
        }
    }


    String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String path = null;
        if (cursor != null) {
            path = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return path;
    }


    void submit(View v) {
        String str = caption_text_input.getText().toString();
        if(!(path_to_be_added == null || str.equals(""))) {
            paths.add(path_to_be_added);
            captions.add(caption_text_input.getText().toString());
            adapter.refresh(paths,captions,decider);
            caption_text_input.setText("");
            show_path.setVisibility(View.GONE);
            caption_text_input.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, "You have not selected image or not entered caption. Please do so and try again.", Toast.LENGTH_SHORT).show();
        }
    }



    void compress_image(Uri uri, Holder holder) {
        BitmapFactory.Options bmoptions = new BitmapFactory.Options();
        bmoptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(),bmoptions);
        int givenwidth = bmoptions.outWidth;
        float givenheight = bmoptions.outHeight;
        float reqwidth = holder.imageView.getWidth();
        float reqheight = holder.imageView.getHeight();

        float scale_factor = Math.min(givenwidth / reqwidth, givenheight / reqheight);
        bmoptions.inSampleSize = (int) scale_factor;

        bmoptions.inJustDecodeBounds = false;
        InputStream is = null;
        try {
            if(uri!=null) {
                is = getContentResolver().openInputStream(uri);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(is,null,bmoptions);
        //img = new BitmapFactory().decodeFile(s, bmoptions);
        holder.imageView.setImageBitmap(bm);
    }


    void cropImage(int pos) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(adapter.list_details.get(pos).image_uri,"image/*");
        p = pos;
        cropIntent.putExtra("crop","true");
        //cropIntent.putExtra("outputX",320);
        //cropIntent.putExtra("outputY",200);
        cropIntent.putExtra("aspectX",1);
        cropIntent.putExtra("aspectY",1);
        cropIntent.putExtra("return-data","true");
        startActivityForResult(cropIntent,1);

    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("stored_data1",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        n = paths.size();
        int i;
        for(i = 0;i < n; i++) {
            editor.putString("path"+i,paths.get(i));
            editor.putString("caption"+i,captions.get(i));
            editor.putInt("decider"+i,decider.get(i));
        }
        editor.putInt("number_of_items",n);
        editor.clear();
        editor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.image_find:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                String[] items1  = {"Capture an Image","Select from Gallery"};
                builder1.setTitle("Select an Image");
                builder1.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                camera_intent();
                                break;
                            case 1:
                                gallery_intent();
                                break;
                        }
                    }
                });
                builder1.show();
                return true;
            case R.id.app_details:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                String[] items2  = {"This App is basically designed to store wonderful memories in the form of Images."+"\n"+"There are some existing images in the app"+"\n"+"You can add your images from your gallery or camera into the app by clicking on the \"Add Image\" icon in the Action Bar."+"\n"+"You can Delete or Crop the images in the app on Long Clicking on the image that you want to apply the settings on."+"\n"+"\n"+"Thank You."+"\n"+"Designed by Rajat Bhatta(106116066)"};
                builder2.setTitle("WHAT DOES THE APP DO ???");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //askForPermissions();
                                camera_intent();
                                break;
                            case 1:
                                gallery_intent();
                                break;
                        }
                    }
                });
                builder2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int pos = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items  = {"Delete item","Crop"};
        builder.setTitle("Image Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        paths.remove(pos);
                        captions.remove(pos);
                        decider.remove(pos);
                        adapter.refresh(paths,captions,decider);
                        break;
                    case 1:
                        // CROPPING
                        cropImage(pos);
                }
            }
        });
        builder.show();
        return true;
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    void askForPermissions() {
//        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                showMessageOKCancel("You need to allow access to External Storage",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        CAMERA_INTENT_CODE);
//                            }
//                        });
//                return;
//            }
//            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    CAMERA_INTENT_CODE);
//            return;
//        }
//        camera_intent();
//    }
//
//    void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case CAMERA_INTENT_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    camera_intent();
//                } else {
//                    // Permission Denied
//                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

}
