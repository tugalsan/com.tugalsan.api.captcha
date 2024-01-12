package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutor;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForFilePng;
import com.tugalsan.api.validator.client.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_CaptchaSUEMemRefresh extends TS_SURLExecutor {

//    final private static TS_Log d = TS_Log.of(TS_CaptchaSUEMemRefresh.class);

    @Override
    public String name() {
        return TGS_CaptchaUtils.SERVLET_REFRESH();
    }

    @Override
    public void run(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs) {
        TS_SURLHandler.of(servlet, rq, rs).permitNoCache().png(png -> {
            var captcha = new TS_Captcha.Builder().buildPreffered(
                    png.getParameterInteger("bg", false),
                    png.getParameterInteger("gimp", false),
                    png.getParameterInteger("border", false),
                    png.getParameterInteger("txt", false),
                    png.getParameterInteger("word", false),
                    png.getParameterInteger("noise", false),
                    onlyNumbers == null ? false : onlyNumbers.validate(png)
            );
            TS_CaptchaMemUtils.setServer(rq, captcha.getAnswer());
            return captcha.getImage();
        });
    }

    public static TGS_ValidatorType1<TS_SURLHandler02ForFilePng> onlyNumbers;
}
