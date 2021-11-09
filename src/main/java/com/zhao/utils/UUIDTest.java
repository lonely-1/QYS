package com.zhao.utils;

import java.util.UUID;

public class UUIDTest {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
