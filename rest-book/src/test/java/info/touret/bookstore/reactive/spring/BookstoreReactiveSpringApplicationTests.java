package info.touret.bookstore.reactive.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookstoreReactiveSpringTestApplication.class)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
class BookstoreReactiveSpringApplicationTests {

	@Test
	void contextLoads() {
	}

}
