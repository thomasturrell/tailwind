/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

@RunWith(MockitoJUnitRunner.class)
public class MemberValidatorTests {

  @Mock
  private Errors errors;

  @InjectMocks
  private BeforePersistMemberValidator validator;

  @Test
  public void testGivenMemberWithNoRetirementOrBirthDatesWhenValidatedThenZeroErrors() throws Exception {

    // Given Member With No Retirement Or Birth Dates
    final Member member = new Member();

    // When Validated
    validator.validate(member, errors);

    // Then Zero Errors
    verifyZeroInteractions(errors);
  }

  @Test
  public void testGivenMemberWithRetirementButNoBirthDateWhenValidatedThenZeroErrors() throws Exception {

    // Given Member With Retirement But No Birth Date
    final Member member = new Member();
    member.setRetirement(LocalDate.now().minusYears(1));

    // When Validated
    validator.validate(member, errors);

    // Then Zero Errors
    verifyZeroInteractions(errors);
  }

  @Test
  public void testGivenMemberWithRetirementAfterBirthWhenValidatedThenZeroErrors() throws Exception {

    // Given Member With Retirement After Birth
    final Member member = new Member();
    member.setDateOfBirth(LocalDate.now().minusYears(2));
    member.setRetirement(LocalDate.now().minusYears(1));

    // When Validated
    validator.validate(member, errors);

    // Then Zero Errors
    verifyZeroInteractions(errors);
  }

  @Test
  public void testGivenMemberWithRetirementBeforeBirthWhenValidatedThenErrorWasRaised() throws Exception {

    // Given Member With Retirement Before Birth
    final Member member = new Member();
    member.setDateOfBirth(LocalDate.now().minusYears(1));
    member.setRetirement(LocalDate.now().minusYears(2));

    // When Validated
    validator.validate(member, errors);

    // Then Error Was Raised
    verify(errors).rejectValue("retirement", "NotBeforeBeforeBirth",
        "Date of retirement can not be before date of birth");
  }

  @Test
  public void testGivenClassMemberWhenSupportIsCalledThenResultIsTrue() {

    // Given Class Member
    final Class<Member> clazz = Member.class;

    // When Support Is Called
    final boolean supports = validator.supports(clazz);

    // Then Result Is True
    assertThat(supports, is(true));
  }

  @Test
  public void testGivenClassStringWhenSupportIsCalledThenResultIsFalse() {

    // Given Class String
    final Class<String> clazz = String.class;

    // When Support Is Called
    final boolean result = validator.supports(clazz);

    // Then Result Is False
    assertThat(result, is(false));
  }

  @Test
  public void testGivenNullClassWhenSupportIsCalledThenResultIsFalse() {

    // Given Null Class
    final Class<Member> clazz = null;

    // When Support Is Called
    final boolean result = validator.supports(clazz);

    // Then Result Is False
    assertThat(result, is(false));
  }

}
