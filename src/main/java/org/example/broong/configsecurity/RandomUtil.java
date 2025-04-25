package org.example.broong.configsecurity;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomUtil {

    private static String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghizklmnopqrstuvwxyz0123456789";

    private static SecureRandom random = new SecureRandom();

    @Autowired
    public static String RandowPassword(){
        StringBuilder sb = new StringBuilder(10);
        for(int i = 0; i < sb.length(); i++){
            sb.append(DATA.charAt(random.nextInt(DATA.length())));
        }
        return sb.toString();
    }
}
