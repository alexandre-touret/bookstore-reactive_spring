package info.touret.bookstore.reactive.spring;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {"info.touret.bookstore.reactive.spring.*"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {BookstoreConfiguration.class})})
@EnableTransactionManagement
public class BookstoreReactiveSpringTestApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(BookstoreReactiveSpringTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BookstoreReactiveSpringTestApplication.class);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        LOGGER.info(">>>> Initializing TEST ConnectionFactory ...");
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("01-books-test-ddl.sql"), new ClassPathResource("books-data.sql")));
        return initializer;
    }
}
