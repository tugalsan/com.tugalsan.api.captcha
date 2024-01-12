module com.tugalsan.api.captcha {
    requires java.desktop;
    requires gwt.user;
    requires com.tugalsan.api.thread;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.network;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.servlet.url;
    exports com.tugalsan.api.captcha.client;
    exports com.tugalsan.api.captcha.server;
}
