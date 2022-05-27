package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.handler.MaintenanceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class MaintenanceRouter {

    private static final String MAINTENANCE_BASE_PATH = "/api/maintenance";

    @Bean
    public RouterFunction<ServerResponse> maintenanceRoutes(MaintenanceHandler maintenanceHandler) {

        return RouterFunctions.route(GET(MAINTENANCE_BASE_PATH)
                        .and(accept(MediaType.APPLICATION_JSON)), maintenanceHandler::checkMaintenanceStatus)
                .andRoute(PUT(MAINTENANCE_BASE_PATH)
                        .and(accept(MediaType.APPLICATION_JSON)), maintenanceHandler::putInMaintenance)
                ;
    }
}
