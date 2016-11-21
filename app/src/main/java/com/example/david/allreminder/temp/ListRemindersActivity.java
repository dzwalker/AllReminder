package com.example.david.allreminder.temp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import com.example.david.allreminder.R;


public class ListRemindersActivity extends ListActivity {

    private Db db;
    private SQLiteDatabase dbRead;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reminders);
        db = new Db(this);
//        Reminder []reminders = new Reminder[3];
//        new Reminder(db, "没什么要做的1", System.currentTimeMillis(), "洗衣服","不需要什么具体的细节了","home").add();
//        new Reminder(db, "没什么要做的2", System.currentTimeMillis(), "洗衣服","不需要什么具体的细节了","home").add();
//        new Reminder(db, "没什么要做的3", System.currentTimeMillis(), "洗衣服","不需要什么具体的细节了","home").add();
//        reminders[1] = new Reminder(db,new Date(),"还是得做些事情的","不需要什么具体的细节了", "home", "洗衣服");
//        reminders[2] = new Reminder(db, "还是得做些事情的","不需要什么具体的细节了", new Date(),"home", "洗衣服");
//        setListAdapter(new OneReminderCellAdapter(this,reminders));
        dbRead = db.getReadableDatabase();
        adapter = new SimpleCursorAdapter(this, R.layout.view_one_reminder_cell,null,
                new String[]{"title","remindTime"},
                new int[]{R.id.tvCellTitle, R.id.tvCellRemindTime}, 0);
        setListAdapter(adapter);
        refreshListView();
//        while (c.moveToNext()){
//            String title = c.getString(c.getColumnIndex("title"));
//            long remindTime = c.getLong(c.getColumnIndex("remindTime"));
//            System.out.println(String.format("标题：%s  时间：%d", title, remindTime));
//        }

    }

    private void refreshListView(){
        Cursor c = dbRead.query("reminder",null,null,null,null,null,null);
        adapter.changeCursor(c);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
