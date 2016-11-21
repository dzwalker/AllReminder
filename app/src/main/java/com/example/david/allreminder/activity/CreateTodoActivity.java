package com.example.david.allreminder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.allreminder.R;
import com.example.david.allreminder.model.Reminder;
import com.example.david.allreminder.model.ReminderLog;
import com.example.david.allreminder.util.Constant;

import java.util.Calendar;

public class CreateTodoActivity extends Activity implements View.OnClickListener {
    private RadioButton rbTodoTimeTypeChecked;
    private RadioGroup rgTodoTimeType;
    private Button btnCreateTodo,btnMinus,btnPlus;
    private ImageButton btnClose;
    private TextView tvDate;
    private EditText etTodoTitle;
    private Reminder reminder;
    private Calendar calendar=null;

    private void initLayout(){
        rgTodoTimeType = (RadioGroup) findViewById(R.id.rgTodoTimeType);
        btnCreateTodo = (Button) findViewById(R.id.btnCreateTodo);
        btnClose = (ImageButton) findViewById(R.id.btnCloseCreateTodo);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        tvDate = (TextView) findViewById(R.id.tvTodoDate);
        etTodoTitle = (EditText) findViewById(R.id.etTodoTitle);

        btnCreateTodo.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        rgTodoTimeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRgTodoTimeTypeChanged();
            }
        });

        etTodoTitle.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etTodoTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    createTodo();
                }
                return false;
            }
        });

    }

    private void updateDateText(){
        tvDate.setText(reminder.getDateTimeStr());
    }

    private void onRgTodoTimeTypeChanged(){
        switch (rgTodoTimeType.getCheckedRadioButtonId()){
            case R.id.rbTodoDay:
                reminder.type = Constant.TYPE_TODO_DAY;
//                        reminder.calendar = Calendar.getInstance();
                updateDateText();
                break;
            case R.id.rbTodoWeek:
                reminder.type = Constant.TYPE_TODO_WEEK;
                if (reminder.getCalendar().get(Calendar.DAY_OF_WEEK)!=1){
                    calendar = reminder.getCalendar();
                    calendar.set(Calendar.DAY_OF_WEEK, 1);
                    calendar.add(Calendar.DATE,7);
                    reminder.setCalendar(calendar);
//                            reminder.calendar.add(Calendar.DATE,7);
                }

                updateDateText();
                break;
            case R.id.rbTodoAllTime:
                reminder.type = Constant.TYPE_TODO_ALL_TIME;
                tvDate.setText("未设定");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        initLayout();
        initReminder();
        onRgTodoTimeTypeChanged();
        updateDateText();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initReminder();
        onRgTodoTimeTypeChanged();
        updateDateText();
    }

    private void initReminder(){
        calendar = Reminder.getDayEndCalendar();
        reminder = new Reminder(calendar);
        reminder.type = Constant.TYPE_TODO_DAY;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_todo, menu);
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

    private void createTodo(){
        reminder.title = etTodoTitle.getText().toString();
//                TODO:验证
        if (reminder.type==Constant.TYPE_TODO_ALL_TIME){
            calendar.set(3000,1,1);
            reminder.setCalendar(calendar);
        }
        reminder.saveReminder();
        new ReminderLog(Constant.LOG_TYPE_CREATE, reminder, reminder.title);

        Toast.makeText(CreateTodoActivity.this, "创建TODO："+reminder.title, Toast.LENGTH_SHORT).show();
        etTodoTitle.setText("");
        initReminder();
        onRgTodoTimeTypeChanged();
        updateDateText();
    }

    @Override
    public void onDestroy (){
        System.out.println("onDestroy");
        Constant.refreshWidget(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateTodo:
                createTodo();
                break;

            case R.id.btnCloseCreateTodo:
                finish();
                break;
            case R.id.tvTodoDate:
                if (reminder.type == Constant.TYPE_TODO_DAY){
                    new DatePickerDialog(CreateTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar = reminder.getCalendar();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            reminder.setCalendar(calendar);
                            updateDateText();
                        }
                    },reminder.getCalendar().get(Calendar.YEAR),reminder.getCalendar().get(Calendar.MONTH),reminder.getCalendar().get(Calendar.DATE)).show();
                }
                break;
            case R.id.btnMinus:
                calendar = reminder.getCalendar();
                switch (reminder.type){
                    case Constant.TYPE_TODO_DAY:
                        calendar.add(Calendar.DATE, -1);
                        reminder.setCalendar(calendar);
                        break;
                    case Constant.TYPE_TODO_WEEK:
                        calendar.add(Calendar.DATE,-7);
                        reminder.setCalendar(calendar);
                        break;
                }
                updateDateText();
                break;

            case R.id.btnPlus:
                calendar = reminder.getCalendar();
                switch (reminder.type){
                    case Constant.TYPE_TODO_DAY:
                        calendar.add(Calendar.DATE,1);
                        reminder.setCalendar(calendar);
                        break;
                    case Constant.TYPE_TODO_WEEK:
                        calendar.add(Calendar.DATE,7);
                        reminder.setCalendar(calendar);
                        break;
                }
                updateDateText();
                break;
        }
    }
}
