package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface ScanPatternService {
    APIResponse getLatestPatterns(String symbol, String timeframe) throws Exception;
}
