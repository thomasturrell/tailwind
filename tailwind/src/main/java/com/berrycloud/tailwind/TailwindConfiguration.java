
package com.berrycloud.tailwind;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class TailwindConfiguration {

  /**
   * <p>
   * Configure Spring Data REST to execute JSR-303 validations prior to storing an entity to the database.
   * </p>
   */
  @Configuration
  @RequiredArgsConstructor
  protected class RepositoryConfig implements RepositoryRestConfigurer {

    private final LocalValidatorFactoryBean smartValidator;

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

      // Add default jsr303 validations
      validatingListener.addValidator("beforeCreate", smartValidator);
      validatingListener.addValidator("beforeSave", smartValidator);

    }
  }
}
