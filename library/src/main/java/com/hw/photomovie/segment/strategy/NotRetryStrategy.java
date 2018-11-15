package com.hw.photomovie.segment.strategy;

import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.segment.MovieSegment;

import java.util.List;


public class NotRetryStrategy implements RetryStrategy {
    @Override
    public List<PhotoData> getAvailableData(PhotoMovie photoMovie, MovieSegment movieSegment) {
        return movieSegment==null?null:movieSegment.getAllocatedPhotos();
    }
}
