#### 项目说明
极速识别中国二代身份证(无需联网，离线秒扫，极速识别)
本应用使用相机进行识别中国二代身份证信息正反面,也可以选择相册中的图片,(OCR库还支持驾驶证、护照、军官证) 不需要联网即可离线识别，识别速度快，识别率高, 可保存识别图片。

<img src='https://github.com/XieZhiFa/IdCardOCR/blob/master/image/device-demo.png?raw=true' width='800' alt='扫描示例'/>
 
 
#### Application中初始化
    LibraryInitOCR.initOCR(context);


#### 调用扫描界面
	Intent intent = new Intent("com.msd.ocr.idcard.ICVideo");
	intent.putExtra("saveImage", false);    //是否保存图片
	startActivityForResult(intent, REQUEST_CODE);


#### 返回结果
<img src='https://github.com/XieZhiFa/IdCardOCR/blob/master/image/device-result.png?raw=true' width='375' alt='识别结果'/>

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
                textview.setText(sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
	    }
    }



#### aar集成方法
将文件aar文件复制到 libs目录下, 然后在build.gradle中增加:

    android{
        repositories {
            flatDir {
                dirs 'libs'
            }
        }

        manifestPlaceholders = [
            "OCR_API_KEY" : ""    //OCR密钥联系QQ2227421573授权
        ]
        ndk {
            abiFilters 'armeabi', 'armeabi-v7a', 'x86'
        }
    }

    dependencies {
        implementation fileTree(include: ['*.jar'], dir: 'libs')
        implementation (name: 'library-ocr-1.0.2-SNAPSHOT', ext: 'aar')
    }


#### 自定义识别框方式集成
如果当前扫描界面无法满足,您可以自己开发相机预览界面,使用以下API进行识别. 

    //1. Application中初始化
    LibraryInitOCR.initOCR(context); 
    
    //2. 初始化解码器
    /**
     * 解码器初始化, 如果需要保存图片, 需要在调用向系统审核SDCard读写权限.
     * @param context   Activity
     * @param handler   接收解码消息
     * @param isSaveImage   是否保存图片
     */
    public static void initDecode(Context context, Handler handler, boolean isSaveImage){
        idCardDecode = new IDCardDecode(context, handler, isSaveImage);
    }



    //3. 开始解码
    /**
     * 开始解码, 将相机预览数据传递到这里来, onPreviewFrame(byte[] data, Camera camera)
     * @param rect  预览框对象
     * @param previewWidth  界面预览宽
     * @param previewHeight 界面预览高
     * @param data  相机预览数据
     */
    public static void decode(Rect rect, int previewWidth, int previewHeight, byte[] data){
        idCardDecode.decode(rect, previewWidth, previewHeight, data);
    }

    
    /**
     * 识别选择的身份证图片(注意提前申请读写权限)
     * @param filePath  文件路径
     */
    public static void decode(String filePath){
        idCardDecode.decode(filePath);
    }


    //4.在Activity onDestroy 释放资源
    /**
     * 释放资源
     */
    public static void closeDecode(){
        if(idCardDecode != null){
            idCardDecode.close();
            idCardDecode = null;
        }
    }
    
    
    
    //解码结果通过handler 接收
    switch (msg.what){
        //解码成功
        case LibraryInitOCR.DECODE_SUCCESS: {
            Intent intent = (Intent) msg.obj;
            String result = intent.getStringExtra("OCRResult");
            String headImg = intent.getStringExtra("headImg");
            String fullImg = intent.getStringExtra("fullImg");
            break;
        }

        //解码失败
        case LibraryInitOCR.DECODE_FAIL:{
            break;
        }

        //未授权
        case LibraryInitOCR.DECODE_UNAUTHORIZED:{
            break;
        }

        //提示重新聚焦
        case LibraryInitOCR.DECODE_AUTO_FOCUS:{
            break;
        }
    }
    
    

#### 混淆排除
    #排除身份证识别库本地方法
    -keep class com.ym.idcard.reg.** {*;}
    -keep class com.ym.ocr.img.** {*;}
    -keep class hotcard.doc.reader.** {*;}
    -keep class com.msd.ocr.idcard.LibraryInitOCR {*;}
    
    -keepclassmembers class * {
        native <methods>;
    }
    -keepclasseswithmembernames class * {
        native <methods>;
    }




#### 技术支持 QQ 2227421573
    applicationId、deubg keystore、release keystore 开发版本及发布版 sha1 发给我。觉得好用的发个红包鼓励下。
    



#### 身份证示例图
对着电脑扫描识别率会比较低, 建议使用身份证原件做测试.<br/><br/>
![身份证示例图](https://github.com/XieZhiFa/IdCardOCR/blob/master/image/%E7%A4%BA%E4%BE%8B%E8%BA%AB%E4%BB%BD%E8%AF%81.png?raw=true)


