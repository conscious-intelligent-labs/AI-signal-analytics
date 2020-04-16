package com.consciousintelligentlabs.dao;

import com.consciousintelligentlabs.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

  int insertUser(User user);

  List<User> getUsers();

  Optional<User> getUser(String username);

  int deleteUser(String username);

  int updateUser(String username, User user);

}
