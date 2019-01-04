[TOC]

#### 项目说明
极速识别中国二代身份证(无需联网，离线秒扫，极速识别)
QQ 2227421573
本应用使用相机进行识别中国二代身份证信息正反面 (OCR库还支持驾驶证、护照、军官证) 不需要联网即可离线识别，识别速度快，识别率高, 可保存识别图片，可以受权AAR包给第三方开发。

#### 调用方式
	Intent intent = new Intent("com.msd.ocr.idcard.ICVideo");
	intent.putExtra("saveImage", false);    //是否保存图片
	startActivityForResult(intent, REQUEST_CODE);

#### 返回结果
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
            //sb.append(String.format("整体照片 = %s\n", jo.opt("imgPath")));
            //sb.append(String.format("头像路径 = %s\n", jo.opt("headPath")));
              binding.textview.setText(sb.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
