/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlanControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void testWhenGettingPlansThenStatusCodeIsOk() {

    // When Getting Plans
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenPlanExistsWhenGettingPlanThenStatusCodeIsOk() {

    // Given Plan Exists

    // When Getting Plan
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenPlanDoesNotExistWhenGettingPlanThenStatusCodeIsNotFound() {

    // Given Plan Does Not Exist

    // When Getting Plan
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/400", String.class);

    // Then Status Code Is Not Found
    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testGivenPlanExistsWhenGettingPlanThenNameFieldExists() {

    // Given Plan Exists

    // When Getting Plan
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4", String.class);

    // Then Name Field Exists
    assertThat(response.getBody(), hasJsonPath("$.name"));
  }

  @Test
  public void testGivenExistingPlanWithNameGoldWhenGettingPlanThenNameIsGold() {

    // Given Existing Plan With Name Gold

    // When Getting Plan
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4", String.class);

    // Then Name Is Gold
    assertThat(response.getBody(), hasJsonPath("$.name", is("Gold")));
  }

  @Test
  public void testGivenPlanExistsWhenGettingPlanThenOrganizationLinkExists() {

    // Given Plan Exists

    // When Getting Plan
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4", String.class);

    // Then Organization Link Exists
    assertThat(response.getBody(), hasJsonPath("$._links.organization"));
  }

  @Test
  public void testGivenExistingPlanWithOrganizationWhenGettingPlanOrganizationThenStatusCodeIsOk() {

    // Given Existing Plan With Organization

    // When Getting Plan Organization
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4/organization", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenExistingPlanWithMemberWhenGettingPlanMembersThenStatusCodeIsOk() {

    // Given Existing Plan With Member

    // When Getting Plan Members
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/4/members", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenPlanExistsWhenDeletingPlanThenStatusCodeIsNoContent() {

    // Given Plan Exists

    // When Deleting Plan
    final ResponseEntity<String> response =
        testRestTemplate.exchange("/plans/5", HttpMethod.DELETE, null, String.class);

    // Then Status Code Is No Content
    assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
  }

  @Test
  public void testGivenPlanExistsWhenDeletingPlanThenPlanIsDeleted() {

    // Given Plan Exists

    // When Deleting Plan
    testRestTemplate.delete("/plans/6");

    // Then Plan Is Deleted
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/6", String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testWhenPostingPlanThenStatusCodeIsCreated() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Plan
    final ResponseEntity<String> response = testRestTemplate.exchange("/plans", HttpMethod.POST,
        new HttpEntity<>("{\"name\":\"Tin\"}", headers), String.class);

    // Then Status Code Is Created
    assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
  }

  @Test
  public void testWhenPostingPlanThenLocationIsNotNull() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Plan
    final ResponseEntity<String> response = testRestTemplate.exchange("/plans", HttpMethod.POST,
        new HttpEntity<>("{\"name\":\"Lead\"}", headers), String.class);

    // Then Location Is Not Null
    assertThat(response.getHeaders().getLocation(), is(notNullValue()));
  }

  @Test
  public void testGivenPlanExistsWhenPatchingPlanWithDifferentNameThenPlansNameIsChanged() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Plan Exists

    // When Patching Plan With Different Name
    testRestTemplate.exchange("/plans/7", HttpMethod.PATCH, new HttpEntity<>("{\"name\":\"Steel\"}", headers),
        String.class);

    // Then Plans Name Is Changed
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/plans/7", String.class);
    assertThat(response.getBody(), hasJsonPath("$.name", is("Steel")));
  }

}
