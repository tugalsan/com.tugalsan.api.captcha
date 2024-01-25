package com.tugalsan.api.captcha.server.bg;

import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

/**
 * Generates a transparent background.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaBGTransparent implements TS_CaptchaBG {

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        return getBackground(image.getWidth(), image.getHeight());
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        var bg = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        var g = bg.createGraphics();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        g.fillRect(0, 0, width, height);

        return bg;
    }

}
