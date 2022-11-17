//package com.ironsoftware.ironpdf.internal.staticapi;
//
//import org.junit.jupiter.api.Assertions;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.util.Iterator;
//
//public class TestBase {
//
//    public TestBase() {
//        Setting_Api.enableDebug = true;
//        Setting_Api.ironPdfEngineFolder = Paths.get("TestOutput/");
//    }
//
//    public static Color GetAvgColor(byte[] imageBytes) throws IOException {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
//
//        java.awt.image.BufferedImage bi = ImageIO.read(byteArrayInputStream);
//        long sumR = 0, sumG = 0, sumB = 0, sumA = 0;
//        int width = bi.getWidth();
//        int height = bi.getHeight();
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                Color pixel = new Color(bi.getRGB(x, y));
//                sumR += pixel.getRed();
//                sumG += pixel.getGreen();
//                sumB += pixel.getBlue();
//                sumA += pixel.getAlpha();
//            }
//        }
//        int num = width * height;
//        return new Color((int) sumR / num, (int) sumG / num, (int) sumB / num, (int) sumA / num);
//    }
//
//    public static <T> void AssertNotNullOrEmpty(T[] array) {
//        Assertions.assertTrue(array != null && array.length != 0);
//    }
//
//    public static void AssertNotNullOrEmpty(byte[] array) {
//        Assertions.assertTrue(array != null && array.length != 0);
//    }
//
//    public static void AssertNotNullOrEmpty(String s) {
//        Assertions.assertTrue(s != null && !s.isEmpty());
//    }
//
//    public static void AssertNullOrEmpty(String s) {
//        Assertions.assertTrue(s == null || s.isEmpty());
//    }
//
//    public static <T> void AssertNullOrEmpty(Iterator<T> s) {
//        Assertions.assertTrue(s == null || !s.hasNext());
//    }
//
//    public static <T> void AssertNullOrEmpty(java.util.List<T> s) {
//        Assertions.assertTrue(s == null || s.isEmpty());
//    }
//
//    public String getTestFile(String name) {
//        return Paths.get("src", "test", "resources", name).toString();
//    }
//}
