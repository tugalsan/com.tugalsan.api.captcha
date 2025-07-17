package com.tugalsan.api.gwt.captcha.server.text;

/**
 * TextProducer implementation that will return a series of numbers.
 */
public class TS_CaptchaTextNumber implements TS_CaptchaText {

    private static int DEFAULT_LENGTH() {
        return 5;
    }

    private static char[] NUMBERS() {
        return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    }

    private final TS_CaptchaText _txtProd;

    public TS_CaptchaTextNumber() {
        this(DEFAULT_LENGTH());
    }

    public TS_CaptchaTextNumber(int length) {
        _txtProd = new TS_CaptchaTextDefault(length, NUMBERS());
    }

    @Override
    public String getText() {
        return new StringBuilder(_txtProd.getText()).toString();
    }
}
