package com.wz.mobilemedia.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import com.wz.mobilemedia.bean.MediaInfoBean;
import com.wz.mobilemedia.bean.StorageInfo;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机可用的外置内存卡和内置的内存卡
 *
 * @author Administrator
 */
public class StorageListUtil<T> {
    private Activity mActivity;
    private StorageManager mStorageManager;
    private Method mMethodGetPaths;
    private static ArrayList<String> sListPath = new ArrayList<>();
    private static String[]  extensions = {".avi",".3gp",".wmv",".ts",".rmvb",".mov",".m4v",".m3u8",".3gpp",".3gpp2",
            ".mkv",".flv",".divx",".f4v",".rm",".asf",".ram",".mpg",".v8",".swf",".m2v",".asx",".ra",".ndivx",".xvid"};


    private StorageListUtil(Activity activity) {
        mActivity = activity;
        if (mActivity != null) {
            mStorageManager = (StorageManager) mActivity
                    .getSystemService(Activity.STORAGE_SERVICE);
            try {
                mMethodGetPaths = mStorageManager.getClass().getMethod(
                        "getVolumePaths");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getVolumePaths() {
        String[] paths = null;
        try {
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    @SuppressLint("NewApi")
    private static List<StorageInfo> listAvaliableStorage(Context context) {
        ArrayList<StorageInfo> storagges = new ArrayList<>();
        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod(
                    "getVolumeList", paramClasses);
            getVolumeList.setAccessible(true);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager,
                    params);
            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath",
                            new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);
                    info = new StorageInfo(path);
                    File file = new File(info.path);
                    if ((file.exists()) && (file.isDirectory())
                            && (file.canWrite())) {
                        Method isRemovable = obj.getClass().getMethod(
                                "isRemovable", new Class[0]);
                        String state = null;
                        try {
                            Method getVolumeState = StorageManager.class
                                    .getMethod("getVolumeState", String.class);
                            state = (String) getVolumeState.invoke(
                                    storageManager, info.path);
                            info.state = state;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (info.isMounted()) {
                            info.isRemoveable = ((Boolean) isRemovable.invoke(
                                    obj, new Object[0])).booleanValue();
                            storagges.add(info);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        storagges.trimToSize();

        return storagges;
    }

    /**
     * 扫描音乐，从手机文件夹中递归扫描
     */
    public static List<String> scannerMedia(Context context) {
        List<StorageInfo> list = StorageListUtil
                .listAvaliableStorage(context);
        for (int i = 0; i < list.size(); i++) {
            StorageInfo storageInfo = list.get(i);
            List<String> strings = scannerLocalMediaFile(storageInfo.path, true);

            return strings;
        }

        return null;
    }


    /**
     * @param Path        搜索目录
     * @param IsIterative 是否进入子文件夹
     */
    public static List<String> scannerLocalMediaFile(String Path,
                                                     boolean IsIterative) {
        File[] files = new File(Path).listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isFile()) {
                    // 判断扩展名
                    for (int j = 0; j < extensions.length; j++) {
                        if (f.getPath().endsWith(extensions[j])) {
                            if (!f.exists()) {
                                continue;
                            }
                            // 文件名
                            String displayName = f.getName();
                            if (displayName.endsWith(extensions[j])) {
                                String path = f.getAbsolutePath();

                                sListPath.add(path);
                            }
                        }
                    }


                    if (!IsIterative)
                        break;
                } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
                {
                    scannerLocalMediaFile(f.getPath(), IsIterative);
                }
            }

            return sListPath;
        }
        return sListPath;
    }


    /**
     * 10M=10485760 b,小于10m的过滤掉
     * 过滤视频文件
     *
     * @param videoInfos
     * @return
     */
    private List<MediaInfoBean> filterVideo(List<MediaInfoBean> videoInfos) {
        List<MediaInfoBean> newVideos = new ArrayList<MediaInfoBean>();
        for (MediaInfoBean videoInfo : videoInfos) {
            File f = new File(videoInfo.getPath());
            if (f.exists() && f.isFile() && f.length() > 10485760) {
                newVideos.add(videoInfo);
                Log.i("TGA", "文件大小" + f.length());
            } else {
                Log.i("TGA", "文件太小或者不存在");
            }
        }
        return newVideos;
    }

}