package me.zhaoliufeng.toolslib;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SDK工具类
 */

public class SDCardUtils {

    /**
     * 判断SD卡是否已经挂载成功
     * @return 是否已经挂载成功
     */
    private static boolean isSDCardMounted() {
        // 通过Environment获取当前SD卡的状态
        String state = Environment.getExternalStorageState();
        // 如果当前SD卡状态与MEDIA_MOUNTED相同，则表示SD卡已经挂载成功
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 返回SD卡的根路径
     * @return 如果SD卡处于挂载状态则返回SD卡根路径，否则返回null
     */
    private static String getSDCardRootDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 创建根目录
     * @return 创建状态
     */
    public static SDCardState createRootFileFolder(String folderName){
        File dirFirstFolder = new File(getSDCardRootDir() +"/" + folderName);
        //如果文件夹不存在则创建文件夹
        if(!dirFirstFolder.exists())
        {
            if (dirFirstFolder.mkdirs())
                return SDCardState.CREATE_SUCCESS;
            else
                return SDCardState.CREATE_FAIL;
        }
        return SDCardState.EXISTS;
    }

    /**
     * @return 返回SD卡的总容量
     */
    @SuppressWarnings("deprecation")
    public static long getSDCardTotalSize() {
        if (isSDCardMounted()) {
            // 簇StatFs Statistic File System文件系统统计
            StatFs sf = new StatFs(getSDCardRootDir()); // 获取对SD卡根路径的统计
            // 先获取当前系统中，每一簇所代表大小
            int blockSize = sf.getBlockSize();
            // 然后获取SD卡中有多少簇
            int blockCount = sf.getBlockCount();

            return blockSize * blockCount / 1024 / 1024; // 以兆的单位返回
        }
        return 0;
    }

    enum SDCardState{
        EXISTS,     //以存在
        CREATE_SUCCESS,     //创建文件夹成功
        CREATE_FAIL //创建文件夹失败
    }

}
