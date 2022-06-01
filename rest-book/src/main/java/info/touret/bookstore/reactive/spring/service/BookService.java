package info.touret.bookstore.reactive.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.touret.bookstore.reactive.spring.dto.IsbnNumbers;
import info.touret.bookstore.reactive.spring.entity.Book;
import info.touret.bookstore.reactive.spring.exception.ApiCallTimeoutException;
import info.touret.bookstore.reactive.spring.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@Service
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;
    private WebClient webClient;
    private int timeoutInSec;

    public BookService(BookRepository bookRepository, WebClient webClient, @Value("${booknumbers.api.timeout_sec}") int timeoutInSec) {
        this.bookRepository = bookRepository;
        this.webClient = webClient;
        this.timeoutInSec = timeoutInSec;
    }

    public Mono<Book> findRandomBook() {
        return bookRepository.findAllIds().collectList().flatMap(list -> bookRepository.findById(list.get(new Random().nextInt(list.size()))));
    }

    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    public Mono<Map<String, Long>> count() {
        return bookRepository.count().map(aLong -> Map.of("books.count", aLong));
    }

    public Mono<Book> create(Book book) {
        var isbnNumbersMono = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(IsbnNumbers.class);
        isbnNumbersMono.flatMap(isbnNumbers -> {
                    var newBook = new Book(book);
                    newBook.setIsbn10(isbnNumbers.getIsbn10());
                    newBook.setIsbn13(isbnNumbers.getIsbn13());
                    return Mono.just(newBook);
                }).timeout(Duration.ofSeconds(timeoutInSec))
                .onErrorResume(TimeoutException.class, e -> fallBackPersistBook(book));
        return bookRepository.save(book);
    }

    private Mono<? extends Book> fallBackPersistBook(Book book) {
        try (var out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")) {
            var objectMapper = new ObjectMapper();
            var bookJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
            out.println(bookJson);
        } catch (FileNotFoundException | JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException("Cannot serialize data");
        }
        throw new ApiCallTimeoutException("Numbers not accessible");
    }


    public Mono<Book> update(Book book) {
        return bookRepository.save(book);
    }

    public Mono<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Mono<Void> delete(Long id) {
        return bookRepository.findById(id).flatMap(book -> bookRepository.delete(book));
    }
}
