/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TailwindApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(TailwindApplication.class, args);
  }
}
