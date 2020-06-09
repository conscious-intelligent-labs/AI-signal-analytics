package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface TechnicalIndicatorService {
  APIResponse getTechnicalIndicatorData(String symbol, String timeframe);
  APIResponse getSingleIndicatorData(String indicator, String symbol, String timeframe, int count, String seriesType);
}
