package com.aegon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class CustomerRoutesConfig {

	@Bean
	public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler) {
		return RouterFunctions.route(GET("/customers"), customerHandler::getAllCustomers);
	}

}
