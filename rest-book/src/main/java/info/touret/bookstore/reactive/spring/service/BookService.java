package info.touret.bookstore.reactive.spring.service;

import info.touret.bookstore.reactive.spring.entity.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    public Book findRandomBook() {
        return new Book();
    }
}
