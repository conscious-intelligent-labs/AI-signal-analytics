package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.TechnicalIndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TechnicalIndicatorServiceImpl implements TechnicalIndicatorService {
  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

  /** The URL for the harmonic scanner API. */
  @Value(value = "${harmonicpattern.host}")
  protected String host;

  /** Token for harmonic pattern API. */
  @Value(value = "${harmonicpattern.token}")
  private String token;

  /** Endpoint. */
  @Value(value = "${harmonicpattern.technical-indicator.endpoint}")
  private String endpoint;

  /** The exchanges providing symbol data. */
  @Value(value = "${harmonicpattern.exchanges}")
  private String exchanges;

  @Value(value = "${alphaAdvantage.api.key}")
  protected String alphavantageApiKey;

  @Value(value = "${alphaAdvantage.api.endpoint}")
  protected String alphavantageEndpoint;

  /**
   * Creates the endpoint.
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
   * Makes a request to get the Technical Indicators for a Symbol.
   *
   * @return APIResponse
   */
  public APIResponse getTechnicalIndicatorData(String symbol, String timeframe) {
    // Create response object.
    APIResponse APIResponse = new APIResponse();
    HTTPClient client = new HTTPClient();

    try {
      // Create Get client.
      client.createGetClient(this.getEndpoint(symbol, timeframe));

      // Send Get request.
      client.sendGet();

      logger.info(Constants.TECHNICAL_INDICATOR_EVENT + " Result returned: " + client.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(client.getResult());
    } catch (Exception e) {
      logger.error(
          Constants.TECHNICAL_INDICATOR_EVENT
              + " Error making an request to retrieve Technical Indicators from "
              + this.getEndpoint(symbol, timeframe)
              + ". [Error] "
              + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }

    return APIResponse;
  }

  /**
   * Gets the data for a single indicator.
   *
   * @param indicator  Indicator.
   * @param symbol     Symbol.
   * @param timeframe  Timeframe.
   * @param count      Candles to return.
   * @param seriesType Price series type.
   *
   * @return
   */
  public APIResponse getSingleIndicatorData(String indicator, String symbol, String timeframe, int count, String seriesType) {

    // Create response object.
    APIResponse APIResponse = new APIResponse();

    String convertedTimeframe = this.convertTimeframe(timeframe);
    String url = "function=" +
            indicator +
            "&symbol=" +
            symbol +
            "&interval=" +
            convertedTimeframe +
            "&time_period=" +
            count +
            "&series_type=" +
            seriesType +
            "&apikey=" + this.alphavantageApiKey;
    try {
      // Get Client.
      HTTPClient httpClient = new HTTPClient();
      httpClient.createGetClient(this.alphavantageEndpoint + url);
      httpClient.sendGet();

      logger.info(Constants.MARKETDATA_EVENT + " Result returned: " + httpClient.getResult());

      // Set response to return to server.
      APIResponse.setCode(HttpStatus.OK);
      APIResponse.setMessage(httpClient.getResult());
    } catch (Exception e) {
      logger.error(
              Constants.MARKETDATA_EVENT
                      + " Error making an request to retrieve Market data from "
                      + this.alphavantageEndpoint
                      + ". [Error] "
                      + e.getMessage());

      // Set Error response to return to server.
      APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
      APIResponse.setMessage("Internal Error. Please see admin for more details.");
    }

    return APIResponse;
  }

  /**
   * Parses a Symbol.
   *
   * @param symbol Symbol.
   * @return String []
   */
  public String[] parseSymbolToArray(String symbol) {
    int indexOfSplit = symbol.indexOf('/');
    String symbolFrom;
    String symbolTo;

    if (indexOfSplit > 0) {
      symbolFrom =  symbol.substring(0, indexOfSplit);
      symbolTo =  symbol.substring(indexOfSplit + 1);
    } else {
      return new String[] {symbol};
    }

    return new String[]{symbolFrom, symbolTo};
  }

  /**
   * Converts the timeframe.
   *
   * @param timeframe Timeframe to convert.
   *
   * @return
   */
  private String convertTimeframe (String timeframe) {
    switch (timeframe.toLowerCase()) {
      case "m1":
        return "1min";
      case "m5":
        return "5min";
      case "m15":
        return "15min";
      case "m30":
        return "30min";
      case "h1":
        return "60min";
      case "1":
        return "1min";
      case "5":
        return "5min";
      case "15":
        return "15min";
      case "30":
        return "30min";
      case "60":
        return "60min";

      default: return "15min";
    }
  }
}
