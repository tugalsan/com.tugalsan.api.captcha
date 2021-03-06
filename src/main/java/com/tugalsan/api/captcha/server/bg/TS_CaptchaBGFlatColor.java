package com.tugalsan.api.captcha.server.bg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class TS_CaptchaBGFlatColor implements TS_CaptchaBG {

    private Color _color = Color.GRAY;

    public TS_CaptchaBGFlatColor() {
        this(Color.GRAY);
    }

    public TS_CaptchaBGFlatColor(Color color) {
        _color = color;
    }

    public BufferedImage addBackground(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        return this.getBackground(width, height);
    }

    public BufferedImage getBackground(int width, int height) {
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(_color);
        graphics.fill(new Rectangle2D.Double(0, 0, width, height));
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return img;
    }
}
