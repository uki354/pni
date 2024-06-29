package pearl.ch.services.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "pearl.ch.services.entity.dbch1",
        entityManagerFactoryRef = "entityManagerFactoryDbch1",
        transactionManagerRef = "transactionManagerDbch1"
)
public class HibernateConfigDbch1Read {
	
	
    @Bean(name = "dataSourceDbch1")
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource dataSourceDbch1() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "entityManagerFactoryDbch1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryDbch1(
        EntityManagerFactoryBuilder builder,
        @Qualifier("dataSourceDbch1") DataSource dataSourceDbch1) {
        return builder
            .dataSource(dataSourceDbch1)
            .packages("pearl.ch.services.entity.dbch1")
            .persistenceUnit("dbch1")
            .build();
    }

    @Bean(name = "transactionManagerDbch1")
    public PlatformTransactionManager transactionManagerDbch1(
            @Qualifier("entityManagerFactoryDbch1") EntityManagerFactory entityManagerFactoryDbch1) {
        return new JpaTransactionManager(entityManagerFactoryDbch1);
    }

}
