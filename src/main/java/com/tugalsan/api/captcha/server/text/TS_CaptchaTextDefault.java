package com.tugalsan.api.captcha.server.text;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Produces text of a given length from a given array of characters. The default
 * (no-args) constructor produces five Latin characters from a specific set of
 * alphanumerics. Characters such as "1" (one) and "l" (lower-case 'ell') have
 * been removed to reduce ambiguity in the generated CAPTCHA.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *
 */
public class TS_CaptchaTextDefault implements TS_CaptchaText {

    private static Random _gen() {
        return new SecureRandom();
    }

    private static int DEFAULT_LENGTH() {
        return 5;
    }

    private static char[] DEFAULT_CHARS() {
        return new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'h', 'k', 'n', 'p', 'r', 'v', 'x', 'y',
            '2', '3', '4', '5', '6', '7', '8'
        };
    }

    private final int _length;
    private final char[] _srcChars;

    public TS_CaptchaTextDefault() {
        this(DEFAULT_LENGTH(), DEFAULT_CHARS());
    }

    public TS_CaptchaTextDefault(int length) {
        this(length, DEFAULT_CHARS());
    }

    public TS_CaptchaTextDefault(int length, char[] srcChars) {
        _length = length;
        _srcChars = srcChars != null ? copyOf(srcChars, srcChars.length) : DEFAULT_CHARS();
    }

    @Override
    public String getText() {
        var car = _srcChars.length - 1;
        var capText = new StringBuilder();
        for (var i = 0; i < _length; i++) {
            capText.append(_srcChars[_gen().nextInt(car) + 1]);
        }
        return capText.toString();
    }

    public static char[] copyOf(char[] original, int newLength) {
        var copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }
}
