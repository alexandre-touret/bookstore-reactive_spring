package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.BookstoreReactiveSpringTestApplication;
import info.touret.bookstore.reactive.spring.dto.MaintenanceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookstoreReactiveSpringTestApplication.class)
class MaintenanceRouterTest {


    private static final String MAINTENANCE_BASE_PATH = "/api/maintenance";
    ;
    @Autowired
    private WebTestClient webTestClient;

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
                .expectStatus().isOk();

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
        webTestClient
                .get()
                .uri("/api/books/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT);
    }
}
