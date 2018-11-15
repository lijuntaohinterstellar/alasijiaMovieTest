package com.hw.photomovie;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.hw.photomovie.filter.FilterItem;
import com.hw.photomovie.filter.FilterType;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.model.SimplePhotoData;
import com.hw.photomovie.moviefilter.IMovieFilter;
import com.hw.photomovie.record.GLMovieRecorder;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.GLTextureMovieRender;
import com.hw.photomovie.render.GLTextureView;
import com.hw.photomovie.timer.IMovieTimer;
import com.hw.photomovie.util.MLog;
import com.hw.photomovie.util.UriUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 影集 Adapter, 提供外部调用影集模块接口的具体实现
 */
public class PhotoMovieRenderer implements IMovieTimer.MovieListener, PhotoMovieAdapter {


    private final String TAG = "PhotoMovieRenderer";

    private PhotoMovie mPhotoMovie;
    private PhotoMoviePlayer mPhotoMoviePlayer;
    private GLSurfaceMovieRenderer mMovieRenderer;
    private Uri mMusicUri;
    private PhotoMovieFactory.PhotoMovieType mMovieType =
            PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS;
    Activity mActivity;
    GLTextureView mGLTextureView;
    FilterItem mFilterItem;
    private volatile OnComposeProgressListener mComposeProgressListener = null;
    ArrayList<String> mPhotos = new ArrayList<String>();


    public PhotoMovieRenderer(Activity activity, GLTextureView glTextureView) {
        mActivity = activity;
        mGLTextureView = glTextureView;
        mFilterItem = new FilterItem();
        initMoviePlayer();
    }

    /**
     * 添加图片集合，生成照片电影
     *
     * @param paths
     * @return
     */
    @Override
    public void addImages(ArrayList<String> paths, PhotoMovieFactory.PhotoMovieType type) {
        List<PhotoData> photoDataList = new ArrayList<PhotoData>(paths.size());
        for (String path : paths) {
            PhotoData photoData = new SimplePhotoData(mActivity, path,
                    PhotoData.STATE_LOCAL);
            photoDataList.add(photoData);
        }
        PhotoSource photoSource = new PhotoSource(photoDataList);
        if (mPhotoMoviePlayer == null) {
            startPlay(photoSource);
        } else {
            mPhotoMoviePlayer.stop();
            mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource,
                    type);
            mPhotoMoviePlayer.setDataSource(mPhotoMovie);
            if (mMusicUri != null) {
                mPhotoMoviePlayer.setMusic(mActivity, mMusicUri);
            }
            mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
                @Override
                public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
                }

                @Override
                public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "onPhotoPick onPrepared");
                            mPhotoMoviePlayer.start();
                        }
                    });
                }

                @Override
                public void onError(PhotoMoviePlayer moviePlayer) {
                    MLog.i("onPrepare", "onPrepare error");
                }
            });
            mPhotoMoviePlayer.prepare();
        }
    }

    private void startPlay(PhotoSource photoSource) {
        Log.i(TAG, "startPlay");
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        mPhotoMoviePlayer.prepare();
    }

    private void initMoviePlayer() {
        Log.i(TAG, "initMoviePlayer()");
        mMovieRenderer = new GLTextureMovieRender(mGLTextureView);
        //addWaterMark();
        mPhotoMoviePlayer = new PhotoMoviePlayer(mActivity.getApplicationContext());
        mPhotoMoviePlayer.setMovieRenderer(mMovieRenderer);
        mPhotoMoviePlayer.setMovieListener(this);
        mPhotoMoviePlayer.setLoop(true);
        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "onPrepared");
                        mPhotoMoviePlayer.start();
                    }
                });

            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
    }

    /**
     * 设置影集转场效果
     *
     * @param movieType
     * @return
     */
    @Override
    public void setTransition(PhotoMovieFactory.PhotoMovieType movieType) {
        mMovieType = movieType;
        mPhotoMoviePlayer.stop();
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        if (mMusicUri != null) {
            mPhotoMoviePlayer.setMusic(mActivity, mMusicUri);
        }
        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                if (mActivity == null) {
                    return;
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPhotoMoviePlayer.start();
                    }
                });
            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
        mPhotoMoviePlayer.prepare();
    }

    /**
     * 设置影集转场效果
     *
     * @param type
     * @return
     */
    @Override
    public void setFilter(FilterType type) {
        IMovieFilter movieFilter = mFilterItem.initFilter(type);
        mMovieRenderer.setMovieFilter(movieFilter);

    }

    /**
     * 设置影集音乐
     *
     * @param uri
     * @return
     */
    public void setMusic(Uri uri) {
        if (uri != null) {
            mMusicUri = uri;
            mPhotoMoviePlayer.setMusic(mActivity, uri);
            Log.i(TAG, "start music");
        }
        else {
            mPhotoMoviePlayer.stopMusic();
            Log.i(TAG, "stop music");
        }
    }


    @Override
    public void composeVideo(int waterDrawable, String filePath) {
        Bitmap waterMark = BitmapFactory.decodeResource(mActivity.getResources(), waterDrawable);
        composeVideo(waterMark, filePath);
    }

    @Override
    public void composeVideo(String waterMarkPath, String filePath) {
        Bitmap waterMark = BitmapFactory.decodeFile(waterMarkPath);
        composeVideo(waterMark, filePath);
    }

    /**
     * 合成影集视频
     *
     * @param
     * @return
     */
    public void composeVideo(Bitmap waterMark, String filePath) {
        mPhotoMoviePlayer.pause();

        final long startRecodTime = System.currentTimeMillis();
        final GLMovieRecorder recorder = new GLMovieRecorder(mActivity);
        final File file = new File(filePath);
        int bitrate = mGLTextureView.getWidth()
                * mGLTextureView.getHeight() > 1000 * 1500 ? 8000000 : 4000000;
        recorder.configOutput(mGLTextureView.getWidth(),
                mGLTextureView.getHeight(), bitrate, 30,
                1, file.getAbsolutePath());
        //生成一个全新的MovieRender，不然与现有的GL环境不一致，相互干扰容易出问题
        PhotoMovie newPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        //添加水印


        if (waterMark != null) {
            DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
            mMovieRenderer.setWaterMark(waterMark, new RectF(0,
                    0, waterMark.getWidth(), waterMark.getHeight()), 1f);
        }

        GLSurfaceMovieRenderer newMovieRenderer = new GLSurfaceMovieRenderer(mMovieRenderer);
        newMovieRenderer.setPhotoMovie(newPhotoMovie);
        String audioPath = null;
        if (mMusicUri != null) {
            audioPath = UriUtil.getPath(mActivity, mMusicUri);
        }
        if (!TextUtils.isEmpty(audioPath)) {
            if (Build.VERSION.SDK_INT < 18) {
                Toast.makeText(mActivity.getApplicationContext(),
                        "Mix audio needs api18!", Toast.LENGTH_LONG).show();
            } else {
                recorder.setMusic(audioPath);
            }
        }
        recorder.setDataSource(newMovieRenderer);
        recorder.startRecord(new GLMovieRecorder.OnRecordListener() {
            @Override
            public void onRecordFinish(boolean success) {
                File outputFile = file;
                long recordEndTime = System.currentTimeMillis();
                MLog.i("Record", "record:" + (recordEndTime - startRecodTime));
                //dialog.dismiss();
                if (success) {
                    if (mComposeProgressListener != null) {
                        mComposeProgressListener.onComposedComplete(file);
                    } else {
                        Toast.makeText(mActivity.getApplicationContext(), "Video save to path:" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(file)));


                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "video/*";
                        intent.setDataAndType(Uri.fromFile(outputFile), type);
                        mActivity.startActivity(intent);
                    }
                } else {
                    if (mComposeProgressListener != null) {
                        mComposeProgressListener.onComposedError(1);
                    }
                    Toast.makeText(mActivity.getApplicationContext(), "com.hw.photomovie.record error!", Toast.LENGTH_LONG).show();
                }
                if (recorder.getAudioRecordException() != null) {
                    if (mComposeProgressListener != null) {
                        mComposeProgressListener.onComposedError(0);
                    }
                    Toast.makeText(mActivity.getApplicationContext(), "record audio failed:" + recorder.getAudioRecordException().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRecordProgress(int recordedDuration, int totalDuration) {
                if (mComposeProgressListener != null) {
                    mComposeProgressListener.onComposedProgress((int) (recordedDuration / (float) totalDuration * 100));
                }
                //dialog.setProgress((int) (recordedDuration / (float) totalDuration * 100));
            }
        });
    }

    /**
     * 当前Activity启动另一个Activity时调用，外部紧接着调用GLTextureView对象的onPause()方法
     *
     * @param
     */
    @Override
    public void onPause() {
        if (mPhotoMoviePlayer != null) {
            mPhotoMoviePlayer.pause();
        }
    }

    /**
     * 返回当前Activity时调用，外部紧接着调用GLTextureView对象的onResume()方法
     *
     * @param
     */
    @Override
    public void onResume() {
        if (mMovieRenderer != null) {
            mMovieRenderer.releaseCoverSegment();
        }
        if (mPhotoMoviePlayer != null) {
            mPhotoMoviePlayer.start();
        }
    }

    @Override
    public void setProgressListener(PhotoMovieRenderer.OnComposeProgressListener lister) {
        mComposeProgressListener = lister;
    }

    /**
     * 回调合成视频当前的进度
     */
    public static interface OnComposeProgressListener {
        void onComposedProgress(int progress);

        void onComposedComplete(File file);

        void onComposedError(int error);
    }

    @Override
    public void onMovieUpdate(int elapsedTime) {
    }

    @Override
    public void onMovieStarted() {

    }

    @Override
    public void onMoviedPaused() {

    }

    @Override
    public void onMovieResumed() {

    }

    @Override
    public void onMovieEnd() {

    }
}
