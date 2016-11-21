package com.example.david.allreminder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.david.allreminder.util.Constant;
import com.example.david.allreminder.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class VoiceRecognizeDialogActivity extends Activity implements View.OnClickListener {
    private Button btnSubmit,btnDirectCreate;
    private ImageButton btnCancel;
    private Context context;
    private Intent intent=null;
    private SpeechRecognizer mIat;
    private String allResultWords="";
    private int from = 0;



    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recongnize_dialog_actvity);

        setContext(this);
        from = getIntent().getIntExtra("from",0);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnDirectCreate = (Button) findViewById(R.id.btnDirectCreate);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
//        btnStartSpeech = (Button) findViewById(R.id.btnStartSpeech);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDirectCreate.setOnClickListener(this);
//        btnStartSpeech.setOnClickListener(this);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=55b72c58");
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        startVoiceRecognition();
    }

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                System.out.println("ERROR!!!");
            }
            System.out.println( "初始化code:" + i );
        }
    };
    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            System.out.println("onBeginOfSpeech");

        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("onEndOfSpeech");

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            JSONTokener jsonTokener = new JSONTokener(recognizerResult.getResultString());
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
                        resultWords += Constant.CMD_SEPARATOR + JSONOneWordObject.getString("w");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            allResultWords = allResultWords+resultWords;
            if (b){
                if (from== Constant.INTENT_FROM_HOME_TO_VOICE){
                    intent = new Intent("com.example.david.allreminder.intent.action.CreateReminderActivity");
                    intent.putExtra("from",Constant.INTENT_FROM_VOICE_TO_CREATE_WITH_CMD);
                    intent.putExtra("words", allResultWords);
                    finish();
                    startActivity(intent);
                }else{
                    intent = new Intent();
                    intent.putExtra("words",allResultWords);
                    setResult(1, intent);
                    finish();
                }
            }

            System.out.println(">>>>>>>>>>>>>"+resultWords);
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
    public void setParm(){
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT,"mandarin");
    }
    public void startVoiceRecognition(){
        setParm();
        int ret = mIat.startListening(recognizerListener);
        System.out.println("开始听：" + ret);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voice_recongnize_dialog_actvity, menu);
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
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btnStartSpeech:
////                startVoiceRecognition();
//                break;
            case R.id.btnSubmit:
                mIat.stopListening();
//                System.out.println("stop<><><><><><><><><><>");
                break;
            case R.id.btnCancel:
                mIat.cancel();
                finish();
                break;
            case R.id.btnDirectCreate:
                intent = new Intent("com.example.david.allreminder.intent.action.CreateReminderActivity");
                intent.putExtra("from",Constant.INTENT_FROM_MAIN_TO_CREATE_WITHOUT_CMD);
                finish();
                startActivity(intent);
                break;
        }

    }
}
