package info.touret.bookstore.reactive.spring.number;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class NumberRouter {
    private static final String BASE_PATH = "/api/numbers";


    @Bean
    public RouterFunction<ServerResponse> bookRoutes(NumberHandler numberHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET(BASE_PATH)
                        .and(accept(MediaType.APPLICATION_JSON)), numberHandler::findNumber);
    }
}
