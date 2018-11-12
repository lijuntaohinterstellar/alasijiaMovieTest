package com.hw.photomovie;

import android.net.Uri;

import com.hw.photomovie.filter.FilterType;

import java.io.File;
import java.util.ArrayList;

/**
 * 影集 Adapter, 提供外部调用影集模块的接口
 *
 */
public interface PhotoMovieAdapter {

    /**
     * 添加图片集合，生成照片电影
     * @param paths
     * @return
     */
    void addImages(ArrayList<String> paths);

    /**
     * 设置影集转场效果
     * @param movieType
     * @return
     */
    void setTransfermation(PhotoMovieFactory.PhotoMovieType movieType);

    /**
     * 设置影集转场效果
     * @param type
     * @return
     */
    void setFilter(FilterType type);

    /**
     * 设置影集音乐
     * @param uri
     * @return
     */
    void setMusic(Uri uri);

    /**
     * 合成影集视频
     * @param waterMark, filePath
     * @return
     */
    void composeVideo(String waterMark, String filePath);

    /**
     * 合成视频进度
     * @param lister
     */
    void composeProgress(PhotoMovieRenderer.OnComposeProgressListener lister);

    /**
     * 当前Activity启动另一个Activity时调用，外部紧接着调用GLTextureView对象的onPause()方法
     * @param
     */
    void onPause();

    /**
     * 返回当前Activity时调用，外部紧接着调用GLTextureView对象的onResume()方法
     * @param
     */
    void onResume();


}
