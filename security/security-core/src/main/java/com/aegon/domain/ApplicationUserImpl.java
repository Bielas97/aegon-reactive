package com.aegon.domain;

import com.aegon.Email;
import com.aegon.Preconditions;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode
public class ApplicationUserImpl implements ApplicationUser {

	private final ApplicationUserId id;

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

	public static ApplicationUserImpl withoutId(ApplicationUsername username,
			Email email,
			ApplicationUserPassword password,
			Set<ApplicationUserRole> roles) {
		return new ApplicationUserImpl(null, username, email, password, roles);
	}

	public static ApplicationUserImpl withoutIdAndPassword(ApplicationUsername username,
			Email email,
			Set<ApplicationUserRole> roles) {
		return new ApplicationUserImpl(null, username, email, null, roles);
	}

	public static ApplicationUserImpl from(MongoUserDocument userDocument) {
		return new ApplicationUserImpl(ApplicationUserId.valueOf(userDocument.getId()),
				ApplicationUsername.valueOf(userDocument.getUsername()),
				Email.valueOf(userDocument.getEmail()),
				ApplicationUserPassword.valueOf(userDocument.getPassword()),
				userDocument.getRoles().stream().map(MongoRoleDocument::getName).collect(Collectors.toSet()));
	}

	public ApplicationUserImpl withNewPassword(ApplicationUserPassword newPassword) {
		return new ApplicationUserImpl(id, username, email, newPassword, roles);
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

	public Set<MongoRoleDocument> getMongoRoles() {
		return roles.stream()
				.map(MongoRoleDocument::from)
				.collect(Collectors.toSet());
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
