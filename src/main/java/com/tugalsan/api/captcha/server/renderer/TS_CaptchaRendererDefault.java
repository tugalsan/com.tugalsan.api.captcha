package com.tugalsan.api.captcha.server.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.*;
import com.tugalsan.api.list.client.*;

/**
 * Render a word using the given color and from a randomly selected font chosen
 * from a list of fonts given in the constructor.
 */
public class TS_CaptchaRendererDefault implements TS_CaptchaRenderer {

    private static Color DEFAULT_COLOR() {
        return Color.BLACK;
    }

    private static List<Font> DEFAULT_FONTS() {
        return TGS_ListUtils.of(
                new Font("Arial", Font.BOLD, 40),
                new Font("Courier", Font.BOLD, 40)
        );
    }

    private final Color _color;
    private final List<Font> _fonts;

    /**
     * Will render the characters in black and in either 40pt Arial or Courier.
     */
    public TS_CaptchaRendererDefault() {
        this(DEFAULT_COLOR(), DEFAULT_FONTS());
    }

    public TS_CaptchaRendererDefault(Color color, List<Font> fonts) {
        _color = color != null ? color : DEFAULT_COLOR();
        _fonts = fonts != null ? fonts : DEFAULT_FONTS();
    }

    /**
     * Render a word onto a BufferedImage.
     *
     * @param word The word to be rendered.
     * @param image The BufferedImage onto which the word will be painted on to
     */
    @Override
    public void render(String word, BufferedImage image) {
        var g = image.createGraphics();

        var hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        hints.add(new RenderingHints(
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        ));
        g.setRenderingHints(hints);
        g.setColor(_color);
        var frc = g.getFontRenderContext();

        var startPosX = image.getWidth() / 10;
        var wc = word.toCharArray();
        var generator = new Random();
        for (var element : wc) {
            var itchar = new char[]{element};
            var choiceFont = generator.nextInt(_fonts.size());
            var itFont = _fonts.get(choiceFont);
            g.setFont(itFont);

            var gv = itFont.createGlyphVector(frc, itchar);
            var charWitdth = gv.getVisualBounds().getWidth();

            g.drawChars(itchar, 0, itchar.length, startPosX, (int) (image.getHeight() + gv.getVisualBounds().getHeight()) / 2);
            startPosX = startPosX + (int) charWitdth + 1;
        }
    }
}
