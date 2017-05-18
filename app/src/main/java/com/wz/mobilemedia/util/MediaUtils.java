/*
package com.wz.mobilemedia.util;


import com.wz.wtxyy.domain.SongInfo;
import com.wz.wtxyy.domain.WTXAPP;

import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;


public class MediaUtils {
*
	 * 通过文件获取mp3的相关数据信息
	 * 
	 * @param filePath
	 * @return



	public static SongInfo getSongInfoByFile(String filePath) {
		File sourceFile = new File(filePath);
		if (!sourceFile.exists())
			return null;
		SongInfo songInfo = null;
		try {
			//AudioFileIO.logger.setLevel(Level.SEVERE);
			//ID3v23Frame.logger.setLevel(Level.SEVERE);
			//ID3v23Tag.logger.setLevel(Level.SEVERE);
			MP3File mp3file = new MP3File(sourceFile);
			MP3AudioHeader header = mp3file.getMP3AudioHeader();
			if (header == null)
				return null;
			songInfo = new SongInfo();
			// 歌曲时长
			String durationStr = header.getTrackLengthAsString();
			long duration = getTrackLength(durationStr);
			if (sourceFile.length() < 1024 * 1024 || duration < 5000) {
				return null;
			}
			// 文件名
			String displayName = sourceFile.getName();
			if (displayName.contains(".mp3")) {
				String[] displayNameArr = displayName.split(".mp3");
				displayName = displayNameArr[0].trim();
			}
			String artist = "";
			String title = "";
			if (displayName.contains("-")) {
				String[] titleArr = displayName.split("-");
				artist = titleArr[0].trim();
				title = titleArr[1].trim();
			} else {
				title = displayName;
			}


			String imagePath = saveMP3Image(sourceFile, String.valueOf(WTXAPP.getContext().getCacheDir()), true);
			songInfo.setAlbumUrl(imagePath);
			songInfo.setSid(IDGenerate.getId());
			songInfo.setDisplayName(displayName);
			songInfo.setSinger(artist);
			songInfo.setTitle(title);
			songInfo.setDuration(duration);
			songInfo.setDurationStr(durationStr);
			songInfo.setSize(sourceFile.length());
			songInfo.setSizeStr(getFileSize(sourceFile.length()));
			songInfo.setFilePath(filePath);
			songInfo.setType(SongInfo.LOCALSONG);
			songInfo.setIslike(SongInfo.UNLIKE);
			songInfo.setDownloadStatus(SongInfo.DOWNLOADED);
			songInfo.setCreateTime(DateUtil.dateToString(new Date()));

			mp3file = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return songInfo;

	}

*
	 * 时间格式转换
	 * 
	 * @param time
	 * @return


	public static String formatTime(int time) {

		time /= 1000;
		int minute = time / 60;
		// int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

*
	 * 计算文件的大小，返回相关的m字符串
	 * 
	 * @param fileS
	 * @return


	public static String getFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

*
	 * 获取歌曲长度
	 * 
	 * @param trackLengthAsString
	 * @return


	private static long getTrackLength(String trackLengthAsString) {

		if (trackLengthAsString.contains(":")) {
			String temp[] = trackLengthAsString.split(":");
			if (temp.length == 2) {
				int m = Integer.parseInt(temp[0]);// 分
				int s = Integer.parseInt(temp[1]);// 秒
				int currTime = (m * 60 + s) * 1000;
				return currTime;
			}
		}
		return 0;
	}




*
	 * 获取MP3封面图片
	 * @param mp3File
	 * @return


	public static byte[] getMP3Image(File mp3File) {
		byte[] imageData = null;
		try {
			MP3File mp3file = new MP3File(mp3File);
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();
			AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
			FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
			imageData = body.getImageData();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return imageData;
	}



*
	 *获取mp3图片并将其保存至指定路径下
	 * @param mp3File mp3文件对象
	 * @param mp3ImageSavePath mp3图片保存位置（默认mp3ImageSavePath +"\" mp3File文件名 +".jpg" ）
	 * @param cover 是否覆盖已有图片
	 * @return 生成图片全路径


	public static String saveMP3Image(File mp3File, String mp3ImageSavePath, boolean cover) {
		//生成mp3图片路径
		String mp3FileLabel = getFileLabel(mp3File.getName());
		String mp3ImageFullPath = mp3ImageSavePath + ("/" + mp3FileLabel + ".jpg");

		//若为非覆盖模式，图片存在则直接返回（不再创建）
		if( !cover ) {
			File tempFile = new File(mp3ImageFullPath) ;
			if(tempFile.exists()) {
				return mp3ImageFullPath;
			}
		}

		//生成mp3存放目录
		File saveDirectory = new File(mp3ImageSavePath);
		saveDirectory.mkdirs();

		//获取mp3图片
		byte imageData[] = getMP3Image(mp3File);
		//若图片不存在，则直接返回null
		if (null == imageData || imageData.length == 0) {
			return null;
		}
		//保存mp3图片文件
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mp3ImageFullPath);
			fos.write(imageData);
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return mp3ImageFullPath;
	}


*
	 * 仅返回文件名（不包含.类型）
	 * @param fileName
	 * @return


	private static String getFileLabel(String fileName) {
		int indexOfDot = fileName.lastIndexOf(".");
		fileName = fileName.substring(0,(indexOfDot==-1?fileName.length():indexOfDot));
		return fileName;
	}
	private static String toGB2312(String s) {
		try {
			return new String(s.getBytes("ISO-8859-1"), "gb2312");
		} catch (Exception e) {
			return s;
		}
	}
}
*/
