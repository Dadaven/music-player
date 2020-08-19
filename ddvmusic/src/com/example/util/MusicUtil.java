package com.example.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.entity.Music1;

public class MusicUtil {
	public static ArrayList<Music1> MusicFiles;
public static ArrayList<Music1> getMusicFile(Context context) {
    //ArrayList<Music>存放音乐
    MusicFiles = new ArrayList<Music1>();
    //查询媒体数据库
    ContentResolver resolver = context.getContentResolver();
 
    Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    if(cursor!=null)
    //遍历媒体数据库
    if (cursor.moveToFirst()) {
        while (!cursor.isAfterLast()) {
            //歌曲编号MediaStore.Audio.Media._ID
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

            //歌曲标题
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

            //歌曲的专辑名MediaStore.Audio.Media.ALBUM
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

            //歌曲的歌手名MediaStore.Audio.Media.ARTIST
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            //歌曲文件的路径MediaStore.Audio.Media.DATA
            String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            //歌曲的总播放时长MediaStore.Audio.Media.DURATION
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            //歌曲文件的大小MediaStore.Audio.Media.SIZE
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

            //歌曲图片
            
            if (size > 1024 * 800) {     //是否大于800K
                if (title.equals("<unknown>") || title.equals("")) {
                    title = "未知";
                }
                if ("<unknown>".equals(artist) || "".equals(artist)) {
                    artist = "龚文多";
                }

                Music1 music = new Music1(id, title, artist,
                        url, album, duration, size);
                MusicFiles.add(music);
            }
            cursor.moveToNext();
        }
    }
    return MusicFiles;
}


}
