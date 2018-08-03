package com.joe.SubtitleGenerator;

import java.io.File;
import java.util.ArrayList;

public class subtitleGenerator {
	
	public static void main(String[] args) throws Exception {
		
		String videoName = "SpeechSample";//视频文件的名字，视频默认放在相对路径:./test文件夹中
		int language = 1737;//语言：1737是识别英文  1536普通话+简单英文(无标点)  1537普通话  1637粤语  1936普通话远场
				
		int threshold = 400; //音频的采样值<400认为是 没有声音的点
		int minCutSec = 1; //最少相邻 1秒 进行切割
		int nearby = 200; //切割节点前后200个点 如果是没有声音的点，才进行切割
		
		//设置APPID/AK/SK
	    //public static final String APP_ID = "你的 App ID";
	    //public static final String API_KEY = "你的 Api Key";
	    //public static final String SECRET_KEY = "你的 Secret Key";
				
		subtitleGenerate(videoName, language, threshold, minCutSec, nearby, APP_ID, API_KEY, SECRET_KEY);	
	}
	
	public static void subtitleGenerate(String videoName, int language, int threshold, int minCutSec, int nearby, String appId, String apiKey, String secretKey) throws Exception {
		String directory = System.getProperty("user.dir")+"\\src\\main\\java\\com\\joe\\SubtitleGenerator\\test\\";
		String videoPath = directory+videoName+".mp4";
		
		String audioPath = VideoToAudio.getAudio(videoPath);
		
		ArrayList<Integer> timeStamp = audioCutByWave.audioCutByWave2(audioPath, threshold, minCutSec, nearby);
		
		System.out.println(timeStamp.size());
		for (int i = 0;i<timeStamp.size();i++) {
			System.out.println(timeStamp.get(i));
		}		
		
		int start = timeStamp.get(0);
		int end = timeStamp.get(1);
		
		for (int i = 0;i+1<timeStamp.size();i++) {
			
			if (i ==0 ) {
				start = timeStamp.get(0);
				end = timeStamp.get(1);
			}else {
				start = end;
				end = timeStamp.get(i+1);
			}
			
			String iaudioPath=directory+videoName+"_"+i+".wav";			
			if(!WavCut.cut(audioPath, iaudioPath, start, end)) {
				System.err.println("切割音频失败");
				break;
			}			
			
			String text = AudioToText.audio2Text(iaudioPath, language, appId, apiKey, secretKey);
			TextToSubtitle.text2Subtitle(directory+videoName+".srt", text, i, start, end);
			WavCut.deleteAudio(iaudioPath);
		}					
		WavCut.deleteAudio(audioPath);				
		System.err.println("恭喜！字幕制作完成!");		
	}		
}
