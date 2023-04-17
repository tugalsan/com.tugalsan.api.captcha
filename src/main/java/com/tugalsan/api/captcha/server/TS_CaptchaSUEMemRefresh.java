package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutor;
import com.tugalsan.api.servlet.url.server.TS_SURLHelper;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.validator.client.*;

public class TS_CaptchaSUEMemRefresh extends TS_SURLExecutor {

    final private static TS_Log d = TS_Log.of(TS_CaptchaSUEMemRefresh.class);

    @Override
    public String name() {
        return TGS_CaptchaUtils.SERVLET_REFRESH();
    }

    @Override
    public void run(TS_SURLHelper h) {
        TGS_UnSafe.run(() -> {
            var c = new TS_Captcha.Builder().buildPreffered(
                    h.getParameterInteger("bg", false),
                    h.getParameterInteger("gimp", false),
                    h.getParameterInteger("border", false),
                    h.getParameterInteger("txt", false),
                    h.getParameterInteger("word", false),
                    h.getParameterInteger("noise", false),
                    onlyNumbers == null ? false : onlyNumbers.validate(h)
            );
            TS_CaptchaMemUtils.setServer(h.rq, c.getAnswer());
            h.addHeaderNoCache().compileForPng().transferPng(c.getImage());
        }, e -> d.ce("run", e.getMessage()));
    }

    public static TGS_ValidatorType1<TS_SURLHelper> onlyNumbers;
}
