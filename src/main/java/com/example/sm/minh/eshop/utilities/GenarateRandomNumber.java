package com.example.sm.minh.eshop.utilities;

import java.util.Random;

public class GenarateRandomNumber {
    public static final int LENGTH_TOKEN = 6;
    public static String generateRandomNumberString() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(LENGTH_TOKEN);
        for (int i = 0; i < LENGTH_TOKEN; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
