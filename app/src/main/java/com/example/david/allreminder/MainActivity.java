package com.example.david.allreminder;

/**
 *   刷新service里面的提醒器的方法：
 *   关闭再重新打开service就可以了。

 *   TODO：已完成的reminder记录。
 *   TODO：把notification改为自定义的样式。
 *   TODO：CmdRecord的显示。
 *   TODO：提醒加声音。
 *   TODO：各种列表显示优化。
 *   TODO：起来的时候，到公司的时候，到家以后……有一个默认的时间，也有一个根据wifi的提醒器。

 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.david.allreminder.activity.CmdRecordListActivity;
import com.example.david.allreminder.activity.CreateReminderActivity;
import com.example.david.allreminder.activity.CreateTodoActivity;
import com.example.david.allreminder.activity.VoiceRecognizeDialogActivity;
import com.example.david.allreminder.adapter.MyViewPagerAdapter;
import com.example.david.allreminder.adapter.OneReminderCellAdapter;
import com.example.david.allreminder.model.Reminder;
import com.example.david.allreminder.model.ReminderLog;
import com.example.david.allreminder.util.AutoUpdate;
import com.example.david.allreminder.util.Constant;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection {
    private Button btnStartService, btnCloseService, btnBindService, btnUnbindService, btnGetCurrentNum;
    private ImageButton btnStartActCreateReminder,btnTestVoice, btnSettings, btnCmdRecord, btnTest, btnCreateTodo;
    private Intent intentActivity, intentService;
    private ReminderService reminderService=null;
    private OneReminderCellAdapter cellAdapter;
    private List<Reminder> reminders;
    private ListView listView;
    private Context context;
    private int currentViewpager = 0;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private ViewPager viewPager;
    private TextView tvTab1, tvTab2, tvTab3;
    private List<View> viewPagerViews;
    private View viewPagerView1, viewPagerView2, viewPagerView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        AutoUpdate.updateDB();
        setContentView(R.layout.activity_main);
        setContext(this);
        initLayout();
        initViewPager();
        intentService = new Intent(this, ReminderService.class);
//        处理ListView里面的内容。

//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=55b72c58");
        startService(intentService);
//      处理ListView内容结束

//        refreshList(Constant.LIST_TYPE_REMINDER);
    }

    private void initLayout(){

        btnStartActCreateReminder = (ImageButton) findViewById(R.id.btnStartActCreateReminder);
        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnCloseService = (Button) findViewById(R.id.btnCloseService);
        btnBindService = (Button) findViewById(R.id.btnBindService);
        btnUnbindService = (Button) findViewById(R.id.btnUnbindService);
        btnGetCurrentNum = (Button) findViewById(R.id.btnGetCurrentNum);
        btnTestVoice = (ImageButton) findViewById(R.id.btnTestVoice);
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnCmdRecord = (ImageButton) findViewById(R.id.btnCmdRecord);
        btnTest = (ImageButton) findViewById(R.id.btnTest);
        btnCreateTodo = (ImageButton) findViewById(R.id.btnCreateTodo);
//        listView = (ListView) findViewById(R.id.reminderList);

        btnStartActCreateReminder.setOnClickListener(this);
        btnStartService.setOnClickListener(this);
        btnCloseService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        btnGetCurrentNum.setOnClickListener(this);
        btnTestVoice.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnCmdRecord.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnCreateTodo.setOnClickListener(this);


        tvTab1 = (TextView) findViewById(R.id.tvTabToday);
        tvTab2 = (TextView) findViewById(R.id.tvTabThisWeek);
        tvTab3 = (TextView) findViewById(R.id.tvTabAfterThisWeek);
        tvTab1.setOnClickListener(new MyOnClickListener(0));
        tvTab2.setOnClickListener(new MyOnClickListener(1));
        tvTab3.setOnClickListener(new MyOnClickListener(2));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateTodo:
                intentActivity = new Intent(this, CreateTodoActivity.class);
                intentActivity.putExtra("from",Constant.INTENT_FROM_MAIN_TO_CREATE_WITHOUT_CMD);
                startActivity(intentActivity);
                break;
            case R.id.btnStartActCreateReminder:
                intentActivity = new Intent(this, CreateReminderActivity.class);
                intentActivity.putExtra("from",Constant.INTENT_FROM_MAIN_TO_CREATE_WITHOUT_CMD);
                startActivity(intentActivity);
                break;
            case R.id.btnSettings:
                intentActivity = new Intent(this, SettingsActivity.class);
                startActivity(intentActivity);
                break;
            case R.id.btnCmdRecord:
                intentActivity = new Intent(this, CmdRecordListActivity.class);
                startActivity(intentActivity);
                break;
            case R.id.btnStartService:
                startService(intentService);
                break;
            case R.id.btnCloseService:
                stopService(intentService);
                break;
            case R.id.btnBindService:
                bindService(intentService, this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                unbindService(this);
                reminderService = null;
                break;
            case R.id.btnGetCurrentNum:
                if (reminderService!=null){
//                    System.out.println("service中当前数字："+reminderService.getCurrentNum());
                    reminderService.refreshReminder();
                }
                break;
            case R.id.btnTestVoice:
                intentActivity = new Intent(this, VoiceRecognizeDialogActivity.class);
                intentActivity.putExtra("from", Constant.INTENT_FROM_MAIN_TO_VOICE);
                startActivityForResult(intentActivity, 1);

                break;
            case R.id.btnTest:
//                intentActivity = new Intent(this, TestActivity.class);
//                startActivity(intentActivity);
//                addSC();
                List<ReminderLog> logs = ReminderLog.getAll();
                for (int i = 0; i < logs.size(); i++) {
                    ReminderLog log = logs.get(i);
                    System.out.println(log.detail);
                }
                break;
        }
    }

    private void initViewPager(){

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerViews = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        viewPagerView1 = inflater.inflate(R.layout.view_pager_reminder_list,null);
        viewPagerView2 = inflater.inflate(R.layout.view_pager_reminder_list, null);
        viewPagerView3 = inflater.inflate(R.layout.view_pager_reminder_list,null);
        viewPagerViews.add(viewPagerView1);
        viewPagerViews.add(viewPagerView2);
        viewPagerViews.add(viewPagerView3);

        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerViews));
        viewPager.setCurrentItem(0);
        loadListView(currentViewpager);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    }



    public class MyOnClickListener implements View.OnClickListener {

        private int index=0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//            int two = one * 2;// 页卡1 -> 页卡3 偏移量
//
//            Animation animation = new TranslateAnimation(one*currentIndex, one*position, 0, 0);//显然这个比较简洁，只有一行代码。
//            currentIndex = position;
//            animation.setFillAfter(true);// True:图片停在动画结束位置
//            animation.setDuration(300);
//            imageView.startAnimation(animation);
            currentViewpager = position;
            loadListView(currentViewpager);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void loadListView(int tabIndex){
        tvTab1.setTextColor(getResources().getColor(R.color.tab_normal));
        tvTab2.setTextColor(getResources().getColor(R.color.tab_normal));
        tvTab3.setTextColor(getResources().getColor(R.color.tab_normal));
        switch (tabIndex){
            case 0:
                System.out.println("view1");
                listView = (ListView) viewPagerView1.findViewById(R.id.listViewVP);
                reminders = Reminder.getRemindersToday();
                tvTab1.setTextColor(getResources().getColor(R.color.tab_selected));

                break;
            case 1:
                System.out.println("view2");
                listView = (ListView) viewPagerView2.findViewById(R.id.listViewVP);
                reminders = Reminder.getRemindersThisWeek();
                tvTab2.setTextColor(getResources().getColor(R.color.tab_selected));

                break;
            case 2:
                System.out.println("view3");
                listView = (ListView) viewPagerView3.findViewById(R.id.listViewVP);
                reminders = Reminder.getAfterThisWeek();
                tvTab3.setTextColor(getResources().getColor(R.color.tab_selected));

                break;
        }
        cellAdapter = new OneReminderCellAdapter(MainActivity.this,reminders);
        listView.setAdapter(cellAdapter);
    }

    public void addSC(){
        Intent addShortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//        Intent launcherIntent = new Intent(MainActivity.this, VoiceRecognizeDialogActivity.class);
        Intent launcherIntent = new Intent("com.example.david.allreminder.intent.action.VoiceRecognizeDialogActivity");
        launcherIntent.putExtra("from",Constant.INTENT_FROM_HOME_TO_VOICE);
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "添加Reminder");
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(
                MainActivity.this, R.drawable.application1));
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        sendBroadcast(addShortCut);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        System.out.println("onServiceConnected");
        reminderService = ((ReminderService.ReminderServiceBinder) binder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        System.out.println("服务关闭连接");
    }

    public void refreshReminder() {
//        bindService(intentService, this, Context.BIND_AUTO_CREATE);
//        if (reminderService!=null){
//            reminderService.refreshReminder();
//        }
//
//        unbindService(this);
        stopService(intentService);
        startService(intentService);
    }

//    public void refreshList(int listType){
//        switch (listType){
//            case Constant.LIST_TYPE_REMINDER:
//                reminders = Reminder.getAll();
//                cellAdapter = new OneReminderCellAdapter(this,reminders);
//                listView.setAdapter(cellAdapter);
//
//                break;
//        }
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        loadListView(currentViewpager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        if (resultCode==1){
            String words = data.getStringExtra("words");
            intentActivity = new Intent(this, CreateReminderActivity.class);
            intentActivity.putExtra("from",Constant.INTENT_FROM_VOICE_TO_CREATE_WITH_CMD);
            intentActivity.putExtra("words",words);
            startActivityForResult(intentActivity,1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
