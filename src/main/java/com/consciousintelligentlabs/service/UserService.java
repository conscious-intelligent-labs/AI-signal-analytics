package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;

public interface UserService {

  APIResponse createUser(String username, String password) throws Exception;

}
