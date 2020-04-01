package com.consciousintelligentlabs.service;

import com.consciousintelligentlabs.dao.APIResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public interface NotificationService {
  APIResponse getNotifications() throws Exception;
}
