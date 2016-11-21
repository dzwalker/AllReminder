package com.example.david.allreminder.model;


import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by David on 2015/7/30.
 */
@Table(name = "CmdRecords",id = BaseColumns._ID)
public class CmdRecord extends Model {
    @Column
    public String cmd;
    @Column
    public Reminder reminder;

    public CmdRecord() {
    }

    public CmdRecord(String cmd) {
        this.cmd = cmd;
    }

    public static List<CmdRecord> getAll(){
        return new Select()
                .from(CmdRecord.class)
                .execute();
    }
}
