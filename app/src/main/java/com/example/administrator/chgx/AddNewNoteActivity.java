package com.example.administrator.chgx;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddNewNoteActivity extends AppCompatActivity {
private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        dbHelper=new MyDatabaseHelper(this,"Note.db",null,1);
        dbHelper.getWritableDatabase();
        Button addData =(Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db= dbHelper.getWritableDatabase();
                ContentValues values =new ContentValues();
                values.put("name","DbAdd1");
                db.insert("Note",null,values);
                values.clear();
            }
        });
    }
}
