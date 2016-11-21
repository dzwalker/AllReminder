package com.example.david.allreminder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.allreminder.R;
import com.example.david.allreminder.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TestActivity extends Activity implements View.OnClickListener {
    private Button btnTest01;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private ViewPager viewPager;
    private ImageView imageView;
    private TextView tv1,tv2,tv3;
    private List<View> views;
    private int offset = 0;
    private int currentIndex = 0;
    private int bmpW;
    private View view1,view2,view3;

    private void initViewPager(){

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.view_pager_reminder_list,null);
        view2 = inflater.inflate(R.layout.view_pager_reminder_list,null);
        view3 = inflater.inflate(R.layout.view_pager_reminder_list,null);

        views.add(view1);
        views.add(view2);
        views.add(view3);

        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    }


    private void InitImageView() {
//        imageView= (ImageView) findViewById(R.id.cursor);
//        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }


    private void initTextView(){
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv1.setOnClickListener(new MyOnClickListener(0));
        tv2.setOnClickListener(new MyOnClickListener(1));
        tv3.setOnClickListener(new MyOnClickListener(2));

    }

    public class MyOnClickListener implements View.OnClickListener {

        private int index=0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            System.out.println("你点了"+index);
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
            System.out.println("onPageSelected");
            Toast.makeText(TestActivity.this, "您选择了"+ viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTest01 = (Button) findViewById(R.id.btnTest01);
        btnTest01.setOnClickListener(this);
        initTextView();
        initViewPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

    public void showMessage(String msg){
        Toast.makeText(TestActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTest01:
//                Date date1=new Date();
//                Calendar calendar = Calendar.getInstance();
////                calendar.set(Calendar.MONTH,1);
//
//                System.out.println("===============");
////                showMessage(date1.toString());
//                System.out.println(calendar);
//                System.out.println("XXXXXXXXXXXXXXX");
//                calendar.add(Calendar.DATE, 200);
//                System.out.println(calendar);
//                System.out.println("---------------");
                break;
        }
    }
}
