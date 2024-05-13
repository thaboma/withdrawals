package za.co.sanlam.fintech.withdrawal.config;

import com.zaxxer.hikari.HikariDataSource;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "spring.datasource")
@EnableJpaRepositories(basePackages = "za.co.sanlam.fintech.withdrawal.dao")
@EntityScan(basePackages = "za.co.sanlam.fintech.withdrawal.entity")
@ComponentScan(basePackages = "za.co.sanlam.fintech.withdrawal")
public class AppConfig extends com.zaxxer.hikari.HikariConfig {

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(this);
	}

	@Bean
	public OpenAPI apiDocConfig() {
		return new OpenAPI()
				.info(new Info()
						.title("Sanlam-withdrwal API")
						.description("A sample interview withdrawal app")
						.version("0.0.1")
						.contact(new Contact()
								.name("Thabo Madikane")
								.email("thaboma@gmail.com")))
				.externalDocs(new ExternalDocumentation()
						.description("Documentation")
						.url("https:/wiki...."));
	}
}
