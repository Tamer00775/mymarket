package kz.kartayev.authorization_service.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

  @Value("${spring.datasource.url}")
  private String dataSource;

  @Bean
  public SpringLiquibase liquibase(DataSource dataSource) {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog("classpath:liquibase/master.xml");
    liquibase.setShouldRun(true); // Это флаг, чтобы Liquibase выполнялся только при запуске
    liquibase.setDropFirst(false); // Установите true, если хотите удалять базу данных перед применением изменений
    return liquibase;
  }
}
