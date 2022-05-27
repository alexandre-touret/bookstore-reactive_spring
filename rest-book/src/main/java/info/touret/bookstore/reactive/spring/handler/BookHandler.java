package info.touret.bookstore.reactive.spring.handler;

import info.touret.bookstore.reactive.spring.entity.Book;
import info.touret.bookstore.reactive.spring.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BookHandler {

    private BookService bookService;

    public BookHandler(BookService bookService) {
        this.bookService = bookService;
    }

    public Mono<ServerResponse> random(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(bookService.findRandomBook(), Book.class));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookService.findAll(), Book.class);
    }

    public Mono<ServerResponse> count(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookService.count(), Book.class);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Book.class)
                .flatMap(book -> bookService.create(book))
                .flatMap(book -> ServerResponse.created(serverRequest.uriBuilder().path(book.getId().toString()).build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(serverRequest.uriBuilder().path(book.getId().toString()).build())));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Book.class)
                .flatMap(bookService::update)
                .flatMap(book -> ServerResponse
                        .accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(book)));
    }

    public Mono<ServerResponse> findBook(ServerRequest serverRequest) {
        return bookService.findBookById(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(book -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(book)));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return bookService.delete(Long.valueOf(serverRequest.pathVariable("id")))
                .flatMap(book -> ServerResponse.noContent().build());
    }
}
