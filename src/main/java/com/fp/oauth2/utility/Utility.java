package com.fp.oauth2.utility;

import com.google.gson.Gson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.mariadb.jdbc.internal.com.send.authentication.ed25519.Utils.bytesToHex;

public class Utility {
    public static String objectToString(Object o) {
        Gson g = new Gson();
        String json = g.toJson(o);
        return json;
    }

    public static Object jsonToObject(String json) {
        Gson g = new Gson();
        Object o = g.fromJson(json, Object.class);
        return o;
    }

    public static String getUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null)
            return "";
        return authentication.getName();
    }

    public static boolean checkPasswordWithTimestamp(String passFromDb, String passRequest, String timeStamp) {
        boolean returnValue = false;
        try {
            MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");
            byte[] baPasswdHashdb = mdSHA256.digest((timeStamp + passFromDb).getBytes(StandardCharsets.UTF_8));
            String strPasswdHashdb = bytesToHex(baPasswdHashdb).toLowerCase();

            if (passRequest.equals(strPasswdHashdb)) returnValue = true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public static Map toMap(Throwable map) {
        Map<String, String> newMap = new HashMap<>();
        if (map != null) {
            String[] aArr = (map.toString()).split(",");
            for (String strA : aArr) {
                String[] bArr = strA.split("=");
                for (String strB : bArr) {
                    newMap.put(bArr[0], strB.trim());
                }
            }
        } else {
            newMap.put("error", "");
            newMap.put("error_description", "");
        }
        return newMap;
    }

    public static Set setString(String word, String delimiter){
        return new HashSet<String>(Arrays.asList(word.split(delimiter)));
    }
}
