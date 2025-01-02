package com.tugalsan.api.captcha.client;

import com.google.gwt.user.client.ui.Image;
import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;
import com.tugalsan.api.thread.client.TGC_ThreadUtils;
import com.tugalsan.api.url.client.builder.TGS_UrlBuilderUtils;

public class TGC_CaptchaUtils {

//    final private static TGC_Log d = TGC_Log.of(TGC_CaptchaUtils.class);
    private static String tmpUrl() {
        if (tmpUrl == null) {
            tmpUrl = "data:image/png;base64,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";
        }
        return tmpUrl;
    }
    private static volatile String tmpUrl = null;

    private static String newUrl(CharSequence domain, Integer port, CharSequence spi) {
        var newUrlCaptcha = TGS_UrlBuilderUtils.https()
                .domain(domain).port(port).directory(spi)
                .fileOrServlet(TGS_SURLUtils.LOC_NAME)
                .parameter(TGS_SURLUtils.PARAM_SERVLET_NAME(), TGS_CaptchaUtils.SERVLET_REFRESH())
                .parameterRandom("r", 10).toString();
        Image.prefetch(newUrlCaptcha);
        return newUrlCaptcha;
    }

    public static Image create() {
        return new Image(tmpUrl());
    }

    public static void update(Image imgCaptcha, CharSequence domain, Integer port, CharSequence spi) {
        imgCaptcha.setUrl(tmpUrl());
        var newUrl = newUrl(domain, port, spi);
        TGC_ThreadUtils.run_afterSeconds_afterGUIUpdate(kt -> {
            imgCaptcha.setUrl(newUrl);
        }, 1);
    }
}
