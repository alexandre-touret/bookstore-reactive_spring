package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class BookRouter {
    @Bean
    public RouterFunction<ServerResponse> route(BookHandler bookHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/random").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), bookHandler::random);
    }
}
