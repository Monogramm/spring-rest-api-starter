package com.monogramm.starter;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import javax.mail.internet.MimeMessage;

import org.junit.rules.ExternalResource;

public class SmtpServerRule extends ExternalResource {

  private GreenMail smtpServer;
  private int port;

  public SmtpServerRule(int port) {
    this.port = port;
  }

  @Override
  protected void before() throws Throwable {
    super.before();
    smtpServer = new GreenMail(new ServerSetup(port, null, "smtp"));
    smtpServer.start();

    smtpServer.setUser("username", "secret");
  }

  public MimeMessage[] getMessages() {
    return smtpServer.getReceivedMessages();
  }

  @Override
  protected void after() {
    super.after();
    smtpServer.stop();
  }
}
