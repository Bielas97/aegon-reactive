package com.aegon.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ApplicationUserDetails implements UserDetails {

	private String username;

	private String password;

	private Set<ApplicationUserRole> roles;

	private ApplicationUserDetails(String username, String password, Set<ApplicationUserRole> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public static ApplicationUserDetails from(ApplicationUser user) {
		return new ApplicationUserDetails(user.getUsername(), user.getEncodedPassword(), user.getRoles());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.name()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
}
