package com.pjb.springbootjwt.zhddkk.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import javazoom.jl.player.Player;

public class MusicParserUtil {

    private static void play(String position) {
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(position));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getDuration(String position) {

        int length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(position));
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();

            // 单位为秒
            length = audioHeader.getTrackLength();

            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }
    
    public static String getMusicTrackTime(String filePosition) {
    	String res = "00:00";
    	int trackLength = getDuration(filePosition);
    	if (trackLength < 60) {
    		if (trackLength<10) {
    			res = "00:0" + trackLength;
    		}else {
    			res = "00:" + trackLength;
    		}
    	}else if (trackLength >=60 && trackLength<=3600) {
    		int minutes = trackLength / 60;
    		int seconds = trackLength % 60;
    		
    		String minutesStr = String.valueOf(minutes);
    		if (minutes < 10) {
    			minutesStr = "0" + minutes;
    		}
    		
    		String secondsStr = String.valueOf(seconds);
    		if (seconds < 10) {
    			secondsStr = "0" + seconds;
    		}
    		
    		res = minutesStr + ":" + secondsStr;
    	}
    	
    	return res;
    }
    
    public static void main(String[] args) {
        String position = "D:\\CloudMusic\\fsaf.mp3";
        play(position);
    	System.out.println(getMusicTrackTime(position));
    }
}
