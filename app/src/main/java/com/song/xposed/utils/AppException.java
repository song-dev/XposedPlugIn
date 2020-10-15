package com.song.xposed.utils;

public class AppException extends RuntimeException {
    public AppException() {
    }

    public AppException(String msg) {
        super(msg);
    }

    public AppException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }
}