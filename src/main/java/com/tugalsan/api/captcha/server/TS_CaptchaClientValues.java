package com.tugalsan.api.captcha.server;

import com.tugalsan.api.union.client.TGS_UnionExcuse;

public record TS_CaptchaClientValues(TGS_UnionExcuse<String> clientIp, CharSequence guess) {

}
