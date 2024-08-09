package com.tugalsan.api.captcha.server;

import com.tugalsan.api.time.client.TGS_Time;

public class TS_CaptchaMemItem {

    public TS_CaptchaMemItem(TGS_Time time, CharSequence clientIp, CharSequence answer) {
        this.time = time;
        this.clientIp = clientIp;
        this.answer = answer;
    }
    public TGS_Time time;
    public CharSequence clientIp;
    public CharSequence answer;

    @Override
    public String toString() {
        return TS_CaptchaMemItem.class.getSimpleName() + "{" + "time=" + time + ", clientIp=" + clientIp + ", answer=" + answer + '}';
    }
    
    
}
