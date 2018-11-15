package com.hw.photomovie.segment.strategy;

import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.segment.MovieSegment;

import java.util.List;


public interface RetryStrategy {
    List<PhotoData> getAvailableData(PhotoMovie photoMovie, MovieSegment movieSegment);
}
