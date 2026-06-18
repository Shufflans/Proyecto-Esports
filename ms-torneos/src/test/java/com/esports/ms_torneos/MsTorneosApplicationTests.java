package com.esports.ms_torneos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class MsTorneosApplicationTests {

	@MockitoBean
	private WebClient webClient;

	@Test
	void contextLoads() {
	}

}
