package com.tugalsan.api.captcha.server;

import com.tugalsan.api.function.client.TGS_Func_OutBool_In1;
import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutor;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForFileImg;

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
                    img.getParameterInteger("bg", false),
                    img.getParameterInteger("gimp", false),
                    img.getParameterInteger("border", false),
                    img.getParameterInteger("txt", false),
                    img.getParameterInteger("word", false),
                    img.getParameterInteger("noise", false),
                    onlyNumbers == null ? false : onlyNumbers.validate(img)
            );
            TS_CaptchaMemUtils.setServer(servletUrlHandler.rq, captcha.getAnswer());
            return captcha.getImage();
        });
    }

    public static TGS_Func_OutBool_In1<TS_SURLHandler02ForFileImg> onlyNumbers;
}
