package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface MarketDataService {
    APIResponse getMarketData(String symbol, String timeframe, int count) throws Exception;
}
