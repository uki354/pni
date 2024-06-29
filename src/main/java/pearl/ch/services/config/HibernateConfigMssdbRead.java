package pearl.ch.services.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "pearl.ch.services.entity.mssdb", entityManagerFactoryRef = "entityManagerRead", transactionManagerRef = "transactionManagerMssdb")
public class HibernateConfigMssdbRead {

	@Primary
	@Bean(name = "dataSourceRead")
	@ConfigurationProperties(prefix = "spring.datasource.write")
	public DataSource dataSourceMssdb() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "entityManagerRead")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryMssdb(EntityManagerFactoryBuilder builder,
			@Qualifier("dataSourceRead") DataSource dataSourceMssdb) {
		return builder.dataSource(dataSourceMssdb).packages("pearl.ch.services.entity.mssdb").persistenceUnit("mssRead")
				.build();
	}

	@Primary
	@Bean(name = "transactionManagerMssdb")
	public PlatformTransactionManager transactionManagerMssdb(
			@Qualifier("entityManagerRead") EntityManagerFactory entityManagerFactoryMssdb) {
		return new JpaTransactionManager(entityManagerFactoryMssdb);
	}

}
