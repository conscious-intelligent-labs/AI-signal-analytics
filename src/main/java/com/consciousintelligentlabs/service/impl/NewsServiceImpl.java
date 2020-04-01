package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {
  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

  /** The URL for the harmonic scanner API. */
  @Value(value = "${harmonicpattern.host}")
  protected String host;

  /** Token for harmonic pattern API. */
  @Value(value = "${harmonicpattern.token}")
  private String token;

  /** Notification endpoint. */
  @Value(value = "${harmonicpattern.news.endpoint}")
  private String endpoint;

  /**
   * Creates the notification endpoint.
   *
   * @return string
   */
  public String getEndpoint(String category, int lastId) {
    if (lastId < 1) {
      return host + endpoint + "?token=" + token + "?category=" + category;
    }

    return host + endpoint + "?token=" + token + "?category=" + category + "&minId=" + lastId;
  }

  public APIResponse getlatestNews(String category) throws Exception {
    // Create response object.
    APIResponse APIResponse = new APIResponse();
    HTTPClient client = new HTTPClient();

    try {
      // Create Get client.
      client.createGetClient(this.getEndpoint(category, 0));

      // Send Get request.
      client.sendGet();

      logger.info(Constants.NEWS_EVENT + " Result returned: " + client.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(client.getResult());
    } catch (Exception e) {
      logger.error(
          Constants.NEWS_EVENT
              + " Error making an request to retrieve Support and Resistance from "
              + this.getEndpoint(category, 0)
              + ". [Error] "
              + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }

    return APIResponse;
  }

  public APIResponse getNewsByLastId(String category, int lastId) throws Exception {
    // Create response object.
    APIResponse APIResponse = new APIResponse();
    HTTPClient client = new HTTPClient();

    try {
      // Create Get client.
      client.createGetClient(this.getEndpoint(category, lastId));

      // Send Get request.
      client.sendGet();

      logger.info(Constants.NEWS_EVENT + " Result returned: " + client.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(client.getResult());
    } catch (Exception e) {
      logger.error(
          Constants.NEWS_EVENT
              + " Error making an request to retrieve Support and Resistance from "
              + this.getEndpoint(category, lastId)
              + ". [Error] "
              + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }

    return APIResponse;
  }
}
