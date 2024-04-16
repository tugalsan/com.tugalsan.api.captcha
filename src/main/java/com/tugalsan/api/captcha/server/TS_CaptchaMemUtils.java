package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.network.server.TS_NetworkIPUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutorList;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncScheduled;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.url.server.TS_UrlServletRequestUtils;
import java.time.Duration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class TS_CaptchaMemUtils {

    final private static TS_Log d = TS_Log.of(TS_CaptchaMemUtils.class);
    final private static TS_ThreadSyncLst<TS_CaptchaMemItem> SYNC = TS_ThreadSyncLst.of();

    public static void initialize(TS_ThreadSyncTrigger killTrigger) {
        TS_SURLExecutorList.add(new TS_CaptchaSUEMemRefresh());
        TS_ThreadAsyncScheduled.everyMinutes(killTrigger, Duration.ofSeconds(10), false, 10, kt -> {
            SYNC.removeAll(item -> item.time.hasSmaller(TGS_Time.ofMinutesAgo(10)));
            d.ci("initialize", "cleanUp.done");
        });
    }

    public static TGS_UnionExcuseVoid validate(HttpServletRequest rq) {
        var u_captchaServer = getServer(rq);
        if (u_captchaServer.isExcuse()) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", u_captchaServer.excuse().getMessage());
        }
        var captchaServer = u_captchaServer.value();
        var u_captchaClient = getClient(rq);
        if (u_captchaClient.isExcuse()) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", u_captchaClient.excuse().getMessage());
        }
        var captchaClient = u_captchaClient.value();
        if (captchaClient.guess == null) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", "captchaClient.guess == null");
        }
        var result = captchaClient.guess.toString().compareToIgnoreCase(//NO TURKISH CHECK NEEDED
                String.valueOf(captchaServer.answer)
        ) == 0;
        if (!result) {
            delServer(rq);
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA",
                    "client", captchaClient.clientIp, captchaClient.guess,
                    "server", captchaServer.clientIp, captchaServer.answer, captchaServer.time
            );
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", "!result");
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static TGS_UnionExcuse<TS_CaptchaClientValues> getClient(HttpServletRequest rq) {
        var u_clientIp = TS_NetworkIPUtils.getIPClient(rq);
        if (u_clientIp.isExcuse()) {
            return u_clientIp.toExcuse();
        }
        var clientIp = u_clientIp.value();
        var u_guess = TS_UrlServletRequestUtils.getParameterValueFrom64(rq, TGS_CaptchaUtils.PARAM_ANSWER());
        if (u_guess.isExcuse()) {
            return u_guess.toExcuse();
        }
        return TGS_UnionExcuse.of(new TS_CaptchaClientValues(clientIp, u_guess.value()));
    }

    public static TGS_UnionExcuseVoid delServer(HttpServletRequest rq) {
        var u_clientIp = TS_NetworkIPUtils.getIPClient(rq);
        if (u_clientIp.isExcuse()) {
            return TGS_UnionExcuseVoid.ofExcuse(u_clientIp.excuse());
        }
        if (!delServer(u_clientIp.value())) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "delServer", "Cannot find the item to delete");
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static boolean delServer(CharSequence clientIp) {
        return SYNC.removeFirst(item -> Objects.equals(item.clientIp, clientIp));
    }

    public static TGS_UnionExcuse<TS_CaptchaMemItem> getServer(HttpServletRequest rq) {
        var u_clientIp = TS_NetworkIPUtils.getIPClient(rq);
        if (u_clientIp.isExcuse()) {
            return u_clientIp.toExcuse();
        }
        return TGS_UnionExcuse.of(getServer(u_clientIp.value()));
    }

    public static TS_CaptchaMemItem getServer(CharSequence clientIp) {
        return SYNC.findFirst(item -> Objects.equals(item.clientIp, clientIp));
    }

    public static TGS_UnionExcuseVoid setServer(HttpServletRequest rq, CharSequence answer) {
        var u_clientIp = TS_NetworkIPUtils.getIPClient(rq);
        if (u_clientIp.isExcuse()) {
            return TGS_UnionExcuseVoid.ofExcuse(u_clientIp.excuse());
        }
        setServer(u_clientIp.value(), answer);
        return TGS_UnionExcuseVoid.ofVoid();
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
