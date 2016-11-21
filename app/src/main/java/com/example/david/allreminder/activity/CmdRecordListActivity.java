package com.example.david.allreminder.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import com.example.david.allreminder.R;
import com.example.david.allreminder.model.CmdRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CmdRecordListActivity extends ListActivity {
//    private SimpleCursorAdapter adapter;
    private List<CmdRecord> cmdRecords;
    private List<Map<String ,String >> data;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cmdRecords = CmdRecord.getAll();


        for (int i = 0; i < cmdRecords.size(); i++) {
            CmdRecord cmdRecord = cmdRecords.get(i);
            Map<String ,String > recordDate= new HashMap<String ,String >();
            recordDate.put("cmd",cmdRecord.cmd);
//            data.add(recordDate);
        }
        adapter = new SimpleAdapter(this,data, R.layout.view_one_cmd_cell,
                new  String []{"cmd"}, new int[]{R.id.tvCellCmdText});

//        adapter = new SimpleAdapter(this,R.layout.view_one_cmd_cell,null,
//                new String[]{"cmd","created_at"},
//                new int[]{R.id.tvCellCmdText,R.id.tvCellCmdDate},0);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cmd_record_list, menu);
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
