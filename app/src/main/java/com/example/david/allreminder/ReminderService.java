package com.example.david.allreminder;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.allreminder.activity.ReminderDetailDialogActivity;
import com.example.david.allreminder.model.Reminder;
import com.example.david.allreminder.util.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class ReminderService extends Service {
    private static final int NOTIFICATION_FLAG = 1;

    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private NetworkInfo info;

    private Timer timer = null;
    private TimerTask timerTask = null;
    private Context context;
    private Reminder reminder=null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();

                if (info!=null && info.isAvailable()){
                    String name = info.getTypeName();
                    System.out.println("当前网络："+ name);
                }else{
                    System.out.println("没有网络");
                }
            }
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifiInfo = wifiManager.getConnectionInfo();
                System.out.println(wifiInfo.getSSID());
            }

        }
    };

    public void refreshReminder(){
        reminder = Reminder.getLatestReminder();
        if (reminder!=null){
            if (timer!=null){
                timer.cancel();
            }
            if (timerTask!=null){
                timerTask.cancel();
            }

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
            timer.schedule(timerTask, reminder.getCalendar().getTime());
            Toast.makeText(ReminderService.this, "下次提醒："+ reminder.title + " - " + reminder.getDateTimeStr(), Toast.LENGTH_SHORT).show();
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            System.out.println("来到handler里面了");
            if (msg.what ==1){
//                showDialogLatestReminder();
//                showNotification();
                showLatestReminderNotification();
                showLatestReminderDialog();
            }
            super.handleMessage(msg);
        }
    };


    private int i = 0;

    public ReminderService() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {

        return context;
    }

    public class ReminderServiceBinder extends Binder {
        public ReminderService getService() {
            return ReminderService.this;
        }
    }


    private final ReminderServiceBinder createReminderBinder = new ReminderServiceBinder();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        System.out.println("来到服务的内部了");
//        throw new UnsupportedOperationException("Not yet implemented");
        refreshReminder();
        return createReminderBinder;
    }




    private void showLatestReminderNotification(){
        reminder = Reminder.getLatestReminder();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentTarget = new Intent(this, ReminderDetailDialogActivity.class);
        intentTarget.putExtra("_id",reminder.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intentTarget, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.application2)
                .setTicker("该干活了：" + reminder.title)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentTitle(reminder.title)
                .setContentText(reminder.getDateTimeStr())
                .setContentIntent(pendingIntent).setNumber(1).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_FLAG, notification);
    }

    private void showNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(getContext());
//        builder.setSmallIcon(R.drawable.application1)
//                .setTicker("这是ticker")
//                .setContentTitle("这是标题")
//                .setAutoCancel(true);
//        Notification notification  = builder.build();
        PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify3 = new Notification.Builder(this)
                .setSmallIcon(R.drawable.application2)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                .setContentTitle("Notification Title")
                .setContentText("This is the notification message")
                .setContentIntent(pendingIntent3).setNumber(1).build(); // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        mNotificationManager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
//        自定义的通知：
//        Notification myNotify = new Notification();
//        myNotify.icon = R.drawable.message;
//        myNotify.tickerText = "TickerText:您有新短消息，请注意查收！";
//        myNotify.when = System.currentTimeMillis();
//        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
//        RemoteViews rv = new RemoteViews(getPackageName(),
//                R.layout.my_notification);
//        rv.setTextViewText(R.id.text_content, "hello wrold!");
//        myNotify.contentView = rv;
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
//                intent, 1);
//        myNotify.contentIntent = contentIntent;
//        manager.notify(NOTIFICATION_FLAG, myNotify);

    }


    private void showLatestReminderDialog(){
        reminder = Reminder.getLatestReminder();

        Intent i = new Intent("com.example.david.allreminder.intent.action.ReminderDetailDialogActivity");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("_id", reminder.getId());
        startActivity(i);
    }

    private void showDialog() {

        reminder = Reminder.getLatestReminder();
        System.out.println("<>><<><><><><><><><><><><><>");
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//        dialog.setTitle("提示");
//        dialog.setIcon(R.drawable.application3);
//        dialog.setMessage("你好啊！");
//        dialog.setPositiveButton("收到", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        final AlertDialog mDialog = dialog.create();
//        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.activity_reminder_detail_dialog);
        TextView tvTitle = (TextView) window.findViewById(R.id.tvReminderTitle);
        tvTitle.setText(reminder.title);
        window.findViewById(R.id.btnComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminder.status = Constant.STATUS_DONE;
                reminder.saveReminder();
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate() {
        System.out.println("启动service");
        setContext(this);
        refreshReminder();
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        System.out.println("关闭service");
        if (timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
        if (timer!=null){
            timer.cancel();
            timer = null;
        }

        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}
