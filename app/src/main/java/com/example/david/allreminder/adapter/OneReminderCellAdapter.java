package com.example.david.allreminder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.allreminder.util.Constant;
import com.example.david.allreminder.R;
import com.example.david.allreminder.model.Reminder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by David on 2015/7/25.
 */
public class OneReminderCellAdapter extends BaseAdapter {

    class ViewHolder{
        ImageView icon;
        TextView tvTitle;
        TextView tvDateTime;
        Button btnComplete;
    }

    private Context context=null;

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    private List<Reminder> reminders;

    public Context getContext() {
        return context;
    }

    public OneReminderCellAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Reminder getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return reminders.get(position)._id;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = new ViewHolder();
        LinearLayout ll=null;
        if (convertView == null){
            ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_one_reminder_cell, null);
        }else {
            ll = (LinearLayout) convertView;
        }
        viewHolder.icon = (ImageView) ll.findViewById(R.id.icon);
        viewHolder.tvTitle = (TextView) ll.findViewById(R.id.tvCellTitle);
        viewHolder.tvDateTime = (TextView) ll.findViewById(R.id.tvCellRemindTime);
//        viewHolder.btnComplete = (Button) ll.findViewById(R.id.btnComplete);

//        view.setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                final ViewHolder holder = (ViewHolder) v.getTag();
////当按下时处理
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
////设置背景为选中状态
//                    v.setBackgroundResource(R.drawable.mm_listitem_pressed);
////获取按下时的x轴坐标
//                    x = event.getX();
////判断之前是否出现了删除按钮如果存在就隐藏
//                    if (curDel_btn != null) {
//                        curDel_btn.setVisibility(View.GONE);
//                    }
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理
////设置背景为未选中正常状态
//                    v.setBackgroundResource(R.drawable.mm_listitem_simple);
////获取松开时的x坐标
//                    ux = event.getX();
////判断当前项中按钮控件不为空时
//                    if (holder.btnDel != null) {
////按下和松开绝对值差当大于20时显示删除按钮，否则不显示
//                        if (Math.abs(x - ux) > 20) {
//                            holder.btnDel.setVisibility(View.VISIBLE);
//                            curDel_btn = holder.btnDel;
//                        }
//                    }
//                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {//当滑动时背景为选中状态
//                    v.setBackgroundResource(R.drawable.mm_listitem_pressed);
//                } else {//其他模式
////设置背景为未选中正常状态
//                    v.setBackgroundResource(R.drawable.mm_listitem_simple);
//                }
//                return true;
//            }
//        });
//        ll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getTag();
//                return false;
//            }
//        });

        final Reminder reminder = getItem(position);
        Calendar calendarToday = Calendar.getInstance();

        if (reminder.getCalendar().get(Calendar.YEAR)==calendarToday.get(Calendar.YEAR)
                && reminder.getCalendar().get(Calendar.MONTH)==calendarToday.get(Calendar.MONTH)
                && reminder.getCalendar().get(Calendar.DAY_OF_MONTH)==calendarToday.get(Calendar.DAY_OF_MONTH)
        ){
            viewHolder.icon.setImageResource(R.drawable.iconfont_jin_black);
        }else {
            switch (reminder.type){
                case Constant.TYPE_SINGLE_REMIND:
                    viewHolder.icon.setImageResource(R.drawable.iconfont_shijian_black);
                    break;
                case Constant.TYPE_TODO_DAY:
                    viewHolder.icon.setImageResource(R.drawable.iconfont_date_black);
                    break;
                case Constant.TYPE_TODO_WEEK:
                    viewHolder.icon.setImageResource(R.drawable.iconfont_zhoujie_black);
                    break;
                case Constant.TYPE_TODO_ALL_TIME:
                    viewHolder.icon.setImageResource(R.drawable.iconfont_dengdai_black);
                    break;
            }
        }


        viewHolder.tvTitle.setText(reminder.title);
        viewHolder.tvDateTime.setText(reminder.getDateTimeStr());

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent("com.example.david.allreminder.intent.action.ReminderDetailDialogActivity");
                intentActivity.putExtra("_id",reminder.getId());
                getContext().startActivity(intentActivity);
            }
        });
//        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentActivity = new Intent("com.example.david.allreminder.intent.action.ReminderDetailDialogActivity");
//                intentActivity.putExtra("_id",reminder.getId());
//                getContext().startActivity(intentActivity);
//            }
//        });
//        viewHolder.btnComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.icon.setImageResource(R.drawable.application3);
//                reminder.status = Constant.STATUS_DONE;
//                reminder.save();
//                viewHolder.btnComplete.setText("已完成");
//                viewHolder.btnComplete.setClickable(false);
//            }
//        });
        return ll;
    }
}
