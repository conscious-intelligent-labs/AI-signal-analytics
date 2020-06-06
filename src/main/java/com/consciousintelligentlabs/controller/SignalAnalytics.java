package com.consciousintelligentlabs.controller;

import com.consciousintelligentlabs.dao.*;
import com.consciousintelligentlabs.helper.*;
import com.consciousintelligentlabs.helper.amqp.Producer;
import com.consciousintelligentlabs.helper.amqp.RabbitMqConfig;
import com.consciousintelligentlabs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@RestController
public class SignalAnalytics {

  @Autowired private RabbitMqConfig config;

  @Autowired private NotificationService notificationService;

  @Autowired private SupportAndResistanceService supportAndResistanceService;

  @Autowired private TechnicalIndicatorService technicalIndicatorService;

  @Autowired private NewsService newsService;

  @Autowired private MarketDataService marketDataService;

  @Autowired private ScanPatternService scanPatternService;

  Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

  /**
   * Get all the latest notifications.
   *
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/getnotifications", produces = "application/json")
  public APIResponse getNotifications() throws Exception {
    logger.info(
        Constants.SAAPI_EVENT
            + Constants.NOTIFICATION_EVENT
            + " Request to get latest Notifications.");

    return notificationService.getNotifications();
  }

  /**
   * Receives the latest notifications from an external source.
   *
   * @param notification The latest notifications.
   * @return APIResponse
   */
  @PostMapping(value = "/sa/receiveNotifications", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
  public APIResponse postNotifications(@RequestBody String notification) {
    logger.info(Constants.SAAPI_EVENT + Constants.NOTIFICATION_EVENT + " Received a notification.");
    APIResponse apiResponse;
    try {
      Producer.sendMessage(
          notification,
          config.getNotificationRoutingKey(),
          config.getExchangeName(),
          config.getHostName());

      logger.info(
          Constants.SAAPI_EVENT
              + Constants.NOTIFICATION_EVENT
              + " Notification sent to rabbitMQ:"
              + config.getHostName());

      apiResponse = new APIResponse(HttpStatus.OK, "Successful ");
    } catch (Exception e) {
      apiResponse =
          new APIResponse(
              HttpStatus.SERVICE_UNAVAILABLE, "Error processing request. " + e.getMessage());
    }

    return apiResponse;
  }

  /**
   * Route to get the Support and Resistance for a symbol.
   *
   * @param symbol The market symbol.
   * @param resolution The timeframe of a chart.
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/getsupportandresistance", produces = "application/json")
  public APIResponse getSupportAndResistance(
      @RequestParam String symbol, @RequestParam String resolution) throws Exception {

    logger.info(
        Constants.SAAPI_EVENT
            + Constants.SUPPORT_AND_RESISTANCE_EVENT
            + " Request to get latest Notifications.");

    if (this.isAcceptedTimeFrame(resolution) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid resolution/timeframe provided. ");
    }

    return supportAndResistanceService.getSupportAndResistance(symbol, resolution);
  }

  /**
   * Route to get the technical indicators for a symbol.
   *
   * @param symbol
   * @param resolution
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/gettechnicalindicators", produces = "application/json")
  public APIResponse getTechnicalIndicators(
      @RequestParam String symbol, @RequestParam String resolution) throws Exception {
    logger.info(
        Constants.SAAPI_EVENT
            + Constants.TECHNICAL_INDICATOR_EVENT
            + " Request to get latest Notifications.");

    if (this.isAcceptedTimeFrame(resolution) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid resolution/timeframe provided. ");
    }

    return technicalIndicatorService.getTechnicalIndicatorData(symbol, resolution);
  }

  /**
   * Route to get the latest news.
   *
   * @param category
   * @param lastId
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/getnews", produces = "application/json")
  public APIResponse getNews(@RequestParam String category, @RequestParam int lastId)
      throws Exception {
    logger.info(
        Constants.SAAPI_EVENT + Constants.NEWS_EVENT + " Request to get latest Notifications.");

    if (this.isAcceptedCatagory(category) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid category provided. ");
    }

    if (lastId < 1) {
      return newsService.getlatestNews(category);
    }

    return newsService.getNewsByLastId(category, lastId);
  }

  /**
   * Route to get the market data for a symbol.
   *
   * @param symbol
   * @param resolution
   * @param count
   *
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/getmarketdata", produces = "application/json")
  public APIResponse getMarketData(
          @RequestParam String symbol, @RequestParam String resolution, @RequestParam String count) throws Exception {
    logger.info(
            Constants.SAAPI_EVENT
                    + Constants.TECHNICAL_INDICATOR_EVENT
                    + " Request to get market data.");

    if (this.isAcceptedTimeFrame(resolution) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid resolution/timeframe provided. ");
    }

    return marketDataService.getMarketData(symbol, resolution, Integer.parseInt(count));
  }

  /**
   * Route to get single indicator for a symbol.
   *
   * @param indicator  Indicator to use.
   * @param symbol     Symbol.
   * @param resolution Timeframe of chart.
   * @param count      Number of candles to return.
   *
   * @return
   * @throws Exception
   */
  @GetMapping(value = "/sa/getsingleindicator", produces = "application/json")
  public APIResponse getSingleIndicator(
          @RequestParam String indicator,
          @RequestParam String symbol,
          @RequestParam String resolution,
          @RequestParam String count,
          @RequestParam String series_type
          ) throws Exception {
    logger.info(
            Constants.SAAPI_EVENT
                    + Constants.TECHNICAL_INDICATOR_EVENT
                    + " Request to get indicator data.");

    if (this.isAcceptedTimeFrame(resolution) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid resolution/timeframe provided. ");
    }

    return technicalIndicatorService.getSingleIndicatorData(indicator, symbol, resolution, Integer.parseInt(count), series_type);
  }

  /**
   * Route to get the latest pattern data for a symbol.
   *
   * @param symbol
   * @param resolution
   *
   * @return APIResponse
   * @throws Exception
   */
  @GetMapping(value = "/sa/getlatestpatternstatus", produces = "application/json")
  public APIResponse getLatestPatternsStatus(
          @RequestParam String symbol, @RequestParam String resolution) throws Exception {
    logger.info(
            Constants.SAAPI_EVENT
                    + Constants.SCAN_EVENT
                    + " Request to get Scan pattern data.");

    if (this.isAcceptedTimeFrame(resolution) == false) {
      return new APIResponse(HttpStatus.BAD_REQUEST, "Invalid resolution/timeframe provided. ");
    }

    return scanPatternService.getLatestPatterns(symbol, resolution);
  }

  /**
   * Checks for accepted timeframe.
   *
   * @param timeframe Chart timeframe.
   * @return Boolean
   */
  protected Boolean isAcceptedTimeFrame(String timeframe) {
    List<String> list = Arrays.asList(Constants.CHART_TIMEFRAMES);

    if (list.contains(timeframe)) {
      return true;
    }

    return false;
  }

  /**
   * Checks for accepted category.
   *
   * @param category News Category.
   * @return Boolean
   */
  protected Boolean isAcceptedCatagory(String category) {
    List<String> list = Arrays.asList(Constants.NEWS_CATAGORY);

    if (list.contains(category)) {
      return true;
    }

    return false;
  }
}
