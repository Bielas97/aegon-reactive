package com.aegon;

import com.aegon.domain.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AegonReactiveApplication implements CommandLineRunner {

	private final ApplicationUserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AegonReactiveApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//		final ApplicationUser user = new ApplicationUserImpl(ApplicationUsername.valueOf("bielas"),
		//				Email.valueOf("kuba@kuba.pl"),
		//				ApplicationUserPassword.valueOf("mypassword"),
		//				Set.of(ApplicationUserRole.ROLE_APP_ADMIN, ApplicationUserRole.ROLE_APP_VIEWER));
		//		final Mono<ApplicationUser> save = userRepository.save(user);
		//		save.subscribe();
	}
}
