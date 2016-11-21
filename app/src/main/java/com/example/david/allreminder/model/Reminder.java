package com.example.david.allreminder.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.david.allreminder.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by David on 2015/7/24.
 * 2015/8/16用AA重写
 */

@Table(name = "Reminders",id = BaseColumns._ID)
public class Reminder extends Model{

    //    public long _id;
    @Column
    private long remindTime=0;

    @Column
    public int place=0;
    @Column
    public int type=0;
    @Column
    public String title="";
    @Column
    public int status = Constant.STATUS_NEW;
    @Column
    public String detail;
    @Column
    public int postponeCount;
    @Column
    public long createdAt = 0;
    @Column
    public long lastModifyAt;

    private Calendar calendar=null;

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(this.remindTime * 1000);
    }

    public Reminder() {

    }

    public Long saveReminder(){

        if (this.createdAt == 0){
            this.createdAt = Calendar.getInstance().getTimeInMillis()/1000;
        }
        this.lastModifyAt = Calendar.getInstance().getTimeInMillis()/1000;
        return super.save();
    }

    public Reminder(int place, int type, String title, int status, String detail, Calendar calendar) {
        this.remindTime = calendar.getTimeInMillis()/1000;
        this.place = place;
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.calendar = calendar;
    }

    public Reminder(Calendar calendar) {
        this.setCalendar(calendar);

    }

    public long getRemindTime() {
        return remindTime;
    }

    public Calendar getCalendar() {
        if (calendar==null){
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(this.remindTime * 1000);
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        this.remindTime = this.calendar.getTimeInMillis()/1000;
    }

    static public Calendar getDayEndCalendar(){
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.set(Calendar.HOUR_OF_DAY,23);
        calendarTemp.set(Calendar.MINUTE,59);
        calendarTemp.set(Calendar.SECOND,59);
        calendarTemp.set(Calendar.MILLISECOND,0);
        return  calendarTemp;
    }

    public String getDateFormat(){
        String dateStrFormat = "";
//        定义了一堆日期结束的时间点calendar
        Calendar calendarYesterday = getDayEndCalendar();
        calendarYesterday.add(Calendar.DATE,-1);

        Calendar calendarEndOfToday = getDayEndCalendar();

        Calendar calendarTomorrow = getDayEndCalendar();
        calendarTomorrow.add(Calendar.DATE,1);

        Calendar calendarAfterTomorrow = getDayEndCalendar();
        calendarAfterTomorrow.add(Calendar.DATE,2);

        Calendar calendarThisWeekend = getDayEndCalendar();
        calendarThisWeekend.add(Calendar.DATE,7);
        calendarThisWeekend.set(Calendar.DAY_OF_WEEK,1);

        Calendar calendarNextWeekend = getDayEndCalendar();
        calendarNextWeekend.add(Calendar.DATE,14);
        calendarNextWeekend.set(Calendar.DAY_OF_WEEK, 1);
        String[] daysOfWeek = new String[]{"", "日","一","二","三","四","五","六"};

        if (!calendar.after(calendarYesterday)){
            dateStrFormat = "MM月dd日";
        }else if (!calendar.after(calendarEndOfToday)){
            dateStrFormat = "今天";
        }else if (!calendar.after(calendarTomorrow)){
            dateStrFormat = "明天";
        }else if (!calendar.after(calendarAfterTomorrow)){
            dateStrFormat = "后天";
        }else if (!calendar.after(calendarThisWeekend)){
            dateStrFormat = "星期"+daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];
        }else if (!calendar.after(calendarNextWeekend)){
            dateStrFormat = "下周"+daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];
        }else {
            dateStrFormat = "MM月dd日";
        }
        return dateStrFormat;
    }

    public String getDateStr(){
        if (this.calendar==null){
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.remindTime * 1000);
        }
        String dateStrFormat = getDateFormat();

        SimpleDateFormat sdm=new SimpleDateFormat(dateStrFormat);
        return sdm.format(this.calendar.getTime());
    }
     public String getTimeStr(){
        if (this.calendar==null){
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.remindTime * 1000);
        }

        SimpleDateFormat sdm=new SimpleDateFormat("HH:mm");
        return sdm.format(this.calendar.getTime());
    }


    public String getDateTimeStr(){
        if (this.calendar==null){
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.remindTime * 1000);
        }

        SimpleDateFormat sdm=null;

        String dateStr = getDateFormat();

        if (this.type==0){
            sdm = new SimpleDateFormat(dateStr+" HH:mm");
        }
        switch (this.type){
            case Constant.TYPE_SINGLE_REMIND:
                sdm = new SimpleDateFormat(dateStr+ " HH:mm");
                break;
            case Constant.TYPE_TODO_DAY:
                sdm = new SimpleDateFormat(dateStr);
                break;
            case Constant.TYPE_TODO_WEEK:
                sdm = new SimpleDateFormat(dateStr);
                break;
            case Constant.TYPE_TODO_ALL_TIME:
                return "";

        }
        return sdm.format(this.calendar.getTime());
    }

    public static Reminder getLatestReminder(){
        return new Select()
                .from(Reminder.class)
                .where("status = ? AND remindTime > ? AND type = ?",Constant.STATUS_NEW, Calendar.getInstance().getTimeInMillis()/1000, Constant.TYPE_SINGLE_REMIND)
                .orderBy("remindTime ASC")
                .executeSingle();
    }

    public static Reminder getById(long _id){
        return new Select()
                .from(Reminder.class)
                .where("_id = ?", _id)
                .executeSingle();
    }

    public static List<Reminder> getRemindersThisWeek(){
        Calendar nextWeekCalendar = Calendar.getInstance();
        nextWeekCalendar.set(Calendar.HOUR_OF_DAY,0);
        nextWeekCalendar.set(Calendar.MINUTE,0);
        nextWeekCalendar.set(Calendar.SECOND,0);
        nextWeekCalendar.set(Calendar.DAY_OF_WEEK,0);
        nextWeekCalendar.add(Calendar.DATE,7);
        return new Select()
                .from(Reminder.class)
                .where("status = ? AND remindTime < ?", Constant.STATUS_NEW, nextWeekCalendar.getTimeInMillis() / 1000)
                .orderBy("remindTime ASC")
                .execute();
    }

    public static List<Reminder> getRemindersToday(){
        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.set(Calendar.HOUR_OF_DAY,0);
        tomorrowCalendar.set(Calendar.MINUTE,0);
        tomorrowCalendar.set(Calendar.SECOND,0);
        tomorrowCalendar.add(Calendar.DATE,1);
        return new Select()
                .from(Reminder.class)
                .where("status = ? AND remindTime < ?", Constant.STATUS_NEW,tomorrowCalendar.getTimeInMillis()/1000)
                .orderBy("remindTime ASC")
                .execute();
    }

    public static List<Reminder> getAll(){
        return new Select()
                .from(Reminder.class)
                .where("status = ?",Constant.STATUS_NEW)
                .orderBy("remindTime ASC")
                .execute();
    }

    public static  List<Reminder> getAfterThisWeek(){
        Calendar calendarNextMonday = Calendar.getInstance();
        calendarNextMonday.set(Calendar.HOUR_OF_DAY, 0);
        calendarNextMonday.set(Calendar.MINUTE, 0);
        calendarNextMonday.set(Calendar.SECOND,0);
        calendarNextMonday.set(Calendar.DAY_OF_WEEK, 2);
        if (calendarNextMonday.get(Calendar.DAY_OF_WEEK)!=1){
            calendarNextMonday.add(Calendar.DATE,7);
        }
        return new Select()
                .from(Reminder.class)
                .where("status = ? AND remindTime > ?", Constant.STATUS_NEW,calendarNextMonday.getTimeInMillis()/1000)
                .orderBy("remindTime ASC")
                .execute();
    }
}
