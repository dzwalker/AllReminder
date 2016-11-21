package com.example.david.allreminder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.david.allreminder.model.ReminderLog;
import com.example.david.allreminder.util.CmdProcessor;
import com.example.david.allreminder.util.Constant;
import com.example.david.allreminder.R;
import com.example.david.allreminder.ReminderService;
import com.example.david.allreminder.model.CmdRecord;
import com.example.david.allreminder.model.Reminder;

import java.util.Calendar;


public class CreateReminderActivity extends Activity implements View.OnClickListener {
    private TextView tvPickDate,tvPickTime,tvConversation;
    private Context context;
    private Button btnAddReminder,btnMinusDay,btnPlusDay,btnMinus30m,btnPlus30m;
    private ImageButton btnAddInfoByVoice;
    private EditText etTitle, etDetail;
    private RadioGroup rgPlace;
    private RadioButton rbPlaceChecked;
    private Calendar calendar=Calendar.getInstance();
    private Reminder reminder=new Reminder(calendar);
    private String cmd = "";
    private CmdRecord cmdRecord;
    int from=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initLayout();
        updateDateTime();
        Bundle data = getIntent().getExtras();
        if (data!=null){
            from = data.getInt("from");
            switch (from){
                case Constant.INTENT_FROM_HOME_TO_VOICE:
                    cmd = data.getString("words");
                    updateData();
                    break;
                case Constant.INTENT_FROM_VOICE_TO_CREATE_WITH_CMD:
                    cmd = data.getString("words");
                    updateData();
                    break;
                case Constant.INTENT_FROM_MAIN_TO_CREATE_WITHOUT_CMD:
                    updateData();
                    break;
            }

        }

    }

    private void initLayout(){
        setContentView(R.layout.activity_create_reminder);

        tvPickDate = (TextView) findViewById(R.id.tvPickDate);
        tvPickTime = (TextView) findViewById(R.id.tvPickTime);
        tvConversation = (TextView) findViewById(R.id.tvCoversation);
        btnAddReminder = (Button) findViewById(R.id.btnAddReminder);
        btnAddInfoByVoice = (ImageButton) findViewById(R.id.btnAddInfoByVoice);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDetail = (EditText) findViewById(R.id.etDetail);
        rgPlace = (RadioGroup) findViewById(R.id.rgPlace);

        btnMinusDay = (Button) findViewById(R.id.btnMinusDay);
        btnMinus30m = (Button) findViewById(R.id.btnMinus30m);
        btnPlusDay = (Button) findViewById(R.id.btnPlusDay);
        btnPlus30m = (Button) findViewById(R.id.btnPlus30m);

        tvPickDate.setOnClickListener(this);
        tvPickTime.setOnClickListener(this);
        btnAddReminder.setOnClickListener(this);
        btnAddInfoByVoice.setOnClickListener(this);

        btnMinusDay.setOnClickListener(this);
        btnMinus30m.setOnClickListener(this);
        btnPlusDay.setOnClickListener(this);
        btnPlus30m.setOnClickListener(this);

    }

    private void updateData(){

        if (!cmd.equals("")){
            reminder = CmdProcessor.process(cmd, reminder);
            tvConversation.setText(cmd);
        }
        cmdRecord = new CmdRecord(cmd);
        cmdRecord.save();

        if (reminder!=null){
            etTitle.setText(reminder.title);
            etDetail.setText(reminder.getCalendar().getTime().toString());

            if (reminder.getCalendar()!=null){
                updateDateTime();
            }

            if (reminder.place!=0){
                switch (reminder.place){
                    case Constant.PLACE_HOME:
                        rbPlaceChecked = (RadioButton) findViewById(R.id.rbPlaceHome);
                        break;
                    case Constant.PLACE_WORK:
                        rbPlaceChecked = (RadioButton) findViewById(R.id.rbPlaceWork);
                        break;
                }
                rbPlaceChecked.setChecked(true);
            }


        }

    }


    @Override
    protected void onDestroy() {
        Constant.refreshWidget(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_reminder, menu);
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

    public String formatDateDigit(int d){
        return (d>9)? ""+d : "0"+d;
    }

    private void updateDateTime(){
        tvPickDate.setText(reminder.getDateStr());
        tvPickTime.setText(reminder.getTimeStr());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddReminder:
                rbPlaceChecked = (RadioButton) findViewById(rgPlace.getCheckedRadioButtonId());
                String title = etTitle.getText().toString();
                String detail = etDetail.getText().toString();
                if (title.equals("")){
                    Toast.makeText(CreateReminderActivity.this,"主题是必须要写滴",Toast.LENGTH_LONG).show();
                }else{
                    reminder.title=title;
                    reminder.detail=detail;
                    reminder.type = Constant.TYPE_SINGLE_REMIND;
                    switch (rbPlaceChecked.getId()){
                        case R.id.rbPlaceHome:
                            reminder.place = Constant.PLACE_HOME;
                            break;
                        case R.id.rbPlaceWork:
                            reminder.place = Constant.PLACE_WORK;
                            break;
                    }
                    reminder.saveReminder();
                    Toast.makeText(CreateReminderActivity.this, "创建提醒："+reminder.title, Toast.LENGTH_SHORT).show();

                    new ReminderLog(Constant.LOG_TYPE_CREATE, reminder, reminder.title);

                    if (from==Constant.INTENT_FROM_VOICE_TO_CREATE_WITH_CMD){
                        cmdRecord.reminder = reminder;
                        cmdRecord.save();
                    }

                    Constant.restartReminderService(context);
                    CreateReminderActivity.this.finish();
                }
                break;
            case R.id.tvPickDate:
                new DatePickerDialog(CreateReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar = reminder.getCalendar();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        reminder.setCalendar(calendar);
                        updateDateTime();
                    }
                },reminder.getCalendar().get(Calendar.YEAR),reminder.getCalendar().get(Calendar.MONTH),reminder.getCalendar().get(Calendar.DATE)).show();
                break;
            case R.id.tvPickTime:
                new TimePickerDialog(CreateReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar = reminder.getCalendar();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        reminder.setCalendar(calendar);
                        updateDateTime();
                    }
                },reminder.getCalendar().get(Calendar.HOUR_OF_DAY),reminder.getCalendar().get(Calendar.MINUTE),true).show();
                break;
            case R.id.btnAddInfoByVoice:
                Intent intentActivity = new Intent(this, VoiceRecognizeDialogActivity.class);
                intentActivity.putExtra("from",Constant.FROM_CREATE);
                startActivityForResult(intentActivity, 1);
                break;

            case R.id.btnMinusDay:
                calendar.add(Calendar.DATE,-1);
                reminder.setCalendar(calendar);
                updateDateTime();
                break;

            case R.id.btnMinus30m:
                calendar.add(Calendar.MINUTE,-30);
                reminder.setCalendar(calendar);
                updateDateTime();
                break;

            case R.id.btnPlusDay:
                calendar.add(Calendar.DATE,1);
                reminder.setCalendar(calendar);
                updateDateTime();
                break;

            case R.id.btnPlus30m:
                calendar.add(Calendar.MINUTE,30);
                reminder.setCalendar(calendar);
                updateDateTime();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle returnData = data.getExtras();
        cmd = returnData.getString("words");
        updateData();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
