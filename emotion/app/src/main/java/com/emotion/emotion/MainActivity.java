package com.emotion.emotion;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Boolean doubleBackToExitPressedOnce = false;

    private Switch swMode;
    private Button btnSound, btnClear;
    private TextView tvSound, tvResp;
    private ImageView ivResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        Log.d("REQ", "data:: " + permission);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQ", "Need Internet Permission!");
        }
        */
        swMode = (Switch)findViewById(R.id.swMode);
        btnSound = (Button)findViewById(R.id.btnSound);
        btnClear = (Button)findViewById(R.id.btnClear);
        tvSound = (TextView)findViewById(R.id.tvSound);
        tvResp = (TextView)findViewById(R.id.tvResp);
        ivResp = (ImageView)findViewById(R.id.ivResp);

        btnSound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

                try {
                    startActivityForResult(intent, 1);
                }catch (ActivityNotFoundException a){
                    Toast t = Toast.makeText(getApplicationContext(), "OPP", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tvSound.setText(R.string.start_talk);
                tvResp.setText(R.string.tv_empty);
                ivResp.setImageResource(R.mipmap.ic_launcher);
            }
        });
        swMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    swMode.setText(R.string.SVM_mode);
                }
                else{
                    swMode.setText(R.string.SL_mode);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String resp = text.get(0);
                    tvSound.setText(resp);  //直接拿text.get(0)來用
                    if(swMode.isChecked()){
                        new SVMPost(MainActivity.this).execute(resp);
                    }
                    else{
                        new SLPost(MainActivity.this).execute(resp);
                    }

                }
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.again_leave, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
