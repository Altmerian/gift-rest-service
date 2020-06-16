package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
@PropertySource({"classpath:datasource.properties"})
public class DataConfig {

  private final Environment env;

  @Autowired
  public DataConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public PlatformTransactionManager txManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public DataSource dataSource() {

    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    try {
      dataSource.setDriverClass(env.getProperty("jdbc.driver"));
    } catch (PropertyVetoException exc) {
      throw new RuntimeException(exc);
    }

    dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
    dataSource.setUser(env.getProperty("jdbc.user"));
    dataSource.setPassword(env.getProperty("jdbc.password"));

    dataSource.setInitialPoolSize(
        getIntProperty(env.getProperty("connection.pool.initialPoolSize")));
    dataSource.setMinPoolSize(getIntProperty(env.getProperty("connection.pool.minPoolSize")));
    dataSource.setMaxPoolSize(getIntProperty(env.getProperty("connection.pool.maxPoolSize")));
    dataSource.setMaxIdleTime(getIntProperty(env.getProperty("connection.pool.maxIdleTime")));

    return dataSource;
  }

  private int getIntProperty(String propValue) {
    if (isParsable(propValue)) {
      return Integer.parseInt(propValue);
    } else {
      throw new IllegalArgumentException("Invalid connection pool parameter: " + propValue);
    }
  }
}
