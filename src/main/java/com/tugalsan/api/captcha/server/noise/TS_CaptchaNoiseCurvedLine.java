package com.tugalsan.api.captcha.server.noise;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Adds a randomly curved line to the image.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaNoiseCurvedLine implements TS_CaptchaNoise {

    private static final Random RAND = new SecureRandom();

    private final Color _color;
    private final float _width;

    public TS_CaptchaNoiseCurvedLine() {
        this(Color.BLACK, 3.0f);
    }

    public TS_CaptchaNoiseCurvedLine(Color color, float width) {
        _color = color;
        _width = width;
    }

    @Override
    public void makeNoise(BufferedImage image) {
        var width = image.getWidth();
        var height = image.getHeight();

        // the curve from where the points are taken
        var cc = new CubicCurve2D.Float(width * .1f, height
                * RAND.nextFloat(), width * .1f, height
                * RAND.nextFloat(), width * .25f, height
                * RAND.nextFloat(), width * .9f, height
                * RAND.nextFloat());

        // creates an iterator to define the boundary of the flattened curve
        var pi = cc.getPathIterator(null, 2);
        var tmp = new Point2D[200];
        var i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            var coords = new float[6];
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO ->
                    tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        // the points where the line changes the stroke and direction
        var pts = new Point2D[i];
        // copies points from tmp to pts
        System.arraycopy(tmp, 0, pts, 0, i);

        var graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        graph.setColor(_color);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(_width));
            }
            graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
                    (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
        }

        graph.dispose();
    }
}
