package com.tugalsan.api.captcha.server;

public class TS_CaptchaClientValues {

    public TS_CaptchaClientValues(CharSequence clientIp, CharSequence guess) {
        this.clientIp = clientIp;
        this.guess = guess;
    }
    public CharSequence clientIp;
    public CharSequence guess;
}
