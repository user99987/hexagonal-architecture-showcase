package com.cp.ecommerce.adapter.common.mapping;

import java.util.Optional;

/**
 * Interface representing web mapper.
 *
 * @param <D> domain object
 * @param <R> resource object
 */
public interface WebMapper<D, R> {

    Optional<D> mapToDomainObject(R resource);

    Optional<R> mapToResource(D domainObject);

}
