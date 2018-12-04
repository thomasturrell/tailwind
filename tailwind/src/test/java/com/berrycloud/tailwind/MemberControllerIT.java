
package com.berrycloud.tailwind;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
  public void testWhenPostingMemberWithBlankFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Blank First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"   \",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithBlankFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Blank First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"   \",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        containsString(
            "{\"entity\":\"Member\",\"property\":\"firstName\",\"invalidValue\":\"   \",\"message\":\"must not be "
                + "blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithEmptyFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Empty First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithEmptyFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Empty First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(), containsString(
        "{\"entity\":\"Member\",\"property\":\"firstName\",\"invalidValue\":\"\",\"message\":\"must not be blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithNullFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Null First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":null,\"lastName\":\"Helper\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithNullFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Null First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":null,\"lastName\":\"Helper\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(), containsString(
        "{\"entity\":\"Member\",\"property\":\"firstName\",\"invalidValue\":null,\"message\":\"must not be blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithFirstNameWhichIsTooLongThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With First Name Which Is Too Long
    final String firstName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"" + firstName + "\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithFirstNameWhichIsTooLongThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With First Name Which Is Too Long
    final String firstName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"" + firstName + "\",\"lastName\":\"Helper\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        containsString("{\"entity\":\"Member\",\"property\":\"firstName\",\"invalidValue\":\"" + firstName
            + "\",\"message\":" + "\"size must be between 0 and 255\"}"));
  }

  @Test
  public void testWhenPostingMemberWithBlankLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Blank Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"   \"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithBlankLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Blank Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"   \"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        containsString(
            "{\"entity\":\"Member\",\"property\":\"lastName\",\"invalidValue\":\"   \",\"message\":\"must not be "
                + "blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithEmptyLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Empty Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithEmptyLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Empty Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(), containsString(
        "{\"entity\":\"Member\",\"property\":\"lastName\",\"invalidValue\":\"\",\"message\":\"must not be blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithNullLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Null Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":null}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithNullLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Null Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":null}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(), containsString(
        "{\"entity\":\"Member\",\"property\":\"lastName\",\"invalidValue\":null,\"message\":\"must not be blank\"}"));
  }

  @Test
  public void testWhenPostingMemberWithLastNameWhichIsTooLongThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Last Name Which Is Too Long
    final String lastName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"" + lastName + "\"}", headers),
        String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testWhenPostingMemberWithLastNameWhichIsTooLongThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // When Posting Member With Last Name Which Is Too Long
    final String lastName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members", HttpMethod.POST,
        new HttpEntity<>("{\"firstName\":\"Santa's Little\",\"lastName\":\"" + lastName + "\"}", headers),
        String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(), containsString("{\"entity\":\"Member\",\"property\":\"lastName\",\"invalidValue\":\""
        + lastName + "\",\"message\":" + "\"size must be between 0 and 255\"}"));
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

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"   \"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"   \"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("firstName")),
            hasJsonPath("$.errors[0].invalidValue", is("   ")),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankFirstNameThenFirstNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank First Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"firstName\":\"   \"}", headers),
        String.class);

    // Then First Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Lisa")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("firstName")),
            hasJsonPath("$.errors[0].invalidValue", is("")),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyFirstNameThenFirstNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty First Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"firstName\":\"\"}", headers),
        String.class);

    // Then First Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Lisa")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullFirstNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":null}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullFirstNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null First Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":null}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("firstName")),
            hasJsonPath("$.errors[0].invalidValue", is(nullValue())),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullFirstNameThenFirstNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null First Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"firstName\":null}", headers),
        String.class);

    // Then First Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Lisa")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithFirstNameWhichIsTooLongThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With First Name Which Is Too Long
    final String firstName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"" + firstName + "\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void
      testGivenMemberExistsWhenPatchingMemberWithFirstNameWhichIsTooLongThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With First Name Which Is Too Long
    final String firstName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"" + firstName + "\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("firstName")),
            hasJsonPath("$.errors[0].invalidValue", is(firstName)),
            hasJsonPath("$.errors[0].message", is("size must be between 0 and 255"))));
  }

  @Test
  public void
      testGivenMemberExistsWhenPatchingMemberWithFirstNameWhichIsTooLongThenFirstNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With First Name Which Is Too Long
    final String firstName = new String(new char[256]).replace('\0', 'a');
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"firstName\":\"" + firstName + "\"}", headers), String.class);

    // Then First Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.firstName", is("Lisa")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"   \"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"   \"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("lastName")),
            hasJsonPath("$.errors[0].invalidValue", is("   ")),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithBlankLastNameThenLastNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Blank Last Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"lastName\":\"   \"}", headers),
        String.class);

    // Then Last Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.lastName", is("Simpson")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("lastName")),
            hasJsonPath("$.errors[0].invalidValue", is("")),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithEmptyLastNameThenLastNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Empty Last Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"lastName\":\"\"}", headers),
        String.class);

    // Then Last Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.lastName", is("Simpson")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullLastNameThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":null}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullLastNameThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null Last Name
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":null}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("lastName")),
            hasJsonPath("$.errors[0].invalidValue", is(nullValue())),
            hasJsonPath("$.errors[0].message", is("must not be blank"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithNullLastNameThenLastNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Null Last Name
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH, new HttpEntity<>("{\"lastName\":null}", headers),
        String.class);

    // Then Last Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.lastName", is("Simpson")));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithLastNameWhichIsTooLongThenStatusCodeIsBadRequest() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Last Name Which Is Too Long
    final String lastName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"" + lastName + "\"}", headers), String.class);

    // Then Status Code Is Bad Request
    assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
  }

  @Test
  public void
      testGivenMemberExistsWhenPatchingMemberWithLastNameWhichIsTooLongThenResponseContainsExpectedErrorMessage() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Last Name Which Is Too Long
    final String lastName = new String(new char[256]).replace('\0', 'a');
    final ResponseEntity<String> response = testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"" + lastName + "\"}", headers), String.class);

    // Then Response Contains Expected Error Message
    assertThat(response.getBody(),
        allOf(hasJsonPath("$.errors[0].entity", is("Member")), hasJsonPath("$.errors[0].property", is("lastName")),
            hasJsonPath("$.errors[0].invalidValue", is(lastName)),
            hasJsonPath("$.errors[0].message", is("size must be between 0 and 255"))));
  }

  @Test
  public void testGivenMemberExistsWhenPatchingMemberWithLastNameWhichIsTooLongThenLastNameOfTheMemberDoesNotChange() {

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Given Member Exists

    // When Patching Member With Last Name Which Is Too Long
    final String lastName = new String(new char[256]).replace('\0', 'a');
    testRestTemplate.exchange("/members/1", HttpMethod.PATCH,
        new HttpEntity<>("{\"lastName\":\"" + lastName + "\"}", headers), String.class);

    // Then Last Name Of The Member Does Not Change
    final ResponseEntity<String> response = testRestTemplate.getForEntity("/members/1", String.class);
    assertThat(response.getBody(), hasJsonPath("$.lastName", is("Simpson")));
  }

}
