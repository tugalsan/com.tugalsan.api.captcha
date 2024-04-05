package com.tugalsan.api.captcha.server;

import com.tugalsan.api.log.server.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import com.tugalsan.api.captcha.server.renderer.TS_CaptchaRenderer;
import com.tugalsan.api.random.server.*;
import com.tugalsan.api.captcha.server.renderer.*;
import com.tugalsan.api.captcha.server.bg.*;
import com.tugalsan.api.captcha.server.gimpy.*;
import com.tugalsan.api.captcha.server.noise.*;
import com.tugalsan.api.captcha.server.text.*;

public final class TS_Captcha implements Serializable {

    final private static TS_Log d = TS_Log.of(TS_Captcha.class);

    private final Builder _builder;

    private TS_Captcha(Builder builder) {
        _builder = builder;
    }

    public static class Builder implements Serializable {

        private String _answer = "";
        private BufferedImage _img;
        private BufferedImage _bg;
        private boolean _addBorder = false;

        public Builder() {
            this(200, 50);
        }

        public Builder(int width, int height) {
            _img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        public Builder addBackground(TS_CaptchaBG bgProd) {
            _bg = bgProd.getBackground(_img.getWidth(), _img.getHeight());
            return this;
        }

        public Builder addBackgroundPreffered() {
            var rndBg = TS_RandomUtils.nextInt(0, 1);
            d.ci("prefBg", rndBg);
            return addBackgroundIdx(rndBg);
        }

        public Builder addBackgroundRandom() {
            var rndBg = TS_RandomUtils.nextInt(0, 3);
            d.ci("rndBg", rndBg);
            return addBackgroundIdx(rndBg);
        }

        public Builder addBackgroundIdx(int bgIdx_0_3) {
            switch (bgIdx_0_3) {
                case 0:
                    addBackground(new TS_CaptchaBGFlatColor());
                    break;
                case 1:
                    addBackground(new TS_CaptchaBGGradiated());
                    break;
                case 2:
                    addBackground(new TS_CaptchaBGSquiggles());
                    break;
                case 3:
                default:
                    addBackground(new TS_CaptchaBGTransparent());
            }
            return this;
        }

        public Builder addText(TS_CaptchaText txtProd, TS_CaptchaRenderer wRenderer) {
            _answer += txtProd.getText();
            wRenderer.render(_answer, _img);
            return this;
        }

        public Builder addTextRandom() {
            var rndTxt = TS_RandomUtils.nextInt(0, 1);
            d.ci("rndTxt", rndTxt);
            var rndWord = TS_RandomUtils.nextInt(0, 2);
            d.ci("rndWord", rndWord);
            return addTextIdx(rndTxt, rndWord);
        }

        public Builder addTextPreffered() {
            return addTextPreffered(false);
        }

        public Builder addTextPreffered(boolean onlyNumbers) {
            var rndTxt = onlyNumbers ? 1 : TS_RandomUtils.nextInt(0, 1);
            d.ci("rndTxt", rndTxt);
            var rndRenderer = TS_RandomUtils.nextInt(0, 1);
            d.ci("rndRenderer", rndRenderer);
            return addTextIdx(rndTxt, rndRenderer);
        }

        public Builder addTextIdx(int txtIdx_0_1, int rendererIdx_0_2) {
            TS_CaptchaText txtProd;
            switch (txtIdx_0_1) {
                case 0:
                    txtProd = new TS_CaptchaTextDefault();
                    break;
                case 1:
                default:
                    txtProd = new TS_CaptchaTextNumber();
            }
            TS_CaptchaRenderer wRenderer;
            switch (rendererIdx_0_2) {
                case 0:
                    wRenderer = new TS_CaptchaRendererDefault();
                    break;
                case 1:
                    wRenderer = new TS_CaptchaRendererAligned();
                    break;
                case 2:
                default:
                    wRenderer = new TS_CaptchaRendererColoredEdges();
            }
            addText(txtProd, wRenderer);
            return this;
        }

        public Builder addNoise(TS_CaptchaNoise nProd) {
            nProd.makeNoise(_img);
            return this;
        }

        public Builder addNoiseRandom() {
            var rndNoise = TS_RandomUtils.nextInt(0, 1);
            d.ci("rndNoise", rndNoise);
            return addNoiseIdx(rndNoise);
        }

        public Builder addNoiseIdx(int noiseIdx_0_1) {
            switch (noiseIdx_0_1) {
                case 0:
                    addNoise(new TS_CaptchaNoiseStraightLine());
                    break;
                case 1:
                default:
                    addNoise(new TS_CaptchaNoiseCurvedLine());
            }
            return this;
        }

        public Builder addGimp(TS_CaptchaGimpy gimpy) {
            gimpy.gimp(_img);
            return this;
        }

        public Builder addGimpPreffered() {
            var gimp = 1;
            d.ci("prefGimp", gimp);
            return addGimpIdx(gimp);
        }

        public Builder addGimpRandom() {
            var rndGimp = TS_RandomUtils.nextInt(0, 1);
            d.ci("rndGimp", rndGimp);
            return addGimpIdx(rndGimp);
        }

        public Builder addGimpIdx(int gimpIdx_0_1) {
            switch (gimpIdx_0_1) {
                case 0:
                    addGimp(new TS_CaptchaGimpyShear());
                    break;
                case 1:
                default:
                    addGimp(new TS_CaptchaGimpyFishEye());
            }
            return this;
        }

        public Builder addBorder(boolean enable) {
            _addBorder = enable;
            return this;
        }

        public Builder addBorderRandom() {
            var rndBorder = TS_RandomUtils.nextBoolean();
            d.ci("rndBorder", rndBorder);
            addBorder(rndBorder);
            return this;
        }

        public TS_Captcha buildRandom() {
            return addTextRandom()
                    .addBackgroundRandom()
                    .addGimpRandom()
                    .addNoiseRandom()
                    .addBorderRandom()
                    .buildCustom();
        }

        public TS_Captcha buildPreffered(Integer opt_bgIdx_0_3, Integer opt_gimpIdx_0_1,
                Integer opt_borderIdx_0_1, Integer opt_txtIdx_0_1, Integer opt_wordIdx_0_2,
                Integer opt_noiseIdx_0_1, boolean onlyNumbers) {
            if (opt_bgIdx_0_3 != null) {
                addBackgroundIdx(opt_bgIdx_0_3);
            } else {
                addBackgroundPreffered();
            }
            if (opt_gimpIdx_0_1 != null) {
                addGimpIdx(opt_gimpIdx_0_1);
            } else {
                addGimpPreffered();
            }
            if (opt_borderIdx_0_1 != null) {
                addBorder(opt_borderIdx_0_1 == 1);
            } else {
                addBorderRandom();
            }
            if (opt_txtIdx_0_1 != null && opt_wordIdx_0_2 != null) {
                addTextIdx(opt_txtIdx_0_1, opt_wordIdx_0_2);
            } else if (opt_txtIdx_0_1 != null) {
                addTextIdx(opt_txtIdx_0_1, TS_RandomUtils.nextInt(0, 2));
            } else if (opt_wordIdx_0_2 != null) {
                addTextIdx(TS_RandomUtils.nextInt(0, 1), opt_wordIdx_0_2);
            } else {
                addTextPreffered(onlyNumbers);
            }
            if (opt_noiseIdx_0_1 != null) {
                addNoiseIdx(opt_noiseIdx_0_1);
            } else {
                addNoiseRandom();
            }
            return buildCustom();
        }

        public TS_Captcha buildCustom() {
            if (_bg == null) {
//                _bg = new TS_CaptchaBGTransparent().getBackground(_img.getWidth(), _img.getHeight());
                _bg = new TS_CaptchaBGFlatColor(Color.GRAY).getBackground(_img.getWidth(), _img.getHeight());
            }

            // Paint the main image over the background
            var g = _bg.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(_img, null, null);

            // Add the border, if necessary
            if (_addBorder) {
                var width = _img.getWidth();
                var height = _img.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            _img = _bg;

            return new TS_Captcha(this);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(_answer);
            ImageIO.write(_img, "png", ImageIO.createImageOutputStream(out));
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            _answer = (String) in.readObject();
            _img = ImageIO.read(ImageIO.createImageInputStream(in));
        }
    }

    public String getAnswer() {
        return _builder._answer;
    }

    public BufferedImage getImage() {
        return _builder._img;
    }

    @Override
    public String toString() {
        return _builder.toString();
    }
}
