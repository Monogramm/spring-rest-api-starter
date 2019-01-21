/*
 * Creation by mathieu.brunot the 2018-07-28.
 */

package com.monogramm.starter.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringFoxConfig.
 * 
 * @author mathieu.brunot
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

  @Autowired
  private Environment env;

  @Bean
  public Docket apiDocket() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any()).build().apiInfo(getApiInfo());
  }

  private ApiInfo getApiInfo() {
    final String appName = env.getProperty("info.app.name");
    final String appDescription = env.getProperty("info.app.description");
    final String appVersion = env.getProperty("info.app.version");

    final String tosUrl = env.getProperty("info.tos.url");

    final String licenseName = env.getProperty("info.license.name");
    final String licenseUrl = env.getProperty("info.license.url");

    final String contactName = env.getProperty("info.contact.name");
    final String contactUrl = env.getProperty("info.contact.url");
    final String contactEmail = env.getProperty("info.contact.email");

    return new ApiInfo(appName + " Documentation", appDescription, appVersion, tosUrl,
        new Contact(contactName, contactUrl, contactEmail), licenseName, licenseUrl,
        Collections.emptyList());
  }
}
