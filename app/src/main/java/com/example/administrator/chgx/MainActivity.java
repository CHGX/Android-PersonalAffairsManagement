package com.example.administrator.chgx;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private List<Fruit> noteList = new ArrayList<>();
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this,"Note.db",null,1);
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

     /* //透明状态栏 透明导航栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/
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

    private void initNotes() {
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(apple);
            Fruit banana = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(banana);
            Fruit orange = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(orange);
            Fruit watermelon = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(watermelon);
            Fruit pear = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(pear);
            Fruit grape = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(grape);
            Fruit pineapple = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(pineapple);
            Fruit strawberry = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(strawberry);
            Fruit cherry = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(cherry);
            Fruit mango = new Fruit("Prabhdeep listened to this.", R.mipmap.mon1);
            noteList.add(mango);
        }
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Note",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String name =cursor.getString(cursor.getColumnIndex("name"));
                Log.d("MainActivity","name="+name);
                Fruit note1 = new Fruit(name, R.mipmap.mon1);
                noteList.add(note1);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
