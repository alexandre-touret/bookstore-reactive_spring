package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.BookstoreReactiveSpringTestApplication;
import info.touret.bookstore.reactive.spring.dto.MaintenanceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookstoreReactiveSpringTestApplication.class)
class MaintenanceRouterTest {


    @MockBean
    ApplicationAvailability applicationAvailability;

    @MockBean
    AvailabilityChangeEvent<ReadinessState> readinessStateAvailabilityChangeEvent;
    private static final String MAINTENANCE_BASE_PATH = "/api/maintenance";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        when(applicationAvailability.getReadinessState()).thenReturn(ReadinessState.ACCEPTING_TRAFFIC);
        when(readinessStateAvailabilityChangeEvent.getState()).thenReturn(ReadinessState.ACCEPTING_TRAFFIC);
        when(applicationAvailability.getLastChangeEvent(ReadinessState.class)).thenReturn(readinessStateAvailabilityChangeEvent);
    }

    @Test
    void should_return_a_maintenance_status() {
        webTestClient
                .get()
                .uri(MAINTENANCE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MaintenanceDTO.class);
    }

    @Test
    void should_set_maintenance_status() {
        webTestClient.put()
                .uri(MAINTENANCE_BASE_PATH)
                .bodyValue(Boolean.TRUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
        when(applicationAvailability.getReadinessState()).thenReturn(ReadinessState.REFUSING_TRAFFIC);
        webTestClient
                .get()
                .uri(MAINTENANCE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MaintenanceDTO.class)
                .value(MaintenanceDTO::isInMaintenance);
    }

    @Test
    void should_return_a_maintenance_error() {
        webTestClient
                .put()
                .uri(MAINTENANCE_BASE_PATH)
                .bodyValue(Boolean.TRUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
        when(applicationAvailability.getReadinessState()).thenReturn(ReadinessState.REFUSING_TRAFFIC);
        webTestClient
                .get()
                .uri("/api/books/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT);
    }
}
