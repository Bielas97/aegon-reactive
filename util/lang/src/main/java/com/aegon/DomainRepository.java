package com.aegon;

import reactor.core.publisher.Mono;

public interface DomainRepository<ID extends SimpleId, OBJECT extends DomainObject<ID>> {

	Mono<OBJECT> findById(ID id);

	Mono<OBJECT> save(OBJECT object);

	Mono<ID> delete(ID objectId);

	Mono<OBJECT> update(OBJECT updated);

}
