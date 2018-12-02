
package com.berrycloud.flywaydemo;

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
public class MemberControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void testWhenGettingMembersThenStatusCodeIsOk() {

    // When Getting Members
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenMemberExistsWhenGettingMemberThenStatusCodeIsOk() {

    // Given Member Exists

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);

    // Then Status Code Is Ok
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void testGivenMemberDoesNotExistWhenGettingMemberThenStatusCodeIsNotFound() {

    // Given Member Does Not Exist

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/100", String.class);

    // Then Status Code Is Not Found
    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testGivenMemberExistsWhenGettingMemberThenFirstNameFieldExists() {

    // Given Member Exists

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);

    // Then firstName Field Exists
    assertThat(response.getBody(), hasJsonPath("$.firstName"));
  }

  @Test
  public void testGivenExistingMemberWithFirstNameLisaWhenGettingMemberThenFirstNameIsLisa() {

    // Given Existing Member With firstName Lisa

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);

    // Then firstName Is Lisa
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Lisa")));
  }

  @Test
  public void testGivenMemberExistsWhenGettingMemberThenLastNameFieldExists() {

    // Given Member Exists

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);

    // Then lastName Field Exists
    assertThat(response.getBody(), hasJsonPath("$.lastName"));
  }

  @Test
  public void testGivenExistingMemberWithLastNameSimpsonWhenGettingMemberThenLastNameIsSimpson() {

    // Given Existing Member With LastName Simpson

    // When Getting Member
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);

    // Then lastName Is Simpson
    assertThat(response.getBody(), hasJsonPath("$.lastName", is("Simpson")));
  }

  @Test
  public void testGivenMemberExistsWhenDeletingMemberThenStatusCodeIsNoContent() {

    // Given Member Exists

    // When Deleting Member
    final ResponseEntity<String> response =
        testRestTemplate.exchange("/members/2", HttpMethod.DELETE, null, String.class);

    // Then Status Code Is No Content
    assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
  }

  @Test
  public void testGivenMemberExistsWhenDeletingMemberThenMemberIsDeleted() {

    // Given Member Exists

    // When Deleting Member
    testRestTemplate.delete("/members/3");

    // Then Member Is Deleted
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/3", String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }

  @Test
  public void testWhenPostingMemberThenStatusCodeIsCreated() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Status Code Is Created
    assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
  }

  @Test
  public void testWhenPostingMemberThenLocationIsNotNull() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Location Is Not Null
    assertThat(response.getHeaders().getLocation(), is(notNullValue()));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithDifferentFirstNameThenMembersFirstNameIsChanged() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Different First Name
    testRestTemplate.exchange("/members/4", HttpMethod.PATCH, new HttpEntity<>("{\"firstName\":\"Snowball\"}", headers),
        String.class);

    // Then Members First Name Is Changed
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/4", String.class);
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Snowball")));
  }

}
