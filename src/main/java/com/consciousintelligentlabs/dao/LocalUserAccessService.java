package com.consciousintelligentlabs.dao;

import com.consciousintelligentlabs.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("localDao")

public class LocalUserAccessService implements UserDAO{

  private static List<User> DB = new ArrayList<>();

  @Override
  public int insertUser(User user) {
    DB.add(new User(user.getUsername(), user.getPassword()));
    return 1;
  }

  @Override
  public List<User> getUsers() {
    return DB;
  }

  @Override
  public Optional<User> getUser(String username) {
    return DB.stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public int deleteUser(String username) {
    Optional<User> userOptional = getUser(username);
    if (userOptional.isPresent()) {
      DB.remove(userOptional.get());
      return 1;
    }
    return 0;
  }

  @Override
  public int updateUser(String username, User newUser) {
    return getUser(username)
        .map(oldUser -> {
          int indexOfUser = DB.indexOf(oldUser);
          if (indexOfUser >= 0) {
            DB.set(indexOfUser, new User(username, newUser.getPassword()));
            return 1;
          }
          return 0;
        }).orElse(0);
  }
}
