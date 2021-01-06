package com.aegon.domain;

import com.aegon.DomainObject;
import java.util.Set;
import com.aegon.Email;

public interface ApplicationUser extends DomainObject<ApplicationUserId> {

	ApplicationUsername getUsername();

	Email getEmail();

	Set<ApplicationUserRole> getRoles();

}
