package com.monogramm;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

  /**
   * Logger for {@link Application}.
   */
  private static final Logger LOG = LogManager.getLogger(Application.class);

  /**
   * Spring application starter.
   * 
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    Application.run(args);
  }

  /**
   * Static helper that can be used to run this Spring Application using default settings.
   * 
   * @param args the application arguments (usually passed from a Java main method).
   * 
   * @return the running {@link ApplicationContext}.
   */
  protected static ConfigurableApplicationContext run(String[] args) {
    ConfigurableApplicationContext applicationContext = null;

    try {
      applicationContext = SpringApplication.run(Application.class, args);
    } catch (Exception e) {
      LOG.error(e);
    } finally {
      if (applicationContext == null) {
        LOG.debug("Something went wrong during startup! Check the logs for more details.");
      }
    }

    return applicationContext;
  }

}
