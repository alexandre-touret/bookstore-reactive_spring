package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class BookRouter {

    private static final String BASE_PATH = "/api/books";

    @Bean
    public RouterFunction<ServerResponse> route(BookHandler bookHandler) {
        return RouterFunctions
                .route(GET(BASE_PATH + "/random")
                        .and(accept(MediaType.APPLICATION_JSON)), bookHandler::random)
                .andRoute(GET(BASE_PATH + "/")
                        .and(accept(MediaType.APPLICATION_JSON)), bookHandler::random)
                .andRoute(GET(BASE_PATH + "/count")
                        .and(accept(MediaType.APPLICATION_JSON)), bookHandler::count)
                .andRoute(POST(BASE_PATH + "/")
                        .and(accept(MediaType.APPLICATION_JSON)), bookHandler::create);
    }
}
