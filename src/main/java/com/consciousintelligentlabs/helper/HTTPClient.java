package com.consciousintelligentlabs.helper;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPClient {

  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);
  private final CloseableHttpClient httpClient = HttpClients.createDefault();
  protected List<NameValuePair> urlParameters = new ArrayList<>();
  protected HttpGet get;
  protected HttpPost post;
  protected String result;

  private void close() throws IOException {
    httpClient.close();
  }

  public void sendGet() throws Exception {
    if (this.get == null) {
      throw new Exception("No GET client created");
    }

    try (CloseableHttpResponse response = httpClient.execute(this.get)) {
      logger.info("[HTTPClient] Sending request: " + response.getStatusLine().toString());
      // Get HttpResponse Status
      System.out.println(response.getStatusLine().toString());

      HttpEntity entity = response.getEntity();
      Header headers = entity.getContentType();
      System.out.println(headers);

      if (entity != null) {
        // return it as a String
        this.result = EntityUtils.toString(entity);
      }
    }
  }

  /**
   * Send post request. Use addParameters() to add post parameters.
   *
   * @param url
   * @throws Exception
   */
  public void sendPost(String url) throws Exception {

    if (this.post == null) {
      throw new Exception("No POST client created");
    }

    if (this.urlParameters != null) {
      post.setEntity(new UrlEncodedFormEntity(this.urlParameters));
    }

    try (CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post)) {

      logger.info("[HTTPClient] Response: " + EntityUtils.toString(response.getEntity()));
    }
  }

  public void addHeader(String key, String value) {
    if (this.post != null) {
      this.post.addHeader(key, value);
    }

    if (this.get != null) {
      this.get.addHeader(key, value);
    }
  }

  public String getResult() {
    return result;
  }

  public void addParameter(String key, String value) {
    this.urlParameters.add(new BasicNameValuePair(key, value));
  }

  public HttpPost createPostClient(String url) {
    this.post = new HttpPost(url);
    return this.post;
  }

  public HttpGet createGetClient(String url) {
    this.get = new HttpGet(url);
    return this.get;
  }
}
