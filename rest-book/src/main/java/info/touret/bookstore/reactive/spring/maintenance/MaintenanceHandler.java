package info.touret.bookstore.reactive.spring.maintenance;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class MaintenanceHandler {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ApplicationAvailability applicationAvailability;

    public MaintenanceHandler(ApplicationEventPublisher applicationEventPublisher, ApplicationAvailability applicationAvailability) {

        this.applicationEventPublisher = applicationEventPublisher;
        this.applicationAvailability = applicationAvailability;
    }

    public Mono<ServerResponse> checkMaintenanceStatus(ServerRequest serverRequest) {
        var lastChangeEvent = applicationAvailability.getLastChangeEvent(ReadinessState.class);
        return ServerResponse
                .ok()
                .body(BodyInserters
                        .fromValue(new MaintenanceDTO(lastChangeEvent.getState().equals(ReadinessState.REFUSING_TRAFFIC)
                                , new Date(lastChangeEvent.getTimestamp()))));
    }

    public Mono<ServerResponse> putInMaintenance(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(String.class)
                .map(Boolean::valueOf)
                .doOnNext(status -> AvailabilityChangeEvent.publish(applicationEventPublisher, this, Boolean.TRUE.equals(status) ? ReadinessState.REFUSING_TRAFFIC : ReadinessState.ACCEPTING_TRAFFIC))
                .flatMap(aBoolean -> ServerResponse.noContent().build());
    }
}
