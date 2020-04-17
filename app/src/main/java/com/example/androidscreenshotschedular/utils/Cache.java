package com.example.androidscreenshotschedular.utils;

import java.util.ArrayList;
import java.util.List;

public class Cache<T> {
    private List<CacheElement> cacheElements;
    private final int cacheSize;

    public Cache(int cacheSize) {
        this.cacheSize = cacheSize;
        cacheElements = new ArrayList<>();
        for (int i = 0; i < cacheSize; i++)
            cacheElements.add(null);
    }

    public boolean isDataFoundAt(int actualPosition) {
        int relativeCachePosition = getCacheRelativePosition(actualPosition);
        return cacheElements.get(relativeCachePosition) != null && cacheElements.get(relativeCachePosition).getActualPosition() == actualPosition;
    }

    public int getCacheRelativePosition(int position) {
        return position % cacheSize;
    }

    public T getDataAt(int position) {
        return cacheElements.get(getCacheRelativePosition(position)).getElementData();
    }

    public void setDataAt(int position, T data) {
        cacheElements.set(getCacheRelativePosition(position), new CacheElement(position, data));
    }

    private class CacheElement {
        private int actualPosition;
        private T elementData;

        public CacheElement(int actualPosition, T elementData) {
            this.actualPosition = actualPosition;
            this.elementData = elementData;
        }

        public int getActualPosition() {
            return actualPosition;
        }

        public T getElementData() {
            return elementData;
        }
    }
}
