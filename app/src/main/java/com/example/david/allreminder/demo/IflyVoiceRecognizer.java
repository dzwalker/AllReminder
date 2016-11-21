package com.example.david.allreminder.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Created by David on 2015/7/29.
 */
public class IflyVoiceRecognizer {
    private SpeechRecognizer mIat;
    private Context context;
    private JSONTokener jsonTokener;
    private String allResultWords="";
    private Intent intentTargetActivity=null;


    public Intent getIntentTargetActivity() {
        return intentTargetActivity;
    }

    public void setIntentTargetActivity(Intent intentTargetActivity) {
        this.intentTargetActivity = intentTargetActivity;
    }

    public String getAllResultWords() {
        return allResultWords;
    }

    public Context getContext() {
        return context;

    }


    public IflyVoiceRecognizer(Context context) {
        this.context = context;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=55b72c58");
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
    }
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            allResultWords="";
            System.out.println( "初始化code:" + i );
        }
    };

    public void startVoiceRecognition(){
        setParm();
        int ret = mIat.startListening(recognizerListener);
        System.out.println("开始听：" + ret);
    }


    public void stopListening(){
        mIat.stopListening();
    }


    public void setParm(){
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT,"mandarin");
    }

    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            System.out.println("on BeginOfSpeech");

        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("onEndOfSpeech");

            if (getIntentTargetActivity()!=null){
                intentTargetActivity.putExtra("words",allResultWords);
                getContext().startActivity(intentTargetActivity);
            }
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            jsonTokener = new JSONTokener(recognizerResult.getResultString());
            JSONObject resultJSON=null;
            String resultWords = "";
            try {
                resultJSON = (JSONObject) jsonTokener.nextValue();
                JSONArray JSONws=  resultJSON.getJSONArray("ws");
                for (int i = 0; i < JSONws.length(); i++) {
                    JSONObject JSONOneWord = JSONws.getJSONObject(i);
                    JSONArray JSONOneWordArray = JSONOneWord.getJSONArray("cw");
                    for (int j = 0; j < JSONOneWordArray.length(); j++) {
                        JSONObject JSONOneWordObject = JSONOneWordArray.getJSONObject(j);
                        resultWords = resultWords + " "+ JSONOneWordObject.getString("w");

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            tvOut.setText(tvOut.getText() + resultWords);
            allResultWords = allResultWords+resultWords;
//            System.out.println(resultWords);
//            tvOut.setText(tvOut.getText() + jsonTokener.getJSONA);
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
