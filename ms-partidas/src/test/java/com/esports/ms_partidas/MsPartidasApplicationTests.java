package com.esports.ms_partidas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class MsPartidasApplicationTests {

	@MockitoBean
	private WebClient webClient;

	@Test
	void contextLoads() {
	}

}
