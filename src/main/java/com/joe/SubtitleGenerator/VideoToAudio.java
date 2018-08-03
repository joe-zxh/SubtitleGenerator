package com.joe.SubtitleGenerator;

import java.io.File;

import it.sauronsoftware.jave.*;

public class VideoToAudio {
	public static String getAudio(String filePath){
		File source=new File(filePath); 
		File target = new File(filePath.substring(0, filePath.length()-4) + ".wav");
		String targetPath = target.getPath();
		AudioAttributes audio = new AudioAttributes();
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(1));
		audio.setCodec("pcm_s16le");
		audio.setSamplingRate(new Integer(16000));
		EncodingAttributes attrs=new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try{
			encoder.encode(source,target,attrs);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(EncoderException e){
			e.printStackTrace();
		}
		return targetPath;
	}
}
