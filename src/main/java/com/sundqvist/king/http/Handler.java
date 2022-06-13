package com.sundqvist.king.http;

public interface Handler {

    boolean supports(String path, String requestMethod);

    Response invoke(String body, String pathParam, String user);

}
