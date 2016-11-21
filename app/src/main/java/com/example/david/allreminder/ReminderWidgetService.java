package com.example.david.allreminder;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.david.allreminder.model.Reminder;
import com.example.david.allreminder.util.Constant;

import java.util.Calendar;
import java.util.List;

public class ReminderWidgetService extends RemoteViewsService {

    public ReminderWidgetService() {
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(getApplicationContext(),intent);
    }

    public class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory{
        private Context context;
        private List<Reminder> reminders;

        public MyWidgetFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            System.out.println("onDataSetChanged");
            reminders=Reminder.getAll();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return reminders.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.view_one_reminder_cell_simple);
            Reminder reminder = reminders.get(position);
            Calendar calendarNow = Calendar.getInstance();

//            System.out.println(calendarNow.getTimeInMillis()/1000 + ":::" + reminder.getCalendar().getTimeInMillis()/1000);
//            System.out.println(reminder.title+reminder.getRemindTime());
            Boolean isDue = calendarNow.after(reminder.getCalendar());
            Boolean isToday = reminder.getCalendar().get(Calendar.YEAR)==calendarNow.get(Calendar.YEAR)
                    && reminder.getCalendar().get(Calendar.MONTH)==calendarNow.get(Calendar.MONTH)
                    && reminder.getCalendar().get(Calendar.DAY_OF_MONTH)==calendarNow.get(Calendar.DAY_OF_MONTH);



            switch (reminder.type){
                case Constant.TYPE_SINGLE_REMIND:
                    if (isToday){
                        views.setImageViewResource(R.id.imgCellSimple, R.drawable.iconfont_shijian_orange);

                    }else {
                        views.setImageViewResource(R.id.imgCellSimple, R.drawable.iconfont_shijian_white);
                    }


                    break;
                case Constant.TYPE_TODO_DAY:
                    if (isToday){
                        views.setImageViewResource(R.id.imgCellSimple, R.drawable.iconfont_jin_orange2);
                    }else {
                        views.setImageViewResource(R.id.imgCellSimple,R.drawable.iconfont_date_white);

                    }

                    break;
                case Constant.TYPE_TODO_WEEK:
                    if (isToday){
                        views.setImageViewResource(R.id.imgCellSimple, R.drawable.iconfont_jin_orange2);
                    }else {
                        views.setImageViewResource(R.id.imgCellSimple, R.drawable.iconfont_zhoujie_white);
                    }
                    break;
                case Constant.TYPE_TODO_ALL_TIME:
                    views.setImageViewResource(R.id.imgCellSimple,R.drawable.iconfont_dengdai_white);
                    break;
            }

            views.setTextViewText(R.id.tvCellSimpleTitle,reminder.title);
            views.setTextViewText(R.id.tvCellSimpleTime, reminder.getDateTimeStr());
            if (isDue){
                views.setTextColor(R.id.tvCellSimpleTime,getResources().getColor(R.color.due_time_text));
            }else {
                views.setTextColor(R.id.tvCellSimpleTime,getResources().getColor(R.color.normal_time_text));

            }

            Intent intentTarget = new Intent();
            intentTarget.putExtra("_id", reminder.getId());
            views.setOnClickFillInIntent(R.id.ll_cell_simple, intentTarget);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
