package ru.skillbox.diplom.group32.social.service.service.auth.captcha;

import ru.skillbox.diplom.group32.social.service.model.auth.captcha.Captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaBuilder {

    private BufferedImage image;
    private Graphics2D graphics;
    private Captcha captcha;

    private CaptchaBuilder() {
        this.captcha = new Captcha();
    }

    public static CaptchaBuilder getBuilder() {
        return new CaptchaBuilder();
    }

    public CaptchaBuilder createImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, width, height);
        return this;
    }

    public CaptchaBuilder setText(int lineBeginX, int lineBeginY, int fontSize) {
        return setText(lineBeginX, lineBeginY, fontSize, null);
    }

    public CaptchaBuilder setText(int lineBeginX, int lineBeginY, int fontSize, String line) {

        if (line == null || line.isEmpty() || line.isBlank()) {
            line = randomLine();
        }
        captcha.setCode(line);

        Font myFont = new Font("Georgia", Font.ITALIC, fontSize);
        graphics.setFont(myFont);
        graphics.setColor(Color.BLACK);
        graphics.drawString(line, lineBeginX, lineBeginY + fontSize);

        return this;
    }

    public CaptchaBuilder setLines(int lineCount) {

        Random random = new Random();

        for (int i = 0; i < lineCount; i++) {
            int x1 = random.nextInt(-500, 0);
            int y1 = random.nextInt(-500, 500);
            int x2 = random.nextInt(image.getWidth(), 500);
            int y2 = random.nextInt(-500, 500);

            graphics.drawLine(x1, y1, x2, y2);
        }
        return this;
    }

    public Captcha build() {
        captcha.setImage(image);
        return captcha;
    }


    private static String randomLine() {
        Random random = new Random();
        Integer number = random.nextInt(1000, 2000);
        String line = number.toString();
        char[] array = line.toCharArray();
        array[0] = array[2];
        return String.valueOf(array);
    }
}
