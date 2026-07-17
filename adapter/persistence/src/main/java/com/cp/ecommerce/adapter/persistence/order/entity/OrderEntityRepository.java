package com.cp.ecommerce.adapter.persistence.order.entity;

import com.cp.ecommerce.domain.order.Order;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.cp.ecommerce.adapter.common.constant.CacheConstants.ORDER_CACHE_NAME;

/**
 * Class at the persistence layer representing {@link Order} database repository.
 */
@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    @Cacheable(cacheNames = ORDER_CACHE_NAME, condition = "#p0 != null", unless = "#result == null")
    OrderEntity getOrderEntityByOrderNumber(String orderNumber);

    /**
     * Overridden to keep the cache entry for the saved entity's order number in sync on save. Without this, a cached entry
     * could go stale if an order number gets reused once {@code SEQ_ORDER_NUMBER} cycles back to its starting value.
     *
     * @param entity entity being saved.
     * @param <S> entity type.
     * @return saved entity.
     */
    @Override
    @CachePut(cacheNames = ORDER_CACHE_NAME, key = "#p0.orderNumber", condition = "#p0.orderNumber != null")
    <S extends OrderEntity> S save(S entity);

}
