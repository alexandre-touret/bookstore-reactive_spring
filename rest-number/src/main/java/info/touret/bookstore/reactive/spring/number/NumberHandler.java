package info.touret.bookstore.reactive.spring.number;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class NumberHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumberHandler.class);
    @Value("${number.separator:false}")
    private boolean separator;

    @Value("${time.to.sleep:15}")
    private int timeToSleep;


    public Mono<ServerResponse> findNumber(ServerRequest serverRequest) {
        LOGGER.info("Generating book numbers, sleeping {} msec", timeToSleep);
        try {
            if (timeToSleep != 0)
                TimeUnit.MILLISECONDS.sleep(timeToSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Faker faker = new Faker();
        BookNumbers bookNumbers = new BookNumbers();
        bookNumbers.setIsbn10(faker.code().isbn10(separator));
        bookNumbers.setIsbn13(faker.code().isbn13(separator));
        bookNumbers.setAsin(faker.code().asin());
        bookNumbers.setEan8(faker.code().ean8());
        bookNumbers.setEan13(faker.code().ean13());
        bookNumbers.setGenerationDate(Instant.now());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookNumbers), BookNumbers.class);
    }
}
