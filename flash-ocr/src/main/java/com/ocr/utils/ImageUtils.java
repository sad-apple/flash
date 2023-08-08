package com.ocr.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.ndarray.NDArray;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUtils {

    /**
     * 保存BufferedImage图片
     *
     * @author Calvin
     */
    public static void saveImage(BufferedImage img, String name, String path) {
        Image djlImg = ImageFactory.getInstance().fromImage(img); // 支持多种图片格式，自动适配
        Path outputDir = Paths.get(path);
        Path imagePath = outputDir.resolve(name);
        // OpenJDK 不能保存 jpg 图片的 alpha channel
        try {
            djlImg.save(Files.newOutputStream(imagePath), "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存DJL图片
     *
     * @author Calvin
     */
    public static void saveImage(Image img, String name, String path) {
        Path outputDir = Paths.get(path);
        Path imagePath = outputDir.resolve(name);
        // OpenJDK 不能保存 jpg 图片的 alpha channel
        try {
            img.save(Files.newOutputStream(imagePath), "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片,含检测框
     *
     * @author Calvin
     */
    public static void saveBoundingBoxImage(
            Image img, DetectedObjects detection, String name, String path) throws IOException {
        // Make image copy with alpha channel because original image was jpg
        img.drawBoundingBoxes(detection);
        Path outputDir = Paths.get(path);
        Files.createDirectories(outputDir);
        Path imagePath = outputDir.resolve(name);
        // OpenJDK can't save jpg with alpha channel
        img.save(Files.newOutputStream(imagePath), "png");
    }

    /**
     * 画检测框(有倾斜角)
     *
     * @author Calvin
     */
    public static void drawImageRect(BufferedImage image, NDArray box) {
        float[] points = box.toFloatArray();
        int[] xPoints = new int[5];
        int[] yPoints = new int[5];

        for (int i = 0; i < 4; i++) {
            xPoints[i] = (int) points[2 * i];
            yPoints[i] = (int) points[2 * i + 1];
        }
        xPoints[4] = xPoints[0];
        yPoints[4] = yPoints[0];

        // 将绘制图像转换为Graphics2D
        Graphics2D g = (Graphics2D) image.getGraphics();
        try {
            g.setColor(new Color(0, 255, 0));
            // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
            BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
            g.setStroke(bStroke);
            g.drawPolyline(xPoints, yPoints, 5); // xPoints, yPoints, nPoints
        } finally {
            g.dispose();
        }
    }

    /**
     * 画检测框(有倾斜角)和文本
     *
     * @author Calvin
     */
    public static void drawImageRectWithText(BufferedImage image, NDArray box, String text) {
        float[] points = box.toFloatArray();
        int[] xPoints = new int[5];
        int[] yPoints = new int[5];

        for (int i = 0; i < 4; i++) {
            xPoints[i] = (int) points[2 * i];
            yPoints[i] = (int) points[2 * i + 1];
        }
        xPoints[4] = xPoints[0];
        yPoints[4] = yPoints[0];

        // 将绘制图像转换为Graphics2D
        Graphics2D g = (Graphics2D) image.getGraphics();
        try {
            int fontSize = 32;
            Font font = new Font("楷体", Font.PLAIN, fontSize);
            g.setFont(font);
            g.setColor(new Color(0, 0, 255));
            // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
            BasicStroke bStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
            g.setStroke(bStroke);
            g.drawPolyline(xPoints, yPoints, 5); // xPoints, yPoints, nPoints
            g.drawString(text, xPoints[0], yPoints[0]);
        } finally {
            g.dispose();
        }
    }

    /**
     * 画检测框
     *
     * @author Calvin
     */
    public static void drawImageRect(BufferedImage image, int x, int y, int width, int height) {
        // 将绘制图像转换为Graphics2D
        Graphics2D g = (Graphics2D) image.getGraphics();
        try {
            g.setColor(new Color(0, 255, 0));
            // 声明画笔属性 ：粗 细（单位像素）末端无修饰 折线处呈尖角
            BasicStroke bStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
            g.setStroke(bStroke);
            g.drawRect(x, y, width, height);
        } finally {
            g.dispose();
        }
    }

    /**
     * 显示文字
     *
     * @author Calvin
     */
    public static void drawImageText(BufferedImage image, String text, int x, int y) {
        Graphics graphics = image.getGraphics();
        int fontSize = 32;
        Font font = new Font("楷体", Font.PLAIN, fontSize);
        try {
            graphics.setFont(font);
            graphics.setColor(new Color(0, 0, 255));
            int strWidth = graphics.getFontMetrics().stringWidth(text);
            graphics.drawString(text, x, y);
        } finally {
            graphics.dispose();
        }
    }
}