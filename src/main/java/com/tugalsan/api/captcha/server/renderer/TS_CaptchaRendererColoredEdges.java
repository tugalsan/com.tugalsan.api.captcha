package com.tugalsan.api.captcha.server.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.text.AttributedString;
import java.util.*;
import com.tugalsan.api.list.client.*;

/**
 * Creates an outlined version of the answer using the given colors and fonts.
 */
public class TS_CaptchaRendererColoredEdges implements TS_CaptchaRenderer {

    private static List<Color> DEFAULT_COLORS() {
        return TGS_ListUtils.of(
                Color.BLUE
        );
    }

    private static List<Font> DEFAULT_FONTS() {
        return TGS_ListUtils.of(
                new Font("Arial", Font.BOLD, 40)
        );
    }

    private static float DEFAULT_STROKE_WIDTH() {
        return 0.5f;
    }
    // The text will be rendered 25%/5% of the image height/width from the X and Y axes

    private static double YOFFSET() {
        return 0.25;
    }

    private static double XOFFSET() {
        return 0.05;
    }

    private final List<Font> _fonts;
    private final List<Color> _colors;
    private final float _strokeWidth;

    public TS_CaptchaRendererColoredEdges() {
        this(DEFAULT_COLORS(), DEFAULT_FONTS(), DEFAULT_STROKE_WIDTH());
    }

    public TS_CaptchaRendererColoredEdges(List<Color> colors, List<Font> fonts) {
        this(colors, fonts, DEFAULT_STROKE_WIDTH());
    }

    public TS_CaptchaRendererColoredEdges(List<Color> colors, List<Font> fonts, float strokeWidth) {
        _colors = colors != null ? colors : DEFAULT_COLORS();
        _fonts = fonts != null ? fonts : DEFAULT_FONTS();
        _strokeWidth = strokeWidth < 0 ? DEFAULT_STROKE_WIDTH() : strokeWidth;
    }

    public void render(final String word, BufferedImage image) {
        var g = image.createGraphics();

        var hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        hints.add(new RenderingHints(
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        ));
        g.setRenderingHints(hints);

        var as = new AttributedString(word);
        as.addAttribute(TextAttribute.FONT, getRandomFont());

        var frc = g.getFontRenderContext();
        var aci = as.getIterator();

        var tl = new TextLayout(aci, frc);
        var xBaseline = (int) Math.round(image.getWidth() * XOFFSET());
        var yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * YOFFSET());
        var shape = tl.getOutline(AffineTransform.getTranslateInstance(xBaseline, yBaseline));

        g.setColor(getRandomColor());
        g.setStroke(new BasicStroke(_strokeWidth));

        g.draw(shape);
    }

    private Color getRandomColor() {
        return (Color) getRandomObject(_colors);
    }

    private Font getRandomFont() {
        return (Font) getRandomObject(_fonts);
    }

    private Object getRandomObject(List<? extends Object> objs) {
        if (objs.size() == 1) {
            return objs.get(0);
        }

        var gen = new SecureRandom();
        var i = gen.nextInt(objs.size());
        return objs.get(i);
    }
}
