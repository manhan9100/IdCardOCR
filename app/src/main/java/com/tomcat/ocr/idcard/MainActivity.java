package com.tomcat.ocr.idcard;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msd.ocr.idcard.LibraryInitOCR;
import com.msd.ocr.idcard.id.ICVideoActivity;
import com.tomcat.ocr.idcard.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;


        LibraryInitOCR.initOCR(context);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ICVideoActivity.startScan(context);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ICVideoActivity.REQUEST_CODE && resultCode == RESULT_OK){

            String result = data.getStringExtra("OCRResult");
            try {
                JSONObject jo = new JSONObject(result);
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("正面 = %s\n", jo.opt("type")));
                sb.append(String.format("姓名 = %s\n", jo.opt("name")));
                sb.append(String.format("性别 = %s\n", jo.opt("sex")));
                sb.append(String.format("民族 = %s\n", jo.opt("folk")));
                sb.append(String.format("日期 = %s\n", jo.opt("birt")));
                sb.append(String.format("号码 = %s\n", jo.opt("num")));
                sb.append(String.format("住址 = %s\n", jo.opt("addr")));
                sb.append(String.format("签发机关 = %s\n", jo.opt("issue")));
                sb.append(String.format("有效期限 = %s\n", jo.opt("valid")));
//                sb.append(String.format("整体照片 = %s\n", jo.opt("imgPath")));
//                sb.append(String.format("头像路径 = %s\n", jo.opt("headPath")));

                binding.textview.setText(sb.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
