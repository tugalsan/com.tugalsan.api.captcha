package com.tugalsan.api.captcha.server;

import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU_OutBool_In1;
import com.tugalsan.api.gwt.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutor;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForFileImg;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;

public class TS_CaptchaSUEMemRefresh extends TS_SURLExecutor {

//    final private static TS_Log d = TS_Log.of(TS_CaptchaSUEMemRefresh.class);
    @Override
    public String name() {
        return TGS_CaptchaUtils.SERVLET_REFRESH();
    }

    @Override
    public void run(TS_ThreadSyncTrigger servletKillThrigger, TS_SURLHandler suh) {
        suh.img("png", img -> {
            var captcha = new TS_Captcha.Builder().buildPreffered(
                    img.getParameterInteger("bg", false),
                    img.getParameterInteger("gimp", false),
                    img.getParameterInteger("border", false),
                    img.getParameterInteger("txt", false),
                    img.getParameterInteger("word", false),
                    img.getParameterInteger("noise", false),
                    onlyNumbers == null ? false : onlyNumbers.validate(img)
            );
            TS_CaptchaMemUtils.setServer(suh.rq, captcha.getAnswer());
            return captcha.getImage();
        });
    }

    public static TGS_FuncMTU_OutBool_In1<TS_SURLHandler02ForFileImg> onlyNumbers;
}
