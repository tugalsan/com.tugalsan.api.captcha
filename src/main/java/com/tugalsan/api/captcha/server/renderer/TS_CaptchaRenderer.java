package com.tugalsan.api.captcha.server.renderer;

import java.awt.image.BufferedImage;

public interface TS_CaptchaRenderer {
    /**
     * Render a word to a BufferedImage.
     * 
     * @param word
     *            The word to be rendered.
     * @param width
     *            The width of the image to be created.
     * @param height
     *            The height of the image to be created.
     * @return The BufferedImage created from the word.
     */
    public void render(String word, BufferedImage image);

}
