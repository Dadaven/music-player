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
    //ArrayList<Music>�������
    MusicFiles = new ArrayList<Music1>();
    //��ѯý�����ݿ�
    ContentResolver resolver = context.getContentResolver();
 
    Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    if(cursor!=null)
    //����ý�����ݿ�
    if (cursor.moveToFirst()) {
        while (!cursor.isAfterLast()) {
            //�������MediaStore.Audio.Media._ID
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

            //��������
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

            //������ר����MediaStore.Audio.Media.ALBUM
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

            //�����ĸ�����MediaStore.Audio.Media.ARTIST
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            //�����ļ���·��MediaStore.Audio.Media.DATA
            String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            //�������ܲ���ʱ��MediaStore.Audio.Media.DURATION
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            //�����ļ��Ĵ�СMediaStore.Audio.Media.SIZE
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

            //����ͼƬ
            
            if (size > 1024 * 800) {     //�Ƿ����800K
                if (title.equals("<unknown>") || title.equals("")) {
                    title = "δ֪";
                }
                if ("<unknown>".equals(artist) || "".equals(artist)) {
                    artist = "���Ķ�";
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
