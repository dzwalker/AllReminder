package com.example.david.allreminder.util;

import com.example.david.allreminder.model.Reminder;
import com.example.david.allreminder.util.Constant;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 2015/7/29.
 *
 * 把文字命令输入进来，然后自动填写相应的内容到reminder中，然后返回reminder。
 *
 */
public class CmdProcessor {


    static public Reminder process(String cmd, Reminder reminder) {

        Pattern pattern = null;
        Matcher matcher = null;
        String patternString = "";
        Calendar calendar = reminder.getCalendar();


        if (reminder !=null) {
            patternString = "提醒我";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                cmd = matcher.replaceFirst("");
            }
        }

        if (reminder !=null) {
            patternString = "。$";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                cmd = matcher.replaceFirst("");
            }
        }
        //计算开始
        if (reminder !=null){
            patternString = "明天";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()){
                calendar.add(Calendar.DATE, 1);
                cmd = matcher.replaceFirst("");
            }
        }

        if (reminder !=null){
            patternString = "后天";

            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()){
                calendar.add(Calendar.DATE, 2);
                cmd = matcher.replaceFirst("");

            }
        }

        if (reminder !=null){
            patternString = "(下周|下个星期)";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()){
                calendar.add(Calendar.DATE, 7);
                cmd = matcher.replaceFirst("星期");


            }
        }

        if (reminder != null) {
            patternString = "(星期|周)([一二三四五六天])";

            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                int day = -1;
                switch (matcher.group(2)){
                    case "一":
                        day = 2;
                    case "二":
                        day = 3;
                    case "三":
                        day = 4;
                    case "四":
                        day = 5;
                    case "五":
                        day = 6;
                    case "六":
                        day = 7;
                    case "日":
                        day = 1;
                    case "天":
                        day = 1;
                }
                calendar.set(Calendar.DAY_OF_WEEK, day);

                cmd = matcher.replaceFirst("");


            }
        }

        if (reminder != null) {
            patternString = "(\\d{1,2})\\s?(点|:)\\s?(\\d{1,2}|半|钟|整)?";

            pattern = Pattern.compile(patternString);
            String half = "半";
            String zhong = "钟";
            String zheng = "整";
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                System.out.println(matcher.group());
                int hour = Integer.parseInt(matcher.group(1));
                int minute = 0;
                if (matcher.group(3)!=null){
                    if (matcher.group(3).equals(half)){
                        minute = 30;
                    }else if(matcher.group(3).equals(zhong) ||matcher.group(3).equals(zheng)){
                        minute = 0;
                    }else {
                        minute = Integer.parseInt(matcher.group(3));
                    }
                }

                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                cmd = matcher.replaceFirst("");


            }
        }

        if (reminder != null) {
            patternString = "(上午|早上)";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()){
                calendar.set(Calendar.AM_PM, Calendar.AM);
                cmd = matcher.replaceFirst("");


            }
        }
        if (reminder != null) {
            patternString = "(下午|晚上|今晚)";
            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()){
                calendar.set(Calendar.AM_PM, Calendar.PM);
                cmd = matcher.replaceFirst("");


            }
        }


        if (reminder != null) {
            patternString = "到家";

            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                reminder.place=Constant.PLACE_HOME;
                cmd = matcher.replaceFirst("");


            }
        }
        if (reminder != null) {
            patternString = "到公司";

            pattern = Pattern.compile(patternString);
            matcher = pattern.matcher(cmd);
            if (matcher.find()) {
                reminder.place=Constant.PLACE_WORK;
                cmd = matcher.replaceFirst("");


            }
        }

        reminder.title=cmd;
        reminder.setCalendar(calendar);

        return reminder;
    }
}
