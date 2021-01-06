package com.aegon.domain;

import com.aegon.util.lang.DomainObject;
import com.aegon.util.lang.Email;
import java.util.Set;

public interface ApplicationUser extends DomainObject<ApplicationUserId> {

	ApplicationUsername getUsername();

	Email getEmail();

	Set<ApplicationUserRole> getRoles();

}
