package dev.lemonnik.fern_config.utils;

public class ColorUtils {
    // 0xRRGGBB → [R, G, B]
    public static int[] toRGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new int[]{r, g, b};
    }

    // 0xAARRGGBB → [R, G, B, A]
    public static int[] toRGBA(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new int[]{r, g, b, a};
    }

    // [R, G, B] → 0xRRGGBB
    public static int fromRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    // [R, G, B, A] → 0xAARRGGBB
    public static int fromRGBA(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}