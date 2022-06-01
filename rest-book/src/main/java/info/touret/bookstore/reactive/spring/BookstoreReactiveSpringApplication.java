package info.touret.bookstore.reactive.spring;

import info.touret.bookstore.reactive.spring.book.BookstoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@Import(BookstoreConfiguration.class)
public class BookstoreReactiveSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreReactiveSpringApplication.class, args);
    }


}
