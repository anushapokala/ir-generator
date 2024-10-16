package com.neuron.cv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
public class AppConfig {

  @Value("${cv_order_sheet}")
  public String orderSheetPath;

  @Value("${instaplan_json}")
  public String instaplanJsonPath;

}
