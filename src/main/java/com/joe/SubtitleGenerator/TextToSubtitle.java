package com.joe.SubtitleGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class TextToSubtitle {

	public static boolean text2Subtitle(String subtitlePath, String text, long i, long start, long end) throws Exception {

		File file = new File(subtitlePath);

		if (i == 0) {
			if (file.exists()) { // 存在则先删除文件
				System.err.println("删除原字幕文件");
				file.delete();
			} else {
				System.err.println("创建字幕文件");
				file.createNewFile();
			}
		} else {
			if (!file.exists()) {
				System.err.println("ERROR! 字幕文件不存在!");
				return false;
			}
		}

		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file,true), "UTF-8");
		//true表示从末尾添加
		BufferedWriter writer = new BufferedWriter(write);
		
		writer.write(""+(i+1)+"\n");
		writer.write(second2String(start)+" --> "+second2String(end)+"\n");	
		writer.write(text+"\n");
		writer.close();
		
		return true;
	}

	public static String second2String(long l) {

		long hour = l / 3600;
		long minute = (l - hour * 3600) / 60;
		long second = l - hour * 3600 - minute * 60;

		String h, m, s;
		if (hour == 0) {
			h = "00";
		} else if (hour < 10) {
			h = "0" + hour;
		} else {
			h = "" + hour;
		}

		if (minute == 0) {
			m = "00";
		} else if (minute < 10) {
			m = "0" + minute;
		} else {
			m = "" + minute;
		}

		if (second == 0) {
			s = "00";
		} else if (second < 10) {
			s = "0" + second;
		} else {
			s = "" + second;
		}
		return h + ":" + m + ":" + s + ".000";
	}
}
