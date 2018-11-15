package com.hw.photomovie.util;


public enum  ScaleType {
    /**
     * 左右上下裁剪填满输出窗口
     */
    CENTER_CROP,
    /**
     * 不管比例拉伸填满输出窗口
     */
    FIT_XY,
    /**
     * 居中，同时填满输出窗口的宽或者高
     */
    FIT_CENTER
}
