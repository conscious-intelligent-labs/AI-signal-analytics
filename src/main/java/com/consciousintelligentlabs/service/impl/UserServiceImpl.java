package com.consciousintelligentlabs.service.impl;

import com.consciousintelligentlabs.dao.APIResponse;
import com.consciousintelligentlabs.dao.UserDAO;
import com.consciousintelligentlabs.model.User;
import com.consciousintelligentlabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;

  public UserServiceImpl(@Qualifier("localDAO") UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public APIResponse createUser(String username, String password) throws Exception {
    User user = new User(username, passwordEncoder.encode(password));
    this.userDAO.insertUser(user);

    APIResponse APIResponse = new APIResponse();
    APIResponse.setCode(HttpStatus.OK);
    APIResponse.setMessage("New user created");
    return APIResponse;
  }
}
