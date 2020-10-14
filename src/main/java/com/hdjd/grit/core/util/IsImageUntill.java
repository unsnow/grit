package com.hdjd.grit.core.util;


public class IsImageUntill {
    public static boolean isImage(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if ("jpg".equals(extension) || "png".equals(extension) || "jpeg".equals(extension)) {
            return true;
        }
        return false;
    }
}
