package xyz.ll.life.lab;

import java.awt.*;
import java.util.Random;

/**
 * Created by lucas-cleto on 2/14/16.
 */
public class Utils {

    public static int[] shuffleArray(int[] array, Random random) {
        int index;
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }

    public static Color color(double val) {
        double H = val;
        double S = 1d;
        double B = 1d;
        int rgb = Color.HSBtoRGB((float)H, (float)S, (float)B);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        Color color = new Color(red, green, blue, 60);
        return color;
    }
}
