package me.avenzu.azura.util.math;

import java.util.Random;

public class MathUtil {

    public static final Random RANDOM = new Random();

    //Computes the magnitude of X and Y
    public static double getMagnitude(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
