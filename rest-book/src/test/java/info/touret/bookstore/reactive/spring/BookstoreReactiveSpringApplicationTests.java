package info.touret.bookstore.reactive.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTestConfiguration.class)
class BookstoreReactiveSpringApplicationTests {

	@Test
	void contextLoads() {
	}

}
