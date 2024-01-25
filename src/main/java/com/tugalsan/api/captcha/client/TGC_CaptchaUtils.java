package com.tugalsan.api.captcha.client;

import com.google.gwt.user.client.ui.Image;
import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;
import com.tugalsan.api.url.client.builder.TGS_UrlBuilderUtils;

public class TGC_CaptchaUtils {

//    final private static TGC_Log d = TGC_Log.of(TGC_CaptchaUtils.class);

    public static String newUrl(CharSequence domain, Integer port, CharSequence spi) {
        var newUrlCaptcha = TGS_UrlBuilderUtils.https()
                .domain(domain).port(port).directory(spi)
                .fileOrServlet(TGS_SURLUtils.LOC_NAME)
                .parameter(TGS_SURLUtils.PARAM_SERVLET_NAME(), TGS_CaptchaUtils.SERVLET_REFRESH())
                .parameterRandom("r", 10).toString();
        Image.prefetch(newUrlCaptcha);
        return newUrlCaptcha;
    }
}
