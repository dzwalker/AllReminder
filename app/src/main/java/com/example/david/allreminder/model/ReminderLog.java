package com.example.david.allreminder.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

/**
 * Created by David on 2015/8/31.
 */

@Table(name = "reminderLogs",id = BaseColumns._ID)
public class ReminderLog extends Model {
    @Column
    public long logTime;

    @Column
    public int logType;

    @Column
    public Reminder reminderId;

    @Column
    public String detail;

    public ReminderLog() {
    }

    public ReminderLog(int logType, Reminder reminderId, String detail) {
        this.logType = logType;
        this.reminderId = reminderId;
        this.detail = detail;
        this.logTime = Calendar.getInstance().getTimeInMillis()/1000;
        this.save();
    }

    public static List<ReminderLog> getAll(){
        return new Select()
                .from(ReminderLog.class)
                .orderBy("logTime DESC")
                .execute();
    }
}
