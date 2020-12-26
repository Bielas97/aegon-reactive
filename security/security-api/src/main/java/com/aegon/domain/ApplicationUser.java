package com.aegon.domain;

import java.util.Set;
import lang.Email;

public interface ApplicationUser {

	ApplicationUserId getId();

	ApplicationUsername getUsername();

	Email getEmail();

	Set<ApplicationUserRole> getRoles();

}
