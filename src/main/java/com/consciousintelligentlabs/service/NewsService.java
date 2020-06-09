package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface NewsService {
  APIResponse getlatestNews(String category) throws Exception;
  APIResponse getNewsByLastId(String category, int lastId) throws Exception;
}
