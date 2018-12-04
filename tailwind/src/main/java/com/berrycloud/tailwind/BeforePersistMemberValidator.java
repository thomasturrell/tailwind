/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BeforePersistMemberValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Member.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    final Member member = (Member) target;

    if (member.getRetirement() != null && member.getDateOfBirth() != null
        && member.getRetirement().isBefore(member.getDateOfBirth())) {
      errors.rejectValue("retirement", "NotBeforeBeforeBirth", "Date of retirement can not be before date of birth");
    }

  }

}
