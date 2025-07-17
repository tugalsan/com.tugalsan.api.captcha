package com.tugalsan.api.gwt.captcha.client;

public class TGS_CaptchaUtils {
    
    private TGS_CaptchaUtils(){
        
    }

    public static String SERVLET_REFRESH() {
        return TGS_CaptchaUtils.class.getSimpleName() + "_NAME_SERVLET_REFRESH";
    }

    public static String PARAM_ANSWER() {
        return TGS_CaptchaUtils.class.getSimpleName() + "_NAME_ANSWER";
    }
}
