package com.example.david.allreminder.temp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 2015/7/27.
 */
public class Db extends SQLiteOpenHelper {
    public Db(Context context) {
        super(context, "db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(">>>>>>>>>>>>>>onCreate");
        db.execSQL("CREATE TABLE reminder(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT DEFAULT \"\"," +
                "detail TEXT DEFAULT \"\"," +
                "type INTEGER DEFAULT 0," +
                "place INTEGER DEFAULT 0," +
                "status INTEGER DEFAULT 0," + //状态：0新建，1完成，
                "postponeCount INTEGER DEFAULT 0 ," +
                "remindTime INTEGER " + //存的时候是按秒来存的
                ")");

        db.execSQL("CREATE TABLE cmd_record(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cmd TEXT DEFAULT \"\"," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "reminder_id INTEGER DEFAULT 0" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
