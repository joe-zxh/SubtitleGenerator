package com.joe.SubtitleGenerator;

import it.sauronsoftware.jave.Encoder;  
import it.sauronsoftware.jave.MultimediaInfo;  
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.nio.ByteBuffer;
import java.nio.file.Path;  
  
/** 
 * wav音频文件截取工具 
 * （适用于比特率为128kbps的wav音频文件，此类音频文件的头部信息占用长度44字节） 
 * @author lwj 
 * 
 */  
public class WavCut {  
      
    /** 
     * 截取wav音频文件 
     * @param sourcepath  源文件地址 
     * @param targetpath  目标文件地址 
     * @param start  截取开始时间（秒） 
     * @param end  截取结束时间（秒） 
     *  
     * return  截取成功返回true，否则返回false 
     */  
    public static boolean cut(String sourcefile, String targetfile, long start, long end) {  
        try{  
            if(!sourcefile.toLowerCase().endsWith(".wav") || !targetfile.toLowerCase().endsWith(".wav")){  
                return false;  
            }  
            File wav = new File(sourcefile);  
            if(!wav.exists()){  
                return false;  
            }  
            long t1 = getTimeLen(wav);  //总时长(秒)  
            if(start<0 || end<=0 || start>=t1 || end>t1 || start>=end){  
                return false;  
            }  
            FileInputStream fis = new FileInputStream(wav);  
            long wavSize = wav.length()-44;  //音频数据大小（44为128kbps比特率wav文件头长度）  
            long splitSize = (wavSize/t1)*(end-start);  //截取的音频数据大小  
            long skipSize = (wavSize/t1)*start;  //截取时跳过的音频数据大小  
            int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));  
            int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));  
              
            ByteBuffer buf1 = ByteBuffer.allocate(4);  //存放文件大小,4代表一个int占用字节数  
            buf1.putInt(splitSizeInt+36);  //放入文件长度信息  
            byte[] flen = buf1.array();  //代表文件长度  
            ByteBuffer buf2 = ByteBuffer.allocate(4);  //存放音频数据大小，4代表一个int占用字节数  
            buf2.putInt(splitSizeInt);  //放入数据长度信息  
            byte[] dlen = buf2.array();  //代表数据长度  
            flen = reverse(flen);  //数组反转  
            dlen = reverse(dlen);  
            byte[] head = new byte[44];  //定义wav头部信息数组  
            fis.read(head, 0, head.length);  //读取源wav文件头部信息  
            for(int i=0; i<4; i++){  //4代表一个int占用字节数  
                head[i+4] = flen[i];  //替换原头部信息里的文件长度  
                head[i+40] = dlen[i];  //替换原头部信息里的数据长度  
            }  
            byte[] fbyte = new byte[splitSizeInt+head.length];  //存放截取的音频数据  
            for(int i=0; i<head.length; i++){  //放入修改后的头部信息  
                fbyte[i] = head[i];  
            }  
            byte[] skipBytes = new byte[skipSizeInt];  //存放截取时跳过的音频数据  
            fis.read(skipBytes, 0, skipBytes.length);  //跳过不需要截取的数据  
            fis.read(fbyte, head.length, fbyte.length-head.length);  //读取要截取的数据到目标数组  
            fis.close();  
              
            File target = new File(targetfile);  
            if(target.exists()){  //如果目标文件已存在，则删除目标文件  
                target.delete();  
            }  
            FileOutputStream fos = new FileOutputStream(target);  
            fos.write(fbyte);  
            fos.flush();  
            fos.close();  
        }catch(IOException e){  
            e.printStackTrace();  
            return false;  
        }  
        return true;  
    }  
          
    /** 
     * 获取音频文件总时长  单位是秒
     * @param filePath  文件路径 
     * @return 
     */  
    public static long getTimeLen(File file){  
        long tlen = 0;  
        if(file!=null && file.exists()){  
            Encoder encoder = new Encoder();  
            try {  
                 MultimediaInfo m = encoder.getInfo(file);  
                 long ls = m.getDuration();  
                 tlen = ls/1000;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return tlen;  
    }  
      
    /** 
    * 数组反转 
    * @param array 
    */  
    public static byte[] reverse(byte[] array){  
        byte temp;  
        int len=array.length;  
        for(int i=0;i<len/2;i++){  
            temp=array[i];  
            array[i]=array[len-1-i];  
            array[len-1-i]=temp;  
        }  
        return array;  
    }  
      
    public static void main(String[] args){
    	String path = "D:\\eclipse\\eclipse-workspace\\test\\src\\main\\java\\com\\joe\\SubGen\\test\\testVideo";
        System.out.println(cut(path+".wav",path+"_0_10.wav",0,10));  
        System.out.println(cut(path+".wav",path+"_10_20.wav",10,20));  
        System.out.println(cut(path+".wav",path+"_20_28.wav",20,28));   
    }  
    
    public static boolean deleteAudio(String targetfile) {
    	
    	File file = new File(targetfile);
    	
    	if(!file.delete()) {
    		System.err.println("删除中间文件失败！");
    		return false;
    	}    
    	System.out.println("删除中间文件成功！");
    	return true;
    }
  
}  
