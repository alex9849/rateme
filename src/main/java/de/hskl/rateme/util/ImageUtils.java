package de.hskl.rateme.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static BufferedImage readBase64Image(String b64Image) throws IOException {
        String[] parts = b64Image.split(",", 2);
        byte[] imgBytes = Base64.getDecoder().decode(parts[1]);
        ByteArrayInputStream bis = new ByteArrayInputStream(imgBytes);
        return ImageIO.read(bis);
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int hight) {
        BufferedImage resized = new BufferedImage(width, hight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(image, 0, 0, width, width, null);
        graphics.dispose();
        return resized;
    }

    public static String toBase64Image(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }
}
