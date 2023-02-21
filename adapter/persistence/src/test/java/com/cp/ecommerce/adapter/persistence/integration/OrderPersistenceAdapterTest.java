package com.cp.ecommerce.adapter.persistence.integration;

import java.util.Date;

import javax.sql.DataSource;

import com.cp.ecommerce.adapter.common.utils.OrderBuilder;
import com.cp.ecommerce.adapter.persistence.configuration.PersistenceConfiguration;
import com.cp.ecommerce.adapter.persistence.order.SaveOrderAdapter;
import com.cp.ecommerce.adapter.persistence.order.entity.OrderEntity;
import com.cp.ecommerce.adapter.persistence.order.entity.OrderEntityRepository;
import com.cp.ecommerce.adapter.persistence.order.mapper.OrderPersistenceMapper;
import com.cp.ecommerce.adapter.persistence.utils.CustomerEntityBuilder;
import com.cp.ecommerce.domain.order.Order;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import static com.cp.ecommerce.adapter.common.constant.CacheConstants.ORDER_CACHE_NAME;
import static com.cp.ecommerce.adapter.common.utils.OrderBuilder.TEST_ORDER_NUMBER;
import static com.cp.ecommerce.adapter.common.utils.OrderBuilder.TEST_REMARKS;

/**
 * Integration test for order - save to db and create cache test.
 */
@DataJpaTest
@ActiveProfiles("persistence-h2-in-memory")
@ContextConfiguration(classes = PersistenceConfiguration.class)
class OrderPersistenceAdapterTest {

    @Autowired
    public transient EntityManager entityManager;
    @Autowired
    transient DataSource dataSource;
    @Autowired
    transient JdbcTemplate jdbcTemplate;
    @Autowired
    transient OrderPersistenceMapper orderPersistenceMapper;

    @Autowired
    transient OrderEntityRepository orderEntityRepository;

    @Autowired
    transient CacheManager cacheManager;

    @Autowired
    transient SaveOrderAdapter adapter;

    @Test
    void springContextShouldLoad(final ApplicationContext context) {
        assertThat(context).isNotNull();
    }

    @Test
    void injectedComponentsShouldNotBeNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    void shouldHaveAutowiredComponents() {
        assertThat(adapter).isNotNull();
        assertThat(orderPersistenceMapper).isNotNull();
        assertThat(orderEntityRepository).isNotNull();
    }

    @Test
    @DirtiesContext
    void shouldSaveOrderDirectAndCreateCache() {

        final OrderEntity entity = OrderEntity.builder()
                .remarks(TEST_REMARKS)
                .orderNumber(TEST_ORDER_NUMBER)
                .created(new Date())
                .customer(CustomerEntityBuilder.mockContactEntity())
                .build();

        orderEntityRepository.save(entity);
        final OrderEntity result = orderEntityRepository.getOrderEntityByOrderNumber(TEST_ORDER_NUMBER);
        final OrderEntity cachedOrder = cacheManager.getCache(ORDER_CACHE_NAME).get(TEST_ORDER_NUMBER, OrderEntity.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRemarks()).isEqualTo(TEST_REMARKS);
        assertThat(result.getOrderNumber()).isEqualTo(TEST_ORDER_NUMBER);
        assertThat(result.getCustomer()).isNotNull();
        assertThat(cachedOrder).isEqualTo(result);
    }

    @Test
    @DirtiesContext
    void shouldSaveOrderFromAdapter() {

        final Order order = OrderBuilder.mockOrder();
        final Order result = adapter.save(order);

        assertThat(result).isNotNull();
        assertThat(result.getOrderNumber()).isEqualTo(TEST_ORDER_NUMBER);
    }

}
