package com.menufi.backend.springboot;

public class RestUtil {
    public static String parseAuthHeader(String authRaw) {
        String[] authStrings = authRaw.split("\\s+");
        if (authStrings.length == 2 && authStrings[0].equals("MenuFi")) {
            return authStrings[1];
        }
        return null;
    }
}
