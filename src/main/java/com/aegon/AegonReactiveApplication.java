package com.aegon;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RequiredArgsConstructor
public class AegonReactiveApplication implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(AegonReactiveApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		final Mono<Customer> save = customerRepository.save(new Customer(null, "Kuba", "Bielawski"));
		save.block();
	}
}
