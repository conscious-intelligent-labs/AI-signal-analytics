package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface SupportAndResistanceService {
  APIResponse getSupportAndResistance(String symbol, String timeframe) throws Exception;
}
