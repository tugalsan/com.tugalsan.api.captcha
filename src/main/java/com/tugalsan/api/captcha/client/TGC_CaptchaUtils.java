package com.tugalsan.api.captcha.client;

import com.google.gwt.user.client.ui.Image;
import com.tugalsan.api.executable.client.TGS_Executable;
import com.tugalsan.api.log.client.TGC_Log;
import com.tugalsan.api.pack.client.TGS_Pack1;
import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;
import com.tugalsan.api.thread.client.TGC_ThreadUtils;
import com.tugalsan.api.url.client.builder.TGS_UrlBuilderUtils;

public class TGC_CaptchaUtils {

    final private static TGC_Log d = TGC_Log.of(TGC_CaptchaUtils.class);

    public static String newUrl(CharSequence domain, Integer port, CharSequence spi) {
        var newUrlCaptcha = TGS_UrlBuilderUtils.https()
                .domain(domain).port(port).directory(spi)
                .fileOrServlet(TGS_SURLUtils.LOC_NAME)
                .parameter(TGS_SURLUtils.PARAM_SERVLET_NAME(), TGS_CaptchaUtils.SERVLET_REFRESH())
                .parameterRandom("r", 10).toString();
        Image.prefetch(newUrlCaptcha);
        return newUrlCaptcha;
    }

    @Deprecated //NOT WORKING
    public static void onErrorRecursiveReload(Image imgCaptcha, TGS_Executable onRefreshCaptcha, int imgCaptchaSecWait) {
        TGS_Pack1<Boolean> imgCaptchaLoaded = new TGS_Pack1(false);
        d.cr("execute", "UYARI: Test resmi bekleniyor... ", imgCaptchaSecWait, "saniye içinde gelmez ise yenilenecek!");
        imgCaptcha.addLoadHandler(e -> {
            imgCaptchaLoaded.value0 = true;
            d.cr("execute", "BİLGİ: Test resmi yüklendi.");
        });
        TGC_ThreadUtils.execute_afterSeconds_afterGUIUpdate(t -> {
            if (!imgCaptchaLoaded.value0) {
                d.ce("execute", "HATA: Test resmi yenileniyor...");
                onRefreshCaptcha.execute();
                onErrorRecursiveReload(imgCaptcha, onRefreshCaptcha, imgCaptchaSecWait);
            }
        }, imgCaptchaSecWait);
    }
}
