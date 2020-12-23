package com.aegon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

	private final CustomerRepository customerRepository;

	public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
		final Flux<Customer> all = customerRepository.findAll();
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(all, Customer.class));

	}

}
