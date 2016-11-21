package com.example.david.allreminder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.allreminder.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 2015/7/30.
 */
public class OneReminderCellCursorAdapter extends CursorAdapter {

    class ViewHolder{
        ImageView icon;
        TextView tvTitle;
        TextView tvDateTime;
    }



    private Context context=null;
    int resourceId;

    public Context getContext() {
        return context;
    }

    public OneReminderCellCursorAdapter(Context context, int resourceId, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        LinearLayout ll=null;


//        ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_one_reminder_cell,null);

        ViewHolder viewHolder = new ViewHolder();

        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        li.from(getContext()).inflate(R.layout.view_one_reminder_cell,null);
        View view = li.inflate(resourceId,parent,false);
        viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
        viewHolder.tvTitle  = (TextView) view.findViewById(R.id.tvCellTitle);
        viewHolder.tvDateTime = (TextView) view.findViewById(R.id.tvCellRemindTime);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        final String title = cursor.getString(cursor.getColumnIndex("title"));
        String remindTime = "时间未确定";
        long remindTimeInt = cursor.getInt(cursor.getColumnIndex("remindTime"));
        if (remindTimeInt>0){
            Date dateTime = new Date(remindTimeInt*1000);
            SimpleDateFormat sdm=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            remindTime = sdm.format(dateTime);
        }
        viewHolder.icon.setImageResource(R.drawable.application1);
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDateTime.setText(remindTime);


        viewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "当前选中：" + title, Toast.LENGTH_LONG).show();
            }
        });

        final String finalRemindTime = remindTime;
        viewHolder.tvDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"remindTime"+ finalRemindTime, Toast.LENGTH_LONG).show();
            }
        });
    }
}
