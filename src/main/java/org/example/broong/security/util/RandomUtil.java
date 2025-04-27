package org.example.broong.security.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomUtil {

    private static String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghizklmnopqrstuvwxyz0123456789";

    private static SecureRandom random = new SecureRandom();


    public static String generatRandowPassword(){
        StringBuilder sb = new StringBuilder(10);
        for(int i = 0; i < sb.length(); i++){
            sb.append(DATA.charAt(random.nextInt(DATA.length())));
        }
        return sb.toString();
    }
}
