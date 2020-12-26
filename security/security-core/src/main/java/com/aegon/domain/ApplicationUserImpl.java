package com.aegon.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lang.Email;
import lang.Preconditions;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ApplicationUserImpl implements ApplicationUser {

	private ApplicationUserId id;

	private final ApplicationUsername username;

	private final Email email;

	private final Set<ApplicationUserRole> roles;

	@Getter
	private final ApplicationUserPassword password;

	public ApplicationUserImpl(ApplicationUserId id,
			ApplicationUsername username,
			Email email,
			ApplicationUserPassword password,
			Set<ApplicationUserRole> roles) {
		this.id = id;
		this.username = Preconditions.requireNonNull(username);
		this.email = Preconditions.requireNonNull(email);
		this.roles = Preconditions.requireNonEmpty(roles);
		this.password = password;
	}

	public ApplicationUserImpl(ApplicationUsername username,
			Email email,
			ApplicationUserPassword password,
			Set<ApplicationUserRole> roles) {
		this(null, username, email, password ,roles);
	}

	public ApplicationUserImpl(ApplicationUsername username,
			Email email,
			Set<ApplicationUserRole> roles) {
		this(null, username, email, null, roles);
	}

	@Override
	public ApplicationUserId getId() {
		return id;
	}

	@Override
	public ApplicationUsername getUsername() {
		return username;
	}

	@Override
	public Email getEmail() {
		return email;
	}

	@Override
	public Set<ApplicationUserRole> getRoles() {
		return roles;
	}

	public UserDetails toUserDetails() {
		return new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return roles.stream()
						.map(authority -> new SimpleGrantedAuthority(authority.name()))
						.collect(Collectors.toList());
			}

			@Override
			public String getPassword() {
				return password.getInternal();
			}

			@Override
			public String getUsername() {
				return username.getInternal();
			}

			@Override
			public boolean isAccountNonExpired() {
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return false;
			}

			@Override
			public boolean isEnabled() {
				return false;
			}
		};
	}
}
