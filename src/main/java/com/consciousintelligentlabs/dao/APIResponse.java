package com.consciousintelligentlabs.dao;

import org.springframework.http.HttpStatus;

import java.math.BigInteger;
import java.util.List;

public class APIResponse {

  private HttpStatus code;

  private String message;

  public APIResponse() {}

  public APIResponse(HttpStatus code, String message) {
    this.code = code;
    this.message = message;
  }

  public HttpStatus getCode() {
    return code;
  }

  public void setCode(HttpStatus code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String toString() {
    return "{" + "  \"code\": \"" + code + "\", " + "  \"message\": \"" + message + "\" " + "}";
  }
}
