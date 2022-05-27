package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.GlobalExceptionHandler;
import info.touret.bookstore.reactive.spring.exception.MaintenanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CheckMaintenanceFilter implements WebFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(CheckMaintenanceFilter.class);
    private final ApplicationAvailability availability;

    private final GlobalExceptionHandler exceptionHandler;

    public CheckMaintenanceFilter(ApplicationAvailability availability,  GlobalExceptionHandler exceptionHandler) {
        this.availability = availability;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (availability.getReadinessState().equals(ReadinessState.REFUSING_TRAFFIC) &&
                !exchange.getRequest().getPath().equals(MaintenanceRouter.MAINTENANCE_BASE_PATH)) {
            LOGGER.warn("Message handled during maintenance [{}]", exchange.getRequest().getPath());
            return exceptionHandler.handle(exchange, new MaintenanceException("Service currently in maintenance"));
        } else {
            return chain.filter(exchange);
        }
    }
}
