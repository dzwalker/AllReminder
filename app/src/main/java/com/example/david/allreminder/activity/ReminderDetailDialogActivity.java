package com.example.david.allreminder.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.david.allreminder.model.ReminderLog;
import com.example.david.allreminder.util.Constant;
import com.example.david.allreminder.R;
import com.example.david.allreminder.model.Reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderDetailDialogActivity extends Activity implements View.OnClickListener {
    private ImageButton btnEditTitleAndDetail,btnTimeInfo, btnCloseDetailDialog;
    private TextView tvTitle,tvDetail;

    private Button btnComplete,btnStartAction, btnRearrange;

    private LinearLayout  llDetailAndAction, llStartDoing, llRearrange;

    private List<Button> btnListPostpone = new ArrayList<>();
    private Button btnP1,btnP2,btnP3,btnPMore;
    private EditText etPMinute;

    private RadioGroup rgChooseType;
    private RadioButton rbChooseTypeReminder,rbChooseTypeDay,rbChooseTypeWeek,rbChooseTypeAllTime;
    private TextView tvDate,tvTime;
    private LinearLayout  llSelectTime;

    private Button btnPlusDate,btnMinusDate,btnPlusTime,btnMinusTime;
    private Button btnSaveChange;
//    以上都是布局的控件

    long _id;
    private Reminder reminder;

/*   关于时间和日期的控制：
*    reminder是有日期和时间的，而日、周提醒是只有日期没有时间的，all time是完全没有日期和时间的。
*    进入到重新安排的功能里面时：
*    首先要有一个calendarOri，用来保持最原始的calendar，用于最后判断是提前了还是推后了。
*    对于reminder，直接把它的calendar给到整个界面中的calendar；
*    对于日和周，把日期传到calendar，时间用当时的时间。
*
*    每次修改了时间日期以后，都要把calendar给到reminder.setCalendar，再去个性显示的文字。
*
*       最后要保存的时候，再把日、周、all time里面多余的内容改掉。
*
* */
    private Calendar calendar, calendarOri;

//    private int actionIsArrange = 1;
//    private int actionIsPostpone = 2;
    private long[] postponeSecond = new long[]{900, 1500,3600};
    private int actionTypeChosen = 0;
    private int changeIsDate = 1;
    private int changeIsTime = 2;
    private int changeIsPlus = 1;
    private int changeIsMinus = -1;

//    初始化显示的内容和最开始的按钮。
    private void initBasicLayoutByReminderType(){
        tvTitle = (TextView) findViewById(R.id.tvReminderTitle);
        tvTitle.setText(reminder.title);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        if (reminder.detail==null){
            tvDetail.setText("还没有补充的信息");
        }else {
            tvDetail.setText(reminder.detail);
        }

        btnCloseDetailDialog = (ImageButton) findViewById(R.id.btnCloseDetailDialog);
        btnEditTitleAndDetail = (ImageButton) findViewById(R.id.btnEditTitleAndDetail);
        btnTimeInfo = (ImageButton) findViewById(R.id.btnTimeInfo);

        btnComplete = (Button) findViewById(R.id.btnComplete);
        btnStartAction = (Button) findViewById(R.id.btnStartAction);
        btnRearrange = (Button) findViewById(R.id.btnRearrange);


        btnCloseDetailDialog.setOnClickListener(this);
        btnEditTitleAndDetail.setOnClickListener(this);
        btnTimeInfo.setOnClickListener(this);

        btnComplete.setOnClickListener(this);
        btnStartAction.setOnClickListener(this);
        btnRearrange.setOnClickListener(this);






        llDetailAndAction = (LinearLayout) findViewById(R.id.llDetailAndAction);
        llStartDoing = (LinearLayout) findViewById(R.id.llStartDoing);
        llRearrange = (LinearLayout) findViewById(R.id.llRearrange);
        llStartDoing.setVisibility(View.GONE);
        llRearrange.setVisibility(View.GONE);


    }

//    当点击开干以后，显示的界面。
    private void initStartActionLayout(){
        btnP1 = (Button) findViewById(R.id.btnP1);
        btnListPostpone.add(btnP1);
        btnP2 = (Button) findViewById(R.id.btnP2);
        btnListPostpone.add(btnP2);
        btnP3 = (Button) findViewById(R.id.btnP3);
        btnListPostpone.add(btnP3);
        for (int i = 0; i < btnListPostpone.size(); i++) {
            btnListPostpone.get(i).setOnClickListener(this);
        }

        etPMinute = (EditText) findViewById(R.id.etPMinute);


        btnPMore = (Button) findViewById(R.id.btnPMore);
        btnPMore.setOnClickListener(this);
        llDetailAndAction.animate().alpha(0.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llDetailAndAction.setVisibility(View.GONE);

                llStartDoing.setVisibility(View.VISIBLE);
                llStartDoing.setAlpha(0.0f);
                llStartDoing.animate().alpha(1.0f).setDuration(300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

//    点击安排或者推迟以后，加载一堆的界面。
    private void initRearrangeLayout(){
        rgChooseType = (RadioGroup) findViewById(R.id.rgChooseType);
        rgChooseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRgChooseTypeChange();
            }
        });
        rbChooseTypeReminder = (RadioButton) findViewById(R.id.rbChooseTypeReminder);
        rbChooseTypeDay = (RadioButton) findViewById(R.id.rbChooseTypeDay);
        rbChooseTypeWeek = (RadioButton) findViewById(R.id.rbChooseTypeWeek);
        rbChooseTypeAllTime = (RadioButton) findViewById(R.id.rbChooseTypeAllTime);

        llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
        btnPlusDate = (Button) findViewById(R.id.btnPlusDayDetail);
        btnMinusDate = (Button) findViewById(R.id.btnMinusDayDetail);
        btnPlusTime = (Button) findViewById(R.id.btnPlusTimeDetail);
        btnMinusTime = (Button) findViewById(R.id.btnMinusTimeDetail);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);

        btnSaveChange = (Button) findViewById(R.id.btnSaveChange);

        btnPlusDate.setOnClickListener(this);
        btnMinusDate.setOnClickListener(this);
        btnPlusTime.setOnClickListener(this);
        btnMinusTime.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        btnSaveChange.setOnClickListener(this);

        switch (reminder.type){
            case Constant.TYPE_SINGLE_REMIND:
                rbChooseTypeReminder.setChecked(true);
                break;
            case Constant.TYPE_TODO_DAY:
                rbChooseTypeDay.setChecked(true);
                break;
            case Constant.TYPE_TODO_WEEK:
                rbChooseTypeWeek.setChecked(true);
                break;
            case Constant.TYPE_TODO_ALL_TIME:
                rbChooseTypeAllTime.setChecked(true);
                break;
        }
        llDetailAndAction.animate().alpha(0.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llDetailAndAction.setVisibility(View.GONE);

                llRearrange.setVisibility(View.VISIBLE);
                llRearrange.setAlpha(0.0f);
                llRearrange.animate().alpha(1.0f).setDuration(300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void initCalendar() {
        switch (reminder.type) {
            case Constant.TYPE_SINGLE_REMIND:
                calendar = reminder.getCalendar();
                break;
            case Constant.TYPE_TODO_DAY:
                calendar = Calendar.getInstance();

                calendar.set(reminder.getCalendar().get(Calendar.YEAR), reminder.getCalendar().get(Calendar.MONTH),
                        reminder.getCalendar().get(Calendar.DAY_OF_MONTH));
                break;
            case Constant.TYPE_TODO_WEEK:
                calendar = Calendar.getInstance();

                calendar.set(reminder.getCalendar().get(Calendar.YEAR), reminder.getCalendar().get(Calendar.MONTH),
                        reminder.getCalendar().get(Calendar.DAY_OF_MONTH));
                break;
            case Constant.TYPE_TODO_ALL_TIME:
                calendar = Calendar.getInstance();
                break;
        }
        reminder.setCalendar(calendar);
    }

    private void updateDateTimeText(){
        tvDate.setText(reminder.getDateStr());
        tvTime.setText(reminder.getTimeStr());
    }

//    单选按钮按下时，改日期，显示和隐藏时间
    private void onRgChooseTypeChange(){
        switch (rgChooseType.getCheckedRadioButtonId()){
            case R.id.rbChooseTypeReminder:
                reminder.type = Constant.TYPE_SINGLE_REMIND;
                updateDateTimeText();
                llSelectTime.setVisibility(View.VISIBLE);
                break;
            case R.id.rbChooseTypeDay:
                reminder.type = Constant.TYPE_TODO_DAY;
                updateDateTimeText();
                llSelectTime.setVisibility(View.GONE);
                break;
            case R.id.rbChooseTypeWeek:
                reminder.type = Constant.TYPE_TODO_WEEK;
                if (reminder.getCalendar().get(Calendar.DAY_OF_WEEK)!=1){
                    calendar = reminder.getCalendar();
                    calendar.set(Calendar.DAY_OF_WEEK, 1);
                    calendar.add(Calendar.DATE, 7);
                    reminder.setCalendar(calendar);
                }
                updateDateTimeText();
                llSelectTime.setVisibility(View.GONE);
                break;
            case R.id.rbChooseTypeAllTime:
                reminder.type = Constant.TYPE_TODO_ALL_TIME;
                tvDate.setText("未设定");
                llSelectTime.setVisibility(View.GONE);
                break;
        }
    }

    private void changeDateTime(int dateOrTime, int plusOrMinus){

        int dayPerClick;
        int minutePerClick = 30;
        if (reminder.type == Constant.TYPE_TODO_WEEK){
            dayPerClick = 7;
        } else {
            dayPerClick = 1;
        }
        if (dateOrTime == changeIsDate){
            calendar.add(Calendar.DATE, dayPerClick*plusOrMinus);
        } else if (dateOrTime == changeIsTime){
            calendar.add(Calendar.MINUTE, minutePerClick*plusOrMinus);
        }
        reminder.setCalendar(calendar);
        updateDateTimeText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail_dialog);
        Bundle data = getIntent().getExtras();
        _id = data.getLong("_id");
        reminder = Reminder.getById(_id);
        calendarOri = reminder.getCalendar();
        initBasicLayoutByReminderType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCloseDetailDialog:
                finish();
                break;
            case R.id.btnEditTitleAndDetail:
//                TODO：编辑标题和详情的功能还没做。
                break;
            case R.id.btnTimeInfo:
//                TODO：Toast一下时间相关的信息，还没做。
                break;
            case R.id.btnComplete:
                reminder.status = Constant.STATUS_DONE;
                reminder.saveReminder();
                finish();
                break;
            case R.id.btnStartAction:
                initStartActionLayout();
                break;
            case R.id.btnRearrange:
                initRearrangeLayout();
                initCalendar();
                updateDateTimeText();
                break;

            case R.id.btnP1 :
                startActionAndRemindMeAfter(0);
                finish();
                break;
            case R.id.btnP2 :
                startActionAndRemindMeAfter(1);
                finish();
                break;
            case R.id.btnP3 :
                startActionAndRemindMeAfter(2);
                finish();
                break;
            case R.id.btnPMore:
                int pMinute=0;
                if (etPMinute.getText()!=null){
                    pMinute = Integer.parseInt(etPMinute.getText().toString());
                }
                if (pMinute >0){
                    reminder.setRemindTime(Calendar.getInstance().getTimeInMillis() / 1000 + 60*pMinute);
                    reminder.type = Constant.TYPE_SINGLE_REMIND;
                    reminder.saveReminder();
                    finish();
                }

                break;

            case R.id.btnMinusDayDetail:
                changeDateTime(changeIsDate,changeIsMinus);
                break;
            case R.id.btnPlusDayDetail:
                changeDateTime(changeIsDate,changeIsPlus);
                break;
            case R.id.btnMinusTimeDetail:
                changeDateTime(changeIsTime,changeIsMinus);
                break;
            case R.id.btnPlusTimeDetail:
                changeDateTime(changeIsTime,changeIsPlus);
                break;
            case R.id.tvDate:
                new DatePickerDialog(ReminderDetailDialogActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar = reminder.getCalendar();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        reminder.setCalendar(calendar);
                        updateDateTimeText();
                    }
                },reminder.getCalendar().get(Calendar.YEAR),reminder.getCalendar().get(Calendar.MONTH),reminder.getCalendar().get(Calendar.DATE)).show();

                break;
            case R.id.tvTime:
                new TimePickerDialog(ReminderDetailDialogActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar = reminder.getCalendar();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        reminder.setCalendar(calendar);
                        updateDateTimeText();
                    }
                },reminder.getCalendar().get(Calendar.HOUR_OF_DAY),reminder.getCalendar().get(Calendar.MINUTE),true).show();
                break;
            case R.id.btnSaveChange:
                if (reminder.type == Constant.TYPE_TODO_DAY || reminder.type == Constant.TYPE_TODO_WEEK ||
                        reminder.type == Constant.TYPE_TODO_ALL_TIME){
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 0);
                }
                if (reminder.type == Constant.TYPE_TODO_ALL_TIME){
                    calendar.set(3000, 1, 1);
                }

                if (calendar.after(calendarOri)){
                    reminder.postponeCount += 1;
                    new ReminderLog(Constant.LOG_TYPE_POSTPONE, reminder, calendarOri.getTime().toString()+"->"+calendar.getTime().toString());
                } else {
                    new ReminderLog(Constant.LOG_TYPE_ARRANGE, reminder, calendarOri.getTime().toString()+"->"+calendar.getTime().toString());
                }
                reminder.setCalendar(calendar);
                reminder.saveReminder();
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder_detail_dialog, menu);
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

    private void startActionAndRemindMeAfter(int index){
        reminder.setRemindTime(Calendar.getInstance().getTimeInMillis()/1000+ postponeSecond[index]);
        reminder.type = Constant.TYPE_SINGLE_REMIND;
        reminder.saveReminder();
        new ReminderLog(Constant.LOG_TYPE_START, reminder, "预计用时："+postponeSecond[index]+"秒");
    }


    @Override
    public void onDestroy(){
        Constant.restartReminderService(this);
        Constant.refreshWidget(this);
        super.onDestroy();
    }
}
