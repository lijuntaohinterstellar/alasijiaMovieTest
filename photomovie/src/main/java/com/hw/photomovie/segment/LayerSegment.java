package com.hw.photomovie.segment;

import com.hw.photomovie.segment.layer.MovieLayer;


public class LayerSegment extends AbsLayerSegment {

    public LayerSegment(MovieLayer[] layers, int duration) {
        super(duration);
        mLayers = layers;
    }

    @Override
    protected MovieLayer[] initLayers() {
        return mLayers;
    }
}
