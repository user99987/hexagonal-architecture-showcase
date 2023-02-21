package com.cp.ecommerce.adapter.persistence.order.entity;

import com.cp.ecommerce.domain.order.Order;

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

}
