package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface TechnicalIndicatorService {
  APIResponse getTechnicalIndicatorService(String symbol, String timeframe) throws Exception;
}
