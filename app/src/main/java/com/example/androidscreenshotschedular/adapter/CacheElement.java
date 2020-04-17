package com.example.androidscreenshotschedular.adapter;

public class CacheElement<T> {
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
