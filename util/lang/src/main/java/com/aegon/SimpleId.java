package com.aegon;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class SimpleId<T> implements Id<T>, Serializable {

	private final T internal;

	public SimpleId(T internal) {
		this.internal = Preconditions.requireNonNull(internal);
	}

	@Override
	public T getInternal() {
		return internal;
	}

}
