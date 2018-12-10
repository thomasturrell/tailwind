/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TailwindApplicationIT {

  private PrintStream original;

  @Before
  public void setUp() {
    original = System.out;
  }

  @After
  public void after() {
    System.setOut(original);
  }

  @Test
  public void testWhenStartingApplicationThenApplicationStarts() throws Exception {

    final ByteArrayOutputStream sysOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sysOut));

    // When Starting Application
    TailwindApplication.main(new String[] { "--server.port=0" });

    // Then Application Starts
    assertThat(sysOut.toString(), containsString("Started TailwindApplication"));

  }

}
