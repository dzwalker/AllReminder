package com.example.david.allreminder.util;

import android.content.Context;
import android.content.Intent;

import com.example.david.allreminder.ReminderService;

/**
 * Created by David on 2015/8/12.
 */
public class Constant {
    public static final String UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final int INTENT_FROM_HOME_TO_VOICE = 1;
    public static final int INTENT_FROM_MAIN_TO_VOICE = 2;
    public static final int INTENT_FROM_MAIN_TO_CREATE_WITHOUT_CMD = 3;
    public static final int INTENT_FROM_VOICE_TO_CREATE_WITH_CMD = 4;
    public static final int FROM_CREATE = 3;
    public static final int TYPE_SINGLE_REMIND = 1;
    public static final int TYPE_TODO_DAY = 2;
    public static final int TYPE_TODO_WEEK = 3;
    public static final int TYPE_TODO_ALL_TIME = 4;
    public static final int PLACE_HOME = 1;
    public static final int PLACE_WORK = 2;
    public static final int STATUS_NEW = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_POSTPONED = 2;
    public static final int LIST_TYPE_REMINDER = 1;
    public static final int LIST_TYPE_TODO_TODAY = 2;
    public static final int LIST_TYPE_TODO_THIS_WEEK = 3;
    public static final int LIST_TYPE_TODO_ALL_TIME = 4;
    public static final int LIST_TYPE_TODO_FUNCTION = 5;
    public static final int LOG_TYPE_CREATE = 1;
    public static final int LOG_TYPE_POSTPONE = 2;
    public static final int LOG_TYPE_COMPLETE = 3;
    public static final int LOG_TYPE_ARRANGE = 4;
    public static final int LOG_TYPE_START = 5;
    public static final String CMD_SEPARATOR = "";

    public static final void restartReminderService(Context context){
        Intent intentService = new Intent(context, ReminderService.class);
        context.stopService(intentService);
        context.startService(intentService);
    }

    public static final void refreshWidget(Context context){
        Intent intentRefreshWidget = new Intent(UPDATE_ACTION);
        context.sendBroadcast(intentRefreshWidget);
    }


//    public static final Db db = ;

//    public static final String  INTENT_FROM = "from";



}
