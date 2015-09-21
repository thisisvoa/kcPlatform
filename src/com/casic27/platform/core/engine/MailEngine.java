package com.casic27.platform.core.engine;

import org.springframework.mail.MailSender;

public class MailEngine
{
  public String defaultFrom;
  public MailSender mailSender;
  FreemarkEngine freemarkEngine;

  public String getDefaultFrom()
  {
    return this.defaultFrom;
  }

  public void setDefaultFrom(String defaultFrom) {
    this.defaultFrom = defaultFrom;
  }

  public MailSender getMailSender() {
    return this.mailSender;
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public FreemarkEngine getFreemarkEngine() {
    return this.freemarkEngine;
  }

  public void setFreemarkEngine(FreemarkEngine freemarkEngine) {
    this.freemarkEngine = freemarkEngine;
  }
}

/* Location:           D:\Develop\apache-tomcat-6.0.35\webapps\bpm\WEB-INF\classes\
 * Qualified Name:     com.hotent.core.engine.MailEngine
 * JD-Core Version:    0.6.2
 */