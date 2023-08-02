package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.network.server.TS_NetworkIPUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutorList;
import com.tugalsan.api.thread.server.struct.async.TS_ThreadAsync;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeLst;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.url.server.TS_UrlServletRequestUtils;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class TS_CaptchaMemUtils {

    final private static TS_Log d = TS_Log.of(TS_CaptchaMemUtils.class);
    final private static TS_ThreadSafeLst<TS_CaptchaMemItem> SYNC = new TS_ThreadSafeLst();

    public static void initialize() {
        TS_SURLExecutorList.add(new TS_CaptchaSUEMemRefresh());
        TS_ThreadAsync.everyMinutes(false, 10, () -> {
            SYNC.removeAll(item -> item.time.hasSmaller(TGS_Time.ofMinutesAgo(10)));
            d.ci("initialize", "cleanUp.done");
        });
    }

    public static boolean validate(HttpServletRequest rq) {
        var captchaServer = getServer(rq);
        if (captchaServer == null) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA", "server==null");
            return false;
        }
        var captchaClient = getClient(rq);
        if (captchaClient.guess == null) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA", "captchaClient.guess==null");
            return false;
        }
        var result = captchaClient.guess.toString().compareToIgnoreCase(//NO TURKISH CHECK NEEDED
                String.valueOf(captchaServer.answer)
        ) == 0;
        if (!result) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA",
                    "client", captchaClient.clientIp, captchaClient.guess,
                    "server", captchaServer.clientIp, captchaServer.answer, captchaServer.time
            );
            delServer(rq);
        }
        return result;
    }

    public static TS_CaptchaClientValues getClient(HttpServletRequest rq) {
        var clientIp = TS_NetworkIPUtils.getIPClient(rq);
        var guess = TS_UrlServletRequestUtils.getParameterValueFrom64(rq, TGS_CaptchaUtils.PARAM_ANSWER());
        return new TS_CaptchaClientValues(clientIp, guess);
    }

    public static void delServer(HttpServletRequest rq) {
        delServer(TS_NetworkIPUtils.getIPClient(rq));
    }

    public static void delServer(CharSequence clientIp) {
        SYNC.removeFirst(item -> Objects.equals(item.clientIp, clientIp));
    }

    public static TS_CaptchaMemItem getServer(HttpServletRequest rq) {
        return getServer(TS_NetworkIPUtils.getIPClient(rq));
    }

    public static TS_CaptchaMemItem getServer(CharSequence clientIp) {
        return SYNC.findFirst(item -> Objects.equals(item.clientIp, clientIp));
    }

    public static void setServer(HttpServletRequest rq, CharSequence answer) {
        setServer(TS_NetworkIPUtils.getIPClient(rq), answer);
    }

    public static void setServer(CharSequence clientIp, CharSequence answer) {
        var now = TGS_Time.of();
        var found = getServer(clientIp);
        if (found != null) {
            found.time = now;
            found.answer = answer;
            return;
        }
        SYNC.add(new TS_CaptchaMemItem(now, clientIp, answer));
    }
}
