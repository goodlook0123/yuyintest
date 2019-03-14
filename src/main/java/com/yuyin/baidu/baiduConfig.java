package com.yuyin.baidu;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class baiduConfig {


    //设置APPID/AK/SK
    public static final String APP_ID = "15105692";
    public static final String API_KEY = "PIIZsLIDqf9wjcqvBw4kCCFe";
    public static final String SECRET_KEY = "6Q054tD2f5NtFeuSNGhttCnt6QkCZU8R";

    public static void main(String[] args)
    {
        try {
            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            // 对本地语音文件进行识别
            //String path = "D:\\code\\java-sdk\\speech_sdk\\src\\test\\resources\\16k_test.pcm";
            String path = "D:\\FFOutput\\16k.pcm";
            JSONObject asrRes = client.asr(path, "pcm", 16000, null);
            System.out.println(asrRes);
            // 对语音二进制数据进行识别
            byte[] data = new byte[1000];     //readFileByBytes仅为获取二进制数据示例

            data = Util.readFileByBytes(path);
            JSONObject asrRes2 = client.asr(data, "pcm", 8000, null);
            System.out.println(asrRes2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
