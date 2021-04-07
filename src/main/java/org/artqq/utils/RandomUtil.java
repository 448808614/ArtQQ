package org.artqq.utils;
import java.util.Random;

public class RandomUtil {
    public static int randomInt(int i, int i2) {
        return new Random().nextInt((i2 - i) + 1) + i;
    }

    public static long randomLong(long j, long j2) {
        return ((long) new Random().nextInt((int) ((j2 - j) + ((long) 1)))) + j;
    }
}
