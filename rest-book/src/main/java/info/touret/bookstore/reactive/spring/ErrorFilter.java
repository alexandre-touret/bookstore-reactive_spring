package info.touret.bookstore.reactive.spring;

import info.touret.bookstore.reactive.spring.book.ApiCallTimeoutException;
import info.touret.bookstore.reactive.spring.maintenance.MaintenanceRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;


public class ErrorFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorFilter.class);
    private final ApplicationAvailability availability;

    public ErrorFilter(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        if (availability.getReadinessState().equals(ReadinessState.REFUSING_TRAFFIC) &&
                !request.path().equals(MaintenanceRouter.MAINTENANCE_BASE_PATH)) {
            LOGGER.warn("Message handled during maintenance [{}]", request.path());
            return ServerResponse.status(I_AM_A_TEAPOT).build();
        } else {
            return next.handle(request)
                    .onErrorResume(ApiCallTimeoutException.class, error -> ServerResponse.status(HttpStatus.REQUEST_TIMEOUT).build())
                    .onErrorResume(IllegalStateException.class, error -> ServerResponse.status(INTERNAL_SERVER_ERROR).build())
                    .onErrorResume(Exception.class, error -> ServerResponse.status(INTERNAL_SERVER_ERROR).build());
        }
    }
}
