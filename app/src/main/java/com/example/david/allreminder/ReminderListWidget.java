package com.example.david.allreminder;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.allreminder.activity.CreateTodoActivity;
import com.example.david.allreminder.activity.ReminderDetailDialogActivity;
import com.example.david.allreminder.activity.VoiceRecognizeDialogActivity;
import com.example.david.allreminder.util.Constant;


/**
 * Implementation of App Widget functionality.
 */
public class ReminderListWidget extends AppWidgetProvider {
    public static final String ITEM_DETAIL= "com.example.david.allreminder.intent.action.ReminderDetailDialogActivity";

    public static final String UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reminder_list_widget);
//        views.setTextViewText(R.id.tvWidgetTitle, "这就是我要的标题！");

        Intent intentCreateTodo = new Intent(context, CreateTodoActivity.class);
        PendingIntent pendingIntentTodo = PendingIntent.getActivity(context, 0,
                intentCreateTodo, 0);
        views.setOnClickPendingIntent(R.id.btnCreateTodoWidget,pendingIntentTodo);

        Intent intentCreateByVoice = new Intent("com.example.david.allreminder.intent.action.VoiceRecognizeDialogActivity");
        intentCreateByVoice.putExtra("from",Constant.INTENT_FROM_HOME_TO_VOICE);
        PendingIntent pendingIntentByVoice = PendingIntent.getActivity(context, 0,
                intentCreateByVoice, 0);
        views.setOnClickPendingIntent(R.id.btnCreateByVoice,pendingIntentByVoice);

//        Intent refreshIntent = new Intent();
//        refreshIntent.setAction(Constant.UPDATE_ACTION);
//        refreshIntent.putExtra("widget_id",appWidgetId);
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,0,refreshIntent,0);
//        views.setOnClickPendingIntent(R.id.btnRefresh,pendingIntent2);

        // Instruct the widget manager to update the widget

        Intent intent = new Intent(context,ReminderWidgetService.class);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.listViewWidget, intent);

        Intent clickIntent = new Intent(context, ReminderDetailDialogActivity.class);
//        clickIntent.putExtra("_id",5);
        clickIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent clickPI = PendingIntent.getActivity(context, 0,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listViewWidget,clickPI);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        System.out.println(">>>>>>>>>><<<<<<<<<<<");
    }

    public void updateListView(){

    }


    @Override
    public void onReceive(Context context, Intent intent){
        System.out.println("onReceive");
//        int appWidgetId = intent.getIntExtra("widget_id",0);
//        updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
//        Bundle extras = intent.getExtras();
//        if (intent.getAction().equals(UPDATE_ACTION)){
//            int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//            this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//        }
//        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId,R.id.listViewWidget);

//        TODO:各种判断，预防错误。
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ReminderListWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,  R.id.listViewWidget);

        super.onReceive(context, intent);
    }
}

