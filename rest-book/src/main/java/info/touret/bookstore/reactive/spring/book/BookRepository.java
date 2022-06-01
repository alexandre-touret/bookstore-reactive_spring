package info.touret.bookstore.reactive.spring.book;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

    @Query(value = "select b.id  from Book b")
    Flux<Long> findAllIds();
}
