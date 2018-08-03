package com.joe.SubtitleGenerator;

import java.io.PrintStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import com.baidu.aip.speech.AipSpeech;

public class AudioToText {	
	    	
	public static String audio2Text(String audioPath, int language, String appId, String apiKey, String secretKey) {
		// 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(appId, apiKey, secretKey);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        //String path = "D:\\eclipse\\eclipse-workspace\\test\\hello.wav";
        //String path = "D:\\eclipse\\eclipse-workspace\\test\\src\\main\\java\\com\\joe\\SubGen\\test\\testVideo_0_10.wav";
        
        HashMap<String, Object> pHashMap = new HashMap<String, Object>();
        pHashMap.put("dev_pid", language);//1737是英文 
        JSONObject res = client.asr(audioPath, "wav", 16000, pHashMap); 
        System.out.println(res.toString(2));
        
        int err_no = res.getInt("err_no");
        
        if(err_no==0) {//成功返回
        	JSONArray jArray = res.getJSONArray("result");
            String text = jArray.getString(0);//百度会返回5个值，我选第一个值
            System.err.println(text);
    		return text;
        }else {
        	System.err.println("音频质量差，或者没有声音！");
        	return "";
        }
            
	}

}
