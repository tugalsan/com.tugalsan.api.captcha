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
