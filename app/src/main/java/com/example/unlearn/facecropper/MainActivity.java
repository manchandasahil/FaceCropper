package com.example.unlearn.facecropper;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button pick_dir;
    private TextView path;
    private ArrayList<String> folders = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pick_dir = (Button) findViewById(R.id.dir_select);
        path = (TextView) findViewById(R.id.dir);
        pick_dir.setOnClickListener(this);
        AssetManager am = this.getAssets();
        Log.d("Sahil","info  " + getApplicationInfo().dataDir);
        listAssetFiles("");
        for(String u:folders){
            Log.d("Sahil", u);
            AssetManager mgr = getAssets();
            try {
                String list[] = mgr.list(u);
                for(String i:list){
                    extractFacesFromImage efi = new extractFacesFromImage(u+"/" +i, "/storage/emulated/0/"+u,this);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void getFromAssets (AssetManager mgr, String path) {
        try {
            String list[] = mgr.list(path);
            if (list != null)
                for (int i=0; i<list.length; ++i)
                {
                    File file = new File(list[i]);
                    if (file.isDirectory()) {
                        Log.d("Sahil:",list[i]+ "is Directory");
                    }
                    else {
                        Log.d("Sahil:",list[i]+ "is File");
                    }

                }
        } catch (IOException e) {
            Log.v("List error:", "can't list" + path);
        }

    }

    @Override
    public void onClick(View v) {
        /*Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 9999);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*switch(requestCode) {
            case 9999:
                Log.i("Test", "Result URI " + data.getData());
                path.setText(data.getData().getPath());
                try {
                    traverse(new File(new URI(data.getData().getPath())));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }*/
        String Fpath = data.getDataString();
        path.setText(Fpath);
        try {
            File file = new File(new URI(Fpath));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean listAssetFiles(String path) {

        String [] list;
        try {
            list = getAssets().list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (!listAssetFiles(path + "/" + file))
                        return false;
                    else {
                        folders.add(file);
                        // TODO: add file name to an array list
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
