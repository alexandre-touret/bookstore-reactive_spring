package info.touret.bookstore.reactive.spring.book;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class BookstoreConfiguration {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("01-books-ddl.sql"), new ClassPathResource("02-books-data.sql")));
        return initializer;
    }

    @Bean
    public WebClient createWebClient(@Value("${booknumbers.api.url}") String numbersURL) {
        return WebClient.create(numbersURL);
    }

    /**
     * Creates a circuit breaker customizer applying a timeout specified by the <code>booknumbers.api.timeout_sec</code> property.
     *
     * @return the default resilience4j circuit breaker customizer
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> createDefaultCustomizer(@Value("${booknumbers.api.timeout_sec}") int timeoutInSec) {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(timeoutInSec)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }

    /**
     * Creates a circuit breaker customizer applying a timeout specified by the <code>booknumbers.api.timeout_sec</code> property.
     * This customizer could be reached using this id: <code>slowNumbers</code>
     *
     * @return the circuit breaker customizer to apply when calling to numbers api
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> createSlowNumbersAPICallCustomizer(@Value("${booknumbers.api.timeout_sec}") int timeoutInSec) {
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(timeoutInSec)).build()), "slowNumbers");
    }
}
