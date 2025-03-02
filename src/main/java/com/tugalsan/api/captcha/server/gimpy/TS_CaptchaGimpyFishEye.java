package com.tugalsan.api.captcha.server.gimpy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Overlays a warped grid to the image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaGimpyFishEye implements TS_CaptchaGimpy {

    private final Color _hColor;
    private final Color _vColor;

    public TS_CaptchaGimpyFishEye() {
        this(Color.BLACK, Color.BLACK);
    }

    public TS_CaptchaGimpyFishEye(Color hColor, Color vColor) {
        _hColor = hColor;
        _vColor = vColor;
    }

    @Override
    public void gimp(BufferedImage image) {
        var height = image.getHeight();
        var width = image.getWidth();

        var hstripes = height / 7;
        var vstripes = width / 7;

        // Calculate space between lines
        var hspace = height / (hstripes + 1);
        var vspace = width / (vstripes + 1);

        var graph = (Graphics2D) image.getGraphics();
        // Draw the horizontal stripes
        for (var i = hspace; i < height; i = i + hspace) {
            graph.setColor(_hColor);
            graph.drawLine(0, i, width, i);
        }

        // Draw the vertical stripes
        for (var i = vspace; i < width; i = i + vspace) {
            graph.setColor(_vColor);
            graph.drawLine(i, 0, i, height);
        }

        // Create a pixel array of the original image.
        // we need this later to do the operations on..
        var pix = new int[height * width];
        var j = 0;

        for (var j1 = 0; j1 < width; j1++) {
            for (var k1 = 0; k1 < height; k1++) {
                pix[j] = image.getRGB(j1, k1);
                j++;
            }
        }

        double distance = ranInt(width / 4, width / 3);

        // put the distortion in the (dead) middle
        var wMid = image.getWidth() / 2;
        var hMid = image.getHeight() / 2;

        // again iterate over all pixels..
        for (var x = 0; x < image.getWidth(); x++) {
            for (var y = 0; y < image.getHeight(); y++) {

                var relX = x - wMid;
                var relY = y - hMid;

                var tmp = relX * relX + relY * relY;
                var d1 = Math.sqrt(tmp);
                if (d1 < distance) {

                    var j2 = wMid
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (x - wMid));
                    var k2 = hMid
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (y - hMid));
                    image.setRGB(x, y, pix[j2 * height + k2]);
                }
            }
        }

        graph.dispose();
    }

    private int ranInt(int i, int j) {
        var d = Math.random();
        return (int) (i + ((j - i) + 1) * d);
    }

    private double fishEyeFormula(double s) {
        // implementation of:
        // g(s) = - (3/4)s3 + (3/2)s2 + (1/4)s, with s from 0 to 1.
        if (s < 0.0D) {
            return 0.0D;
        }
        if (s > 1.0D) {
            return s;
        }

        return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
}
