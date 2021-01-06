package com.aegon;

public interface DomainObject<ID extends SimpleId> {

	ID getId();

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();
}
