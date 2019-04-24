package com.tomcat.ocr.idcard;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.msd.ocr.idcard.LibraryInitOCR;
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("saveImage", binding.saveImage.getSelectedItemPosition() == 0 ? true : false); // 是否保存识别图片
                bundle.putBoolean("showSelect", true);                          // 是否显示选择图片
                bundle.putBoolean("showCamera", true);                          // 显示图片界面是否显示拍照(驾照选择图片识别率比扫描高)
                bundle.putInt("requestCode", REQUEST_CODE);                     // requestCode
                bundle.putInt("type", binding.type.getSelectedItemPosition());  // 0身份证, 1驾驶证
                LibraryInitOCR.startScan(context, bundle);

                /*
                //如果您不想集成aar, 那么可以通过隐式意图拉起示例中的扫描界面
                boolean isSave = binding.tip.getVisibility() == View.GONE;
                Intent intent = new Intent("com.msd.ocr.idcard.ICVideo"); //身份证:com.msd.ocr.idcard.ICVideo, 驾驶证: com.msd.ocr.idcard.id.DIVideoActivity
                intent.putExtra("saveImage", isSave);//是否保存图片
                intent.putExtra("showSelect", true);//是否显示选择图片
                bundle.putBoolean("showCamera", true);// 显示图片界面是否显示拍照(驾照选择图片识别率比扫描高)
                intent.addCategory(getPackageName());//调用demo中的扫描界面使用: com.tomcat.ocr.idcard
                startActivityForResult(intent, REQUEST_CODE);
                */
            }
        });



        binding.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                binding.tip.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
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
                sb.append(String.format("整体照片 = %s\n", jo.opt("imgPath")));
                sb.append(String.format("头像路径 = %s\n", jo.opt("headPath")));
                sb.append("\n驾照专属字段\n");
                sb.append(String.format("国家 = %s\n", jo.opt("nation")));
                sb.append(String.format("初始领证 = %s\n", jo.opt("startTime")));
                sb.append(String.format("准驾车型 = %s\n", jo.opt("drivingType")));
                sb.append(String.format("有效期限 = %s\n", jo.opt("registerDate")));
                binding.textview.setText(sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
