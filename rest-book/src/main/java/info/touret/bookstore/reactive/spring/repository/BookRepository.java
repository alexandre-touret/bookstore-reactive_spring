package info.touret.bookstore.reactive.spring.repository;

import info.touret.bookstore.reactive.spring.entity.Book;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

    @Query(value = "select b.id  from Book b")
    Flux<Long> findAllIds();
}
