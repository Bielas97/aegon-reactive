package com.aegon.rest;

import com.aegon.Email;
import com.aegon.domain.ApplicationUserImpl;
import com.aegon.domain.ApplicationUserPassword;
import com.aegon.domain.ApplicationUserRole;
import com.aegon.domain.ApplicationUsername;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	public String username;

	public String email;

	public String password;

	public List<ApplicationUserRole> roles;

	public ApplicationUserImpl toDomain() {
		return ApplicationUserImpl.withoutId(ApplicationUsername.valueOf(username),
				Email.valueOf(email),
				ApplicationUserPassword.valueOf(password),
				new HashSet<>(roles));
	}
}
