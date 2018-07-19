package com.example.administrator.chgx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private List<Fruit> noteList = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    public LocationClient mLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);

        //baidu 定位
        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        //dbHelper
        dbHelper = new MyDatabaseHelper(this, "Note.db", null, 1);

        //RecyclerView
        initNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycer_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(noteList);
        recyclerView.setAdapter(adapter);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(R.mipmap.logo2);
    }


    //baidu 定位
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(10000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0) {
                        for (int result : grantResults) {
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(this, "请同意定位权限！", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                        }
                        requestLocation();
                    } else {
                        Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                default:
            }
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度:").append(location.getLatitude()).append("\n");
            currentPosition.append("经线:").append(location.getLongitude()).append("\n");
            currentPosition.append("国家:").append(location.getCountry()).append("\n");
            currentPosition.append("省:").append(location.getProvince()).append("\n");
            currentPosition.append("市:").append(location.getCity()).append("\n");
            currentPosition.append("区:").append(location.getDistrict()).append("\n");
            currentPosition.append("街道:").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            positionText.setText(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    //menu item
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.addlist:
                Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }


    //改变图片亮度
    private void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }


    //List初始化
    private void initNotes() {
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(apple);
            Fruit banana = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(banana);
            Fruit orange = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(orange);
            Fruit watermelon = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(watermelon);
            Fruit pear = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(pear);
            Fruit grape = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(grape);
            Fruit pineapple = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(pineapple);
            Fruit strawberry = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(strawberry);
            Fruit cherry = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(cherry);
            Fruit mango = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1, "CHGX · 10:48");
            noteList.add(mango);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Note", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("MainActivity", "name=" + name);
                Fruit note1 = new Fruit(name, R.mipmap.mon1, "CHGX · 10:48");
                noteList.add(note1);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
