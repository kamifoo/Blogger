package me.kamikid.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String code(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder stringBuilder = new StringBuilder("");
            for (byte b : byteDigest) {
                i = b;
                if (i < 0) {
                    i += 256;
                } else if (i < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(i));
            }
            return stringBuilder.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        System.out.println(MD5Utils.code("okkid1997"));
    }
}
