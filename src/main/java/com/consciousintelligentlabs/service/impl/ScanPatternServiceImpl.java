package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.controller.SignalAnalytics;
import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.helper.Constants;
import com.consciousintelligentlabs.helper.HTTPClient;
import com.consciousintelligentlabs.service.ScanPatternService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ScanPatternServiceImpl implements ScanPatternService {

    Logger logger = LoggerFactory.getLogger(SignalAnalytics.class);

    /** The URL for the harmonic scanner API. */
    @Value(value = "${harmonicpattern.host}")
    protected String host;

    /** Token for harmonic pattern API. */
    @Value(value = "${harmonicpattern.token}")
    protected String token;

    /** Scan endpoint. */
    @Value(value = "${harmonicpattern.scan.endpoint}")
    private String endpoint;

    /** Exchange. */
    @Value(value = "${harmonicpattern.exchanges}")
    private String exchange;

    /**
     * Creates the notification endpoint.
     *
     * @param symbol
     * @param timeframe
     * @return string
     */
    public String getScanEndpoint(String symbol, String timeframe) {
        return host + endpoint + "?token=" + token + "&symbol=" + exchange + ":" + symbol + "&resolution=" + timeframe;
    }

    /**
     * Makes a request to get the latest patterns.
     *
     * @return APIResponse
     * @throws Exception
     */
    public APIResponse getLatestPatterns(String symbol, String timeframe) throws Exception {
        // Create response object.
        APIResponse APIResponse = new APIResponse();
        HTTPClient client = new HTTPClient();

        try {
            // Create Get client.
            client.createGetClient(this.getScanEndpoint(symbol, timeframe));

            // Send Get request.
            client.sendGet();

            logger.info(Constants.SCAN_EVENT + " Result returned: " + client.getResult());

            // Set response to return to server.
            APIResponse.setCode(HttpStatus.OK);
            APIResponse.setMessage(client.getResult());
        } catch (Exception e) {
            logger.error(
                    Constants.SCAN_EVENT
                            + " Error making an request to retrieve notifications from "
                            + this.getScanEndpoint(symbol, timeframe)
                            + ". [Error] "
                            + e.getMessage());

            // Set Error response to return to server.
            APIResponse.setCode(HttpStatus.SERVICE_UNAVAILABLE);
            APIResponse.setMessage("Internal Error. Please see admin for more details.");
        }
        return APIResponse;
    }
}
