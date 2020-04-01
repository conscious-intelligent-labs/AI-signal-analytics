package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.SupportAndResistanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SupportAndResistanceServiceImpl implements SupportAndResistanceService {
  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

  /** The URL for the harmonic scanner API. */
  @Value(value = "${harmonicpattern.host}")
  protected String host;

  /** Token for harmonic pattern API. */
  @Value(value = "${harmonicpattern.token}")
  private String token;

  /** Notification endpoint. */
  @Value(value = "${harmonicpattern.support-resistance.endpoint}")
  private String endpoint;

  /** The exchanges providing symbol data. */
  @Value(value = "${harmonicpattern.exchanges}")
  private String exchanges;

  /**
   * Creates the notification endpoint.
   *
   * @return string
   */
  public String getEndpoint(String symbol, String timeframe) {
    return host
        + endpoint
        + "?token="
        + token
        + "?symbol="
        + exchanges
        + ":"
        + symbol
        + "&resolution="
        + timeframe;
  }

  /**
   * Makes a request to get the Support and resistance of a Symbol.
   *
   * @return APIResponse
   * @throws Exception
   */
  public APIResponse getSupportAndResistance(String symbol, String timeframe) throws Exception {
    // Create response object.
    APIResponse APIResponse = new APIResponse();
    HTTPClient client = new HTTPClient();

    try {
      // Create Get client.
      client.createGetClient(this.getEndpoint(symbol, timeframe));

      // Send Get request.
      client.sendGet();

      logger.info(
          Constants.SUPPORT_AND_RESISTANCE_EVENT + " Result returned: " + client.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(client.getResult());
    } catch (Exception e) {
      logger.error(
          Constants.SUPPORT_AND_RESISTANCE_EVENT
              + " Error making an request to retrieve Support and Resistance from "
              + this.getEndpoint(symbol, timeframe)
              + ". [Error] "
              + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }

    return APIResponse;
  }
}
