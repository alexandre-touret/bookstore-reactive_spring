package info.touret.bookstore.reactive.spring.service;

import info.touret.bookstore.reactive.spring.entity.Book;
import info.touret.bookstore.reactive.spring.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<Book> findRandomBook() {
        return bookRepository.findById(100L);
    }

    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    public Mono<Map<String, Long>> count() {
        return bookRepository.count().map(aLong -> Map.of("books.count", aLong));
    }

    public Mono<Book> create(Book book) {
        return bookRepository.save(book);
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
