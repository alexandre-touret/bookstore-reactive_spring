package info.touret.bookstore.reactive.spring.router;

import info.touret.bookstore.reactive.spring.BookstoreReactiveSpringTestApplication;
import info.touret.bookstore.reactive.spring.dto.IsbnNumbers;
import info.touret.bookstore.reactive.spring.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookstoreReactiveSpringTestApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookRouterTest {

    private static final String BASE_PATH = "/api/books";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WebClient webClientMock;
    @MockBean
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @MockBean
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @MockBean
    private WebClient.RequestBodySpec requestBodyMock;
    @MockBean
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @MockBean
    private WebClient.ResponseSpec responseMock;

    @BeforeEach
    void setUp() {
        IsbnNumbers isbnNumbers = new IsbnNumbers();
        isbnNumbers.setIsbn10("012456789");
        isbnNumbers.setIsbn13("012456789123");
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(IsbnNumbers.class)).thenReturn(Mono.just(isbnNumbers));
    }

    @Test
    void should_return_a_random_book() {
        webTestClient
                .get()
                .uri(BASE_PATH + "/random")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void should_return_the_list_with_one_item() {
        webTestClient
                .get()
                .uri(BASE_PATH + "/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class).hasSize(1);
    }

    @Test
    void should_return_the_count_1() {
        webTestClient
                .get()
                .uri(BASE_PATH + "/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new HashMap<String, Long>().getClass()).value(count -> count.get("books.count").equals(1));
    }

    @Test
    void should_create_a_book() {
        Book book = new Book();
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        webTestClient
                .post()
                .uri(BASE_PATH + "/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(URI.class).value(uri -> uri.getPath().contains("/api/books/1"))
                .value(System.out::println);
    }

    @Test
    void should_get_a_book() {
        webTestClient
                .get()
                .uri(BASE_PATH + "/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(book -> book.getId().equals(100L));
    }

    @Test
    void should_update_a_book() throws Exception {
        Book book = new Book();
        book.setId(100L);
        book.setAuthor("George Orwell");
        book.setTitle("Animal's farm");
        book.setDescription("Animal's farm");
        String mediumImageUrl = "http://mockaddress.com";
        book.setMediumImageUrl(mediumImageUrl);
        book.setSmallImageUrl(mediumImageUrl);
        webTestClient
                .put()
                .uri(BASE_PATH + "/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(Book.class)
                .value(book1 -> book1.getMediumImageUrl().equals(mediumImageUrl));
    }

    @Test
    void should_delete_a_book() throws Exception {
        webTestClient
                .delete()
                .uri(BASE_PATH + "/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
        ;

        webTestClient
                .get()
                .uri(BASE_PATH + "/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
        ;
    }


}
