package com.aegon.rest;

import com.aegon.domain.ApplicationUserPassword;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequestDTO {

	public String password;

	public ApplicationUserPassword toDomain() {
		return ApplicationUserPassword.valueOf(password);
	}

}
