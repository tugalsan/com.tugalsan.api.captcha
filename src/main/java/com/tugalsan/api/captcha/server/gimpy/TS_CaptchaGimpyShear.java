package com.tugalsan.api.captcha.server.gimpy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

public class TS_CaptchaGimpyShear implements TS_CaptchaGimpy {

    private final Random _gen = new SecureRandom();
    private final Color _color;

    public TS_CaptchaGimpyShear() {
        this(Color.GRAY);
    }

    public TS_CaptchaGimpyShear(Color color) {
        _color = color;
    }

    @Override
    public void gimp(BufferedImage bi) {
        var g = bi.createGraphics();
        shearX(g, bi.getWidth(), bi.getHeight());
        shearY(g, bi.getWidth(), bi.getHeight());
        g.dispose();
    }

    private void shearX(Graphics2D g, int w1, int h1) {
        var period = _gen.nextInt(10) + 5;

        var borderGap = true;
        var frames = 15;
        var phase = _gen.nextInt(5) + 2;

        for (var i = 0; i < h1; i++) {
            var d = (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * phase) / frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(_color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }

    private void shearY(Graphics2D g, int w1, int h1) {
        var period = _gen.nextInt(30) + 10; // 50;

        var borderGap = true;
        var frames = 15;
        var phase = 7;
        for (var i = 0; i < w1; i++) {
            var d = (period >> 1)
                    * Math.sin((float) i / period
                            + (6.2831853071795862D * phase) / frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(_color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
}
