package com.poly.Recruitment.util;

import java.util.Random;

public class Common {

    public static String randomCodeMail() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

}
