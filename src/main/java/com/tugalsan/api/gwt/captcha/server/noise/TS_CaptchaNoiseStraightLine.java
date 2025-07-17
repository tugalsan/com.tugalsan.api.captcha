package com.tugalsan.api.gwt.captcha.server.noise;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

/**
 * Draws a 4-pixel thick straight red line through the given image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaNoiseStraightLine implements TS_CaptchaNoise {

    private final Color _color;
    private final int _thickness;
    private final SecureRandom _gen = new SecureRandom();

    /**
     * Default constructor creates a 4-pixel wide red line.
     */
    public TS_CaptchaNoiseStraightLine() {
        this(Color.BLACK, 4);
    }

    public TS_CaptchaNoiseStraightLine(Color color, int thickness) {
        _color = color;
        _thickness = thickness;
    }

    @Override
    public void makeNoise(BufferedImage image) {
        var graphics = image.createGraphics();
        var height = image.getHeight();
        var width = image.getWidth();
        var y1 = _gen.nextInt(height) + 1;
        var y2 = _gen.nextInt(height) + 1;
        drawLine(graphics, y1, width, y2);
    }

    private void drawLine(Graphics g, int y1, int x2, int y2) {
        var X1 = 0;

        // The thick line is in fact a filled polygon
        g.setColor(_color);
        var dX = x2 - X1;
        var dY = y2 - y1;
        // line length
        var tmp = dX * dX + dY * dY;
        var lineLength = Math.sqrt(tmp);

        var scale = _thickness / (2 * lineLength);

        // The x and y increments from an endpoint needed to create a
        // rectangle...
        var ddx = -scale * dY;
        var ddy = scale * dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        var dx = (int) ddx;
        var dy = (int) ddy;

        // Now we can compute the corner points...
        var xPoints = new int[4];
        var yPoints = new int[4];

        xPoints[0] = X1 + dx;
        yPoints[0] = y1 + dy;
        xPoints[1] = X1 - dx;
        yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx;
        yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx;
        yPoints[3] = y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
    }
}
