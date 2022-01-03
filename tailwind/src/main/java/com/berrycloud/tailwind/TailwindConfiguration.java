/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableJpaAuditing
public class TailwindConfiguration {

  /**
   * <p>
   * Configure Spring Data REST to execute JSR-303 validations and validators
   * prior to storing an entity to the database.
   * </p>
   */
  @Configuration
  @RequiredArgsConstructor
  protected class RepositoryConfig implements RepositoryRestConfigurer {

    private static final String BEFORE_SAVE = "beforeSave";
    private static final String BEFORE_CREATE = "beforeCreate";

    private final ListableBeanFactory beanFactory;
    private final LocalValidatorFactoryBean smartValidator;

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

      // Add default jsr303 validations
      validatingListener.addValidator(BEFORE_CREATE, smartValidator);
      validatingListener.addValidator(BEFORE_SAVE, smartValidator);

      // Add prefixed validators
      final Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);

      // Add validators with 'beforePersist' prefix to both 'beforeSave' and
      // 'beforeCreate' event
      for (final Map.Entry<String, Validator> entry : validators.entrySet()) {
        if (entry.getKey().startsWith("beforePersist")) {
          validatingListener.addValidator(BEFORE_CREATE, entry.getValue());
          validatingListener.addValidator(BEFORE_SAVE, entry.getValue());
        }
      }
    }
  }
}
