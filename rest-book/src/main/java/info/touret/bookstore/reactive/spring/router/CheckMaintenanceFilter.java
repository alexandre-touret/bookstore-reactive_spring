package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.exception.ApiCallTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CheckMaintenanceFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMaintenanceFilter.class);
    private final ApplicationAvailability availability;

    public CheckMaintenanceFilter(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (availability.getReadinessState().equals(ReadinessState.REFUSING_TRAFFIC) &&
                !exchange.getRequest().getPath().value().equals(MaintenanceRouter.MAINTENANCE_BASE_PATH)) {
            LOGGER.warn("Message handled during maintenance [{}]", exchange.getRequest().getPath());
            exchange.getResponse().setStatusCode(HttpStatus.I_AM_A_TEAPOT);
            return exchange.getResponse().writeWith(Mono.empty());
        } else {
            return chain.filter(exchange)
                    .onErrorResume(ApiCallTimeoutException.class, error -> {
                        exchange.getResponse().setStatusCode(HttpStatus.REQUEST_TIMEOUT);
                        return exchange.getResponse().writeWith(Mono.error(error));
                    })
                    .onErrorResume(Exception.class, error -> {
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return exchange.getResponse().writeWith(Mono.error(error));
                    });
        }
    }
}
