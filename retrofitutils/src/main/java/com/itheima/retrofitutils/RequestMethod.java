package com.itheima.retrofitutils;

public enum RequestMethod {
    GET("GET"),

    POST("POST");
    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
