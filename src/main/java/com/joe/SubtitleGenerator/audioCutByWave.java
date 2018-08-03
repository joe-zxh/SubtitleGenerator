package com.joe.SubtitleGenerator;

import java.util.ArrayList;

public class audioCutByWave {
	public static void main(String[] args) {
		String directory = "D:\\eclipse\\eclipse-workspace\\test\\";
		String audioName = "ReliableMulticast";
		String audioPath = directory+audioName+".wav";
		int threshold = 300;
		int minCutSec = 6;
		int nearby = 400;
		ArrayList<Integer> timeStamp = audioCutByWave2(audioPath, threshold, minCutSec, nearby);
				
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
			
			if(!WavCut.cut(audioPath, directory+audioName+"_"+i+".wav", start, end)) {
				System.err.println("切割音频失败");
				break;
			}
			String iaudioPath=directory+audioName+"_"+i+".wav";			
		}
				
	}
	
	public static ArrayList<Integer> audioCutByWave2(String audioPath, int threshold, int minCutSec, int nearby) { //divide切割的分水岭   minCutSec是最小的切割时间
		
		ArrayList<Integer> timeStamp = new ArrayList(); //切割的时间段
		
		timeStamp.add(0);//从0s开始
		
		WaveFileReader reader = new WaveFileReader(audioPath);
		
		int[] data = reader.getData()[0]; //获取第一声道的采样数据
		
		int sampleRate = (int) reader.getSampleRate();
		
		int endSec = data.length/sampleRate;
		
		for (int secIndex = 0;secIndex<=endSec;) {
			
			if (secIndex!=endSec) {
				secIndex = Math.min(endSec, secIndex+minCutSec);
			} else {
				break;
			}			
			
			while(secIndex<endSec && !checkNearbySec(data, secIndex, threshold, nearby, sampleRate)) {
				secIndex++;
			}			
			timeStamp.add(secIndex);			
		}	
		return timeStamp;		
	}
	
	//检查附近的点是否也是小于阈值的
	public static boolean checkNearbySec(int[] data, int secIndex, int threshold, int nearby, int sampleRate) {		
			int startInd = Math.max(0, secIndex*sampleRate-nearby);
			int endInd = Math.min(data.length-1, secIndex*sampleRate+nearby);
			
			for (int i = startInd;i<=endInd;i++) {
				if(data[i]>threshold||data[i]<-threshold) {
					return false;
				}
			}
			return true;
	}
	

	public static ArrayList<Integer> audioCutByWave(String audioPath, int divide, int minCutSec, int nearby, int step) { //divide切割的分水岭   minCutSec是最小的切割时间
				
		ArrayList<Integer> timeStamp = new ArrayList(); //切割的时间段
		
		timeStamp.add(0);//从0开始
		
		WaveFileReader reader = new WaveFileReader(audioPath);
		
		int[] data = reader.getData()[0]; //获取第一声道的采样数据
		
		int sampleRate = (int) reader.getSampleRate();
		
		for (int i = 0;i<data.length;i++) {
			
			if(i+minCutSec*sampleRate<(double)data.length) {
				
				i = i+minCutSec*sampleRate;
				
				while (i<data.length && !checkNearby(data, i, divide, nearby, step)) {
					i+=nearby;					
				}
				
				if (i<data.length) {
					timeStamp.add((int) Math.round(i/(double)(sampleRate)));					
				} else {
					timeStamp.add((data.length-1)/sampleRate);
					break;
				}				
			} else {
				timeStamp.add((data.length-1)/sampleRate);
				break;
			}		
		}	
		return timeStamp;		
	}
	
	//检查附近的点是否也是小于阈值的
	public static boolean checkNearby(int[] data, int iter, int divide, int nearby, int step) {		
		int start = Math.max(0, iter-nearby);
		int end = Math.min(iter+nearby, data.length-1);
				
		for (int i = start;i<=end;) {
			if (data[i]>divide||data[i]<-divide) {
				return false;
			}
			i+=step;
		}		
		return true;
	}
}
