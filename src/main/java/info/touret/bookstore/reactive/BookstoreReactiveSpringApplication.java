package info.touret.bookstore.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BookstoreReactiveSpringApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(BookstoreReactiveSpringApplication.class, args);
        GreetingClient greetingClient = context.getBean(GreetingClient.class);
        System.out.println(">> MSG=" + greetingClient.getMessage().block());
    }

}
