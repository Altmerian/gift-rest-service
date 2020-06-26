package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;

public class AppDispatcherServletInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer {

  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return null;
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {AppConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }
}
