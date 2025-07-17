package com.tugalsan.api.captcha.server.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import com.tugalsan.api.list.client.*;

/**
 * @author nyilmaz
 */
public class TS_CaptchaRendererAligned implements TS_CaptchaRenderer {
    
    

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
    public TS_CaptchaRendererAligned() {
        this(DEFAULT_COLOR(), DEFAULT_FONTS());
    }

    public TS_CaptchaRendererAligned(Color color, java.util.List<Font> fonts) {
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

        var textWidth = 0;
        var wc = word.toCharArray();
        var generator = new Random();

        for (var elem : wc) {
            var itchar = new char[]{elem};
            var choiceFont = generator.nextInt(_fonts.size());
            var itFont = _fonts.get(choiceFont);
            var gv = itFont.createGlyphVector(frc, itchar);
            var charWitdth = gv.getVisualBounds().getWidth();
            textWidth += charWitdth;
        }

        var startPosX = (image.getWidth() - textWidth) / 2;
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
