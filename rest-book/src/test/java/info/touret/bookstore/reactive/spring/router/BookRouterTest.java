package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.IntegrationTestConfiguration;
import info.touret.bookstore.reactive.spring.entity.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.objectweb.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = IntegrationTestConfiguration.class)
class BookRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void should_return_a_random_book() {
        webTestClient
                .get()
                .uri("/random")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void should_return_the_list_with_one_item() {
        webTestClient
                .get()
                .uri("/random")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(book -> book.getId().equals(100L));
    }

    @Test
    void should_return_the_count_1() {
        webTestClient
                .get()
                .uri("/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new HashMap<String, Long>().getClass()).value(count -> count.get("books.count").equals(1));
    }
}
