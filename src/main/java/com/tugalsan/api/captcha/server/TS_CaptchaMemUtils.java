package com.tugalsan.api.captcha.server;

import com.tugalsan.api.captcha.client.TGS_CaptchaUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.network.server.TS_NetworkIPUtils;
import com.tugalsan.api.servlet.url.server.TS_SURLExecutorList;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.async.scheduled.TS_ThreadAsyncScheduled;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.url.server.TS_UrlServletRequestUtils;
import java.time.Duration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class TS_CaptchaMemUtils {
    
    private TS_CaptchaMemUtils(){
        
    }

    final private static TS_Log d = TS_Log.of(TS_CaptchaMemUtils.class);
    final private static TS_ThreadSyncLst<TS_CaptchaMemItem> SYNC = TS_ThreadSyncLst.ofSlowRead();

    public static void initialize(TS_ThreadSyncTrigger killTrigger) {
        TS_SURLExecutorList.add(new TS_CaptchaSUEMemRefresh());
        TS_ThreadAsyncScheduled.everyMinutes(killTrigger.newChild(d.className), Duration.ofSeconds(10), false, 10, kt -> {
            SYNC.removeAll(item -> item.time.hasSmaller(TGS_Time.ofMinutesAgo(10)));
            d.ci("initialize", "cleanUp.done");
        });
    }

    public static TGS_UnionExcuseVoid validate(HttpServletRequest rq) {
        var u = getServer(rq);
        if (u.isExcuse()) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA", "server==null", u.excuse().getMessage());
            return u.toExcuseVoid();
        }
        var captchaServer = u.value();
        var captchaClient = getClient(rq);
        if (captchaClient.guess() == null) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA", "captchaClient.guess==null");
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", "captchaClient.guess() == null");
        }
        var result = captchaClient.guess().toString().compareToIgnoreCase(//NO TURKISH CHECK NEEDED
                String.valueOf(captchaServer.answer)
        ) == 0;
        if (!result) {
            d.ce(d.className, "ERROR: STATUS_REJECTED_WRONG_CAPTCHA",
                    "client", captchaClient.clientIp(), captchaClient.guess(),
                    "server", captchaServer.clientIp, captchaServer.answer, captchaServer.time
            );
            delServer(rq);
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "validate", "wrong captcha");
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static TS_CaptchaClientValues getClient(HttpServletRequest rq) {
        var clientIp = TS_NetworkIPUtils.getIPClient(rq);
        var guess = TS_UrlServletRequestUtils.getParameterValueFrom64(rq, TGS_CaptchaUtils.PARAM_ANSWER());
        return new TS_CaptchaClientValues(clientIp, guess);
    }

    public static TGS_UnionExcuseVoid delServer(HttpServletRequest rq) {
        var u = TS_NetworkIPUtils.getIPClient(rq);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        delServer(u.value());
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static void delServer(CharSequence clientIp) {
        SYNC.removeAndPopFirst(item -> Objects.equals(item.clientIp, clientIp));
    }

    public static TGS_UnionExcuse<TS_CaptchaMemItem> getServer(HttpServletRequest rq) {
        var u = TS_NetworkIPUtils.getIPClient(rq);
        if (u.isExcuse()) {
            return u.toExcuse();
        }
        return getServer(u.value());
    }

    public static TGS_UnionExcuse<TS_CaptchaMemItem> getServer(CharSequence clientIp) {
        var val = SYNC.findFirst(item -> Objects.equals(item.clientIp, clientIp));
        if (val == null) {
            if (SYNC.isEmpty()) {
                d.ce("getServer", "list.empty");
            } else {
                SYNC.forEach(false, item -> {
                    d.ce("getServer", "list.item", item);
                });
            }
            return TGS_UnionExcuse.ofExcuse(d.className, "getServer", "clientIp not found:" + clientIp);
        }
        return TGS_UnionExcuse.of(val);
    }

    public static TGS_UnionExcuseVoid setServer(HttpServletRequest rq, CharSequence answer) {
        var u = TS_NetworkIPUtils.getIPClient(rq);
        if (u.isExcuse()) {
            return u.toExcuseVoid();
        }
        return setServer(u.value(), answer);
    }

    public static TGS_UnionExcuseVoid setServer(CharSequence clientIp, CharSequence answer) {
        var now = TGS_Time.of();
        var u = getServer(clientIp);
        if (u.isExcuse()) {
            SYNC.add(new TS_CaptchaMemItem(now, clientIp, answer));
        } else {
            var found = u.value();
            found.time = now;
            found.answer = answer;
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }
}
