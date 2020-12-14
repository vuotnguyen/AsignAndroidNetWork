package com.example.asignandroidnetwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asignandroidnetwork.model.Example;
import com.example.asignandroidnetwork.model.Photo;
import com.example.asignandroidnetwork.model.Photos;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements CallBack {

    private RecyclerView recyclerView;
    private List<Photo> list;
    private List<String> listString;

    private String link;
    private static final String USER_ID = "191213941@N03";
    private static final String TOKEN = "2eb9ea3b4537fe49ca1d6e0eb1cdfd42";
    private static final String LIST_FAVORITE = "flickr.favorites.getList";
    private static final String PULL_EXTRAS = "views%2C+media%2C+path_alias%2C+url_sq";
    private PictureAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();
        initView();
    }

    private void initView() {
        link = "https://www.flickr.com/services/rest/?method=flickr.favorites.getList&api_key=2eb9ea3b4537fe49ca1d6e0eb1cdfd42&user_id=191213941%40N03&extras=views%2C+media%2C+path_alias%2C+url_sq&per_page=30&page=1&format=json&nojsoncallback=1";


        list = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_listLikeImage);
//        RecyclerView.LayoutManager staggered = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);


        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        readForRetrofit();
//        readForVolley();
    }

    private void readForRetrofit() {

        MyRetrofit.getInStance()
                .getFavorite()
                .enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                        Example example = response.body();
                        Photos photos = example.getPhotos();
                        list = photos.getPhoto();
//                        adapter = new PictureAdapter(MainActivity.this,list);
//                        recyclerView.setAdapter(adapter);
                        Log.i("TAG", "onResponse: "+list.size());
                        getData(response);
                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {

                    }
                });





    }

    private void getData(retrofit2.Response<Example> response) {
        Example example = response.body();
        Photos photos = example.getPhotos();
        list = photos.getPhoto();
        adapter = new PictureAdapter(MainActivity.this,list,this);
        recyclerView.setAdapter(adapter);
        Log.i("TAG", "onResponse: "+list.size());
    }

    private void readForVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Example example = (Example) response.get("photos");
                    Photos photos = example.getPhotos();
                    list = photos.getPhoto();
                    Log.i("TAG", "onResponse: "+list.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(objectRequest);
    }

    @Override
    public void showDialog(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Save");
        dialog.setMessage("Bạn muốn lưu vào thiết bị chứ ?");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // lưu đi
                saveImage(url);
            }
        });
        dialog.setPositiveButton("Thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dissmiss

            }
        });
        dialog.show();
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
    private void saveImage(String url) {
        String path = Environment.getExternalStorageDirectory().getPath()+"/CodeFresherApp/Image"+url;
        File file = new File(path);
        Toast.makeText(MainActivity.this,"Lưu thành công",Toast.LENGTH_SHORT).show();
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            
            FileOutputStream out = new FileOutputStream(file);
            myBitmap.compress(Bitmap.CompressFormat.PNG,100,out);

            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}