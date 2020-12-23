package com.aegon.domain;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationUser {

	private ApplicationUserId id;

	private String username;

	private String encodedPassword;

	private String email;

	private Set<ApplicationUserRole> roles;

}
