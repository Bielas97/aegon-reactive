package com.aegon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class CustomerRoutes {

	@Bean
	public RouterFunction<ServerResponse> route(CustomerHandler customerHandler) {
		return RouterFunctions.route(GET("/customers"), customerHandler::getAllCustomers);
	}

}
