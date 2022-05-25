package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class BookRouter {
    @Bean
    public RouterFunction<ServerResponse> route(BookHandler bookHandler) {

        RouterFunction<ServerResponse> serverResponseRouterFunction = RouterFunctions
                .route(GET("/random")
                        .and(accept(MediaType.APPLICATION_JSON)), bookHandler::random)
                .andRoute(GET("/")
                        .and(accept(MediaType.APPLICATION_JSON)),bookHandler::random)
                .andRoute(GET("/count")
                        .and(accept(MediaType.APPLICATION_JSON)),bookHandler::count);
        return serverResponseRouterFunction;
    }
}
