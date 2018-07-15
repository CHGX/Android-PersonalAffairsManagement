package com.example.administrator.chgx;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class AddNewNoteActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.AddNewNoteToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(R.mipmap.logo2);

        //创建数据库
        dbHelper = new MyDatabaseHelper(this, "Note.db", null, 1);
        dbHelper.getWritableDatabase();

        //增加数据
        Button addData = (Button) findViewById(R.id.add_data);
        final EditText inputText = (EditText) findViewById(R.id.inputText);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", inputText.getText().toString());
                db.insert("Note", null, values);
                values.clear();
            }
        });

        //设定时间
        Button setDate1 = (Button) findViewById(R.id.setDate1);
        Button setDate2 = (Button) findViewById(R.id.setDate2);
        setDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView time1 = (TextView) findViewById(R.id.time1);
                showDatePickerDialog(AddNewNoteActivity.this, 2, time1, calendar);
            }
        });
        setDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView time2 = (TextView) findViewById(R.id.time2);
                showTimePickerDialog(AddNewNoteActivity.this, 2, time2, calendar);
            }
        });
    }


   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    //TimePicker
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        new TimePickerDialog(activity, themeResId,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText("您选择了：" + hourOfDay + "时" + minute + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }
}
