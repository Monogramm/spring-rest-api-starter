package com.monogramm;

import com.monogramm.starter.config.properties.ApplicationProperties;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {

  /**
   * Logger for {@link Application}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

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
      LOG.error("Exception catched during application startup", e);
    } finally {
      if (applicationContext == null) {
        LOG.debug("Something went wrong during startup! Check the logs for more details.");
      }
    }

    return applicationContext;
  }



  private final ApplicationProperties applicationProperties;

  /**
   * Welcome text for startup of application.
   */
  private final String welcome;

  protected final String applicationName;
  protected final String buildVersion;
  protected final String buildTimestamp;

  private void printWelcome() {
    printWelcome(System.out);
  }

  private void printWelcome(final OutputStream out) {
    try {
      out.write(welcome.getBytes());
    } catch (IOException e) {
      LOG.error("Could not display welcome message :(", e);
    }
  }

  /**
   * Create a {@link Application}.
   * 
   * @param applicationProperties application properties.
   */
  @Autowired
  public Application(ApplicationProperties applicationProperties) {
    super();

    if (applicationProperties == null) {
      throw new IllegalArgumentException("Application properties cannot be null.");
    }
    this.applicationProperties = applicationProperties;


    final StringBuilder builder = new StringBuilder("");

    // TODO Retrieve a custom banner from file (different from spring banner)
    // Display a custom ASCII art to describe the project
    final String asciiArt = this.applicationProperties.getWelcome().getAsciiArt();
    if (asciiArt != null && !asciiArt.isEmpty()) {
      builder.append(asciiArt);
    }

    // Display application name
    final String name = this.applicationProperties.getName();
    if (name != null && !"@project.artifactId@".equals(name)) {
      builder.append(" ").append(name);

      this.applicationName = name;
    } else {
      this.applicationName = null;
    }

    // Display application version
    final String version = this.applicationProperties.getBuildVersion();
    if (version != null && !"@project.version@".equals(version)) {
      builder.append(" (v").append(version).append(")");

      this.buildVersion = version;
    } else {
      this.buildVersion = null;
    }

    // Display application build timestamp
    final String timestamp = this.applicationProperties.getBuildTimestamp();
    if (timestamp != null && !"@timestamp@".equals(timestamp)) {
      builder.append(" ").append(timestamp);

      this.buildTimestamp = timestamp;
    } else {
      this.buildTimestamp = null;
    }

    builder.append("\n\n");

    welcome = builder.toString();

    printWelcome();
  }

}
