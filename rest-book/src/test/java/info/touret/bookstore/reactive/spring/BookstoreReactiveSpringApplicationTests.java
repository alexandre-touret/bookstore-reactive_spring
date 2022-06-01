package info.touret.bookstore.reactive.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookstoreReactiveSpringTestApplication.class)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
class BookstoreReactiveSpringApplicationTests {

	@MockBean
	private WebClient webClient;

	@Test
	void contextLoads() {
	}

}
