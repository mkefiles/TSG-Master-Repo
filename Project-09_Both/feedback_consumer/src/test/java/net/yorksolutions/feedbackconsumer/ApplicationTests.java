package net.yorksolutions.feedbackconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.bootstrap-servers=localhost:12345"
})

class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
