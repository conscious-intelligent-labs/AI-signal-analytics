package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import com.consciousintelligentlabs.dao.APIResponse;
import org.springframework.stereotype.Service;

@Service()
public class NotificationServiceImpl implements NotificationService {
  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

  /** The URL for the harmonic scanner API. */
  @Value(value = "${harmonicpattern.host}")
  protected String host;

  /** Token for harmonic pattern API. */
  @Value(value = "${harmonicpattern.token}")
  protected String token;

  /** Notification endpoint. */
  @Value(value = "${harmonicpattern.notification.endpoint}")
  private String endpoint;

  /**
   * Creates the notification endpoint.
   *
   * @return string
   */
  public String getNotificationEndpoint() {
    return host + endpoint + "?token=" + token;
  }

  /**
   * Makes a request to get the latest Notifications.
   *
   * @return APIResponse
   * @throws Exception
   */
  public APIResponse getNotifications() throws Exception {
    // Create response object.
    APIResponse APIResponse = new APIResponse();
    HTTPClient client = new HTTPClient();

    try {
      // Create Get client.
      client.createGetClient(this.getNotificationEndpoint());

      // Send Get request.
      client.sendGet();

      logger.info(Constants.NOTIFICATION_EVENT + " Result returned: " + client.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(client.getResult());
    } catch (Exception e) {
      logger.error(
          Constants.NOTIFICATION_EVENT
              + " Error making an request to retrieve notifications from "
              + this.getNotificationEndpoint()
              + ". [Error] "
              + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }
    return APIResponse;
  }
}
