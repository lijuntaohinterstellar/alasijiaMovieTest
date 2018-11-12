package com.hw.photomovie.filter;

import com.hw.photomovie.moviefilter.CameoMovieFilter;
import com.hw.photomovie.moviefilter.GrayMovieFilter;
import com.hw.photomovie.moviefilter.IMovieFilter;
import com.hw.photomovie.moviefilter.KuwaharaMovieFilter;
import com.hw.photomovie.moviefilter.LutMovieFilter;
import com.hw.photomovie.moviefilter.SnowMovieFilter;

public class FilterItem {

    public IMovieFilter initFilter(FilterType type) {
        switch (type) {
            case GRAY:
                return new GrayMovieFilter();
            case SNOW:
                return new SnowMovieFilter();
            case CAMEO:
                return new CameoMovieFilter();
            case KUWAHARA:
                return new KuwaharaMovieFilter();
            case LUT1:
                return new LutMovieFilter(LutMovieFilter.LutType.A);
            case LUT2:
                return new LutMovieFilter(LutMovieFilter.LutType.B);
            case LUT3:
                return new LutMovieFilter(LutMovieFilter.LutType.C);
            case LUT4:
                return new LutMovieFilter(LutMovieFilter.LutType.D);
            case LUT5:
                return new LutMovieFilter(LutMovieFilter.LutType.E);
            case NONE:
            default:
                return null;
        }
    }
}
