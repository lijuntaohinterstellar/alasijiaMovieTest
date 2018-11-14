package com.example.newsdog.alasijiatest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;

import com.hw.photomovie.PhotoMovieAdapter;
import com.hw.photomovie.PhotoMovieFactory;
import com.hw.photomovie.PhotoMovieRenderer;
import com.hw.photomovie.filter.FilterType;
import com.hw.photomovie.render.GLTextureView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivityTest";
    private GLTextureView mGLTextureView;
    PhotoMovieAdapter mPhotoMovieAdapter = null;

    Handler mHandler = new Handler(Looper.getMainLooper());

    private ArrayList<String> mImages = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏无状态栏，并竖屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLTextureView = new GLTextureView(this);
        mPhotoMovieAdapter = new PhotoMovieRenderer(this, mGLTextureView);

//        mImages.add("/storage/emulated/0/DCIM/Compose20181026205024.jpg");
//        mImages.add("/storage/emulated/0/DCIM/Compose20181026151149.jpg");
//        mImages.add("/storage/emulated/0/DCIM/Compose20181106113729.jpg");

        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181112_140104.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181112_140059.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112901.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112900.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112859.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112857.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112855.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_112853.jpg");
        mImages.add("/storage/emulated/0/DCIM/Camera/IMG_20181113_113723.jpg");

        setContentView(mGLTextureView);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 添加图片序列
                mPhotoMovieAdapter.addImages(mImages, PhotoMovieFactory.PhotoMovieType.WINDOW);

            }
        }, 500);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 切换动画效果
//                mPhotoMovieAdapter.setTransfermation(PhotoMovieFactory.PhotoMovieType.SCALE);
//
//            }
//        }, 3000);
//
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置滤镜
                mPhotoMovieAdapter.setFilter(FilterType.GRAY);

            }
        }, 2000);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String path = "file:///storage/emulated/0/MIUI/music/mp3/%E6%B2%99%E6%BC%A0%E9%AA%86%E9%A9%BC_%E5%B1%95%E5%B1%95%E4%B8%8E%E7%BD%97%E7%BD%97_%E6%B2%99%E6%BC%A0%E9%AA%86%E9%A9%BC.mp3";
//                // 设置滤镜
//                mPhotoMovieAdapter.setMusic(null);
//
//            }
//        }, 3000);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 设置滤镜
//                mPhotoMovieAdapter.setFilter(FilterType.NONE);
//
//            }
//        }, 5000);
//
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 合成视频
                mPhotoMovieAdapter.composeVideo("", "");
//                composeVideo(R.drawable.w, initVideoFile().getAbsolutePath());


            }
        }, 7000);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 设置滤镜
//                mPhotoMovieAdapter.onPause();
//                mGLTextureView.onPause();
//                Log.i("mainActivity", "onPause");
//            }
//        }, 20000);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //
//                mPhotoMovieAdapter.onResume();
//                mGLTextureView.onResume();
//                Log.i("mainActivity", "onResume");
//
//            }
//        }, 23000);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 设置滤镜
//                mDemoPresenter.addMusic("");
//
//            }
//        }, 7000);
    }
    private File initVideoFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            dir = getCacheDir();
        }
        return new File(dir, String.format("photo_movie_%s.mp4",
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis())));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
