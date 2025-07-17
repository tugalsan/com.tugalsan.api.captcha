module com.tugalsan.api.gwt.captcha {
    requires java.desktop;
    requires gwt.user;
    requires com.tugalsan.api.thread;
    requires com.tugalsan.api.log;
    
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.function;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.network;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.url;
    
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.servlet.url;
    exports com.tugalsan.api.gwt.captcha.client;
    exports com.tugalsan.api.gwt.captcha.server;
}
