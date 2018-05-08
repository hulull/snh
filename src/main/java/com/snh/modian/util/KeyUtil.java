package com.snh.modian.util;

public class KeyUtil {
    public static<T> String generateKey(String prefix, T t) {
        return prefix + t;
    }
}
