package com.monogramm;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

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



  private final Environment env;

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
   * @param env application environment properties.
   */
  @Autowired
  public Application(Environment env) {
    super();

    if (env == null) {
      throw new IllegalArgumentException("Application environment cannot be null.");
    }
    this.env = env;


    final StringBuilder builder = new StringBuilder("");

    // Display a custom ASCII art to describe the project
    builder.append("  __  __    ____    _   _    ____     _____   _____               __  __   __  __  \n");
    builder.append(" |  \\/  |  / __ \\  | \\ | |  / __ \\   / ____| |  __ \\      /\\     |  \\/  | |  \\/  | \n");
    builder.append(" | \\  / | | |  | | |  \\| | | |  | | | |  __  | |__) |    /  \\    | \\  / | | \\  / | \n");
    builder.append(" | |\\/| | | |  | | | . ` | | |  | | | | |_ | |  _  /    / /\\ \\   | |\\/| | | |\\/| | \n");
    builder.append(" | |  | | | |__| | | |\\  | | |__| | | |__| | | | \\ \\   / ____ \\  | |  | | | |  | | \n");
    builder.append(" |_|  |_|  \\____/  |_| \\_|  \\____/   \\_____| |_|  \\_\\ /_/    \\_\\ |_|  |_| |_|  |_| \n");

    // Display application name
    final String name = this.env.getProperty("application.name");
    if (name != null && !"@project.artifactId@".equals(name)) {
      builder.append(" ").append(name);

      this.applicationName = name;
    } else {
      this.applicationName = null;
    }

    // Display application version
    final String version = this.env.getProperty("build.version");
    if (version != null && !"@project.version@".equals(version)) {
      builder.append(" (v").append(version).append(")");

      this.buildVersion = version;
    } else {
      this.buildVersion = null;
    }

    // Display application build timestamp
    final String timestamp = this.env.getProperty("build.timestamp");
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
