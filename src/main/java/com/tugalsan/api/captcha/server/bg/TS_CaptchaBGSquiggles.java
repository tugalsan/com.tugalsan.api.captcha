package com.tugalsan.api.captcha.server.bg;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

/**
 * Adds a black and white squiggly background to the image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaBGSquiggles implements TS_CaptchaBG {

    @Override
    public BufferedImage addBackground(BufferedImage image) {
        var width = image.getWidth();
        var height = image.getHeight();
        return getBackground(width, height);
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        var result = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        var graphics = result.createGraphics();

        var bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        graphics.setStroke(bs);
        var ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                0.75f);
        graphics.setComposite(ac);

        graphics.translate(width * -1.0, 0.0);
        var delta = 5.0;
//        double ts = 0.0;
        for (var xt = 0.0; xt < (2.0 * width); xt += delta) {
            var arc = new Arc2D.Double(0, 0, width, height, 0.0, 360.0,
                    Arc2D.OPEN);
            graphics.draw(arc);
            graphics.translate(delta, 0.0);
//            ts += delta;
        }
        graphics.dispose();
        return result;
    }
}
