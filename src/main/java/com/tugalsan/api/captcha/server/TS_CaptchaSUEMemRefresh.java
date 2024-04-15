package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutor;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForFileImg;
import com.tugalsan.api.validator.client.*;

public class TS_CaptchaSUEMemRefresh extends TS_SURLExecutor {

//    final private static TS_Log d = TS_Log.of(TS_CaptchaSUEMemRefresh.class);
    @Override
    public String name() {
        return TGS_CaptchaUtils.SERVLET_REFRESH();
    }

    @Override
    public void run(TS_SURLHandler servletUrlHandler) {
        servletUrlHandler.img("png", img -> {
            var captcha = new TS_Captcha.Builder().buildPreffered(
                    img.getParameterInteger("bg").orElse(excuse -> null),
                    img.getParameterInteger("gimp").orElse(excuse -> null),
                    img.getParameterInteger("border").orElse(excuse -> null),
                    img.getParameterInteger("txt").orElse(excuse -> null),
                    img.getParameterInteger("word").orElse(excuse -> null),
                    img.getParameterInteger("noise").orElse(excuse -> null),
                    onlyNumbers == null ? false : onlyNumbers.validate(img)
            );
            TS_CaptchaMemUtils.setServer(servletUrlHandler.rq, captcha.getAnswer());
            return captcha.getImage();
        });
    }

    public static TGS_ValidatorType1<TS_SURLHandler02ForFileImg> onlyNumbers;
}
