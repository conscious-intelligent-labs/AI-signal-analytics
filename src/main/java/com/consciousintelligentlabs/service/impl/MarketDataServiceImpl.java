package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    /** The URL for the harmonic scanner API. */
    @Value(value = "${harmonicpattern.host}")
    protected String host;

    /** Token for harmonic pattern API. */
    @Value(value = "${harmonicpattern.token}")
    private String token;

    /** endpoint. */
    @Value(value = "${harmonicpattern.candle.endpoint}")
    private String endpoint;

    /** The exchanges providing symbol data. */
    @Value(value = "${harmonicpattern.exchanges}")
    private String exchanges;

    Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

    /**
     * Creates the endpoint.
     *
     * @return string
     */
    public String getEndpoint(String symbol, String timeframe, int count) {
        return host + endpoint + "?token=" + token + "?symbol=" + exchanges + ":" + symbol + "&resolution=" +
                timeframe + "&count=" + count;
    }

    /**
     * Gets the market data for a candle.
     *
     * @param symbol    Symbol.
     * @param timeframe Timeframe.
     * @param count     Number of candles to return.
     *
     * @return
     * @throws Exception
     */
    public APIResponse getMarketData(String symbol, String timeframe, int count) throws Exception {
        // Create response object.
        APIResponse APIResponse = new APIResponse();
        HTTPClient client = new HTTPClient();
        String url = this.getEndpoint(symbol, timeframe, count);

        try {
            // Get Client.
            HTTPClient httpClient = new HTTPClient();
            httpClient.createGetClient(url);
            httpClient.sendGet();

            // Send Get request.
            client.sendGet();

            logger.info(Constants.MARKETDATA_EVENT + " Result returned: " + httpClient.getResult());

            // Set response to return to server.
            APIResponse.setCode(HttpStatus.OK);
            APIResponse.setMessage(httpClient.getResult());
        } catch (Exception e) {
            logger.error(
                    Constants.MARKETDATA_EVENT
                            + " Error making an request to retrieve Market data from "
                            + url
                            + ". [Error] "
                            + e.getMessage());

            // Set Error response to return to server.
            APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
            APIResponse.setMessage("Internal Error. Please see admin for more details.");
        }

        return APIResponse;
    }

}
