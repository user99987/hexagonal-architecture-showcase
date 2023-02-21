package com.cp.ecommerce.adapter.web.order;

import java.util.Optional;

import com.cp.ecommerce.adapter.common.utils.OrderBuilder;
import com.cp.ecommerce.adapter.web.order.mapper.OrderWebMapper;
import com.cp.ecommerce.adapter.web.utils.OrderResourceBuilder;
import com.cp.ecommerce.domain.order.usecase.ManageOrderUseCase;
import com.cp.ecommerce.domain.order.usecase.PlaceOrderUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import jakarta.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.cp.ecommerce.adapter.common.utils.OrderBuilder.TEST_ORDER_NUMBER;

/**
 * Test class checking order page controller's behavior and order page API response.
 */
@AutoConfigureMockMvc
@EnableWebMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private static final String ORDER_ENDPOINT = "/api/order";

    private transient MockMvc mockMvc;

    @InjectMocks
    private transient OrderController orderController;

    @Mock
    private transient PlaceOrderUseCase placeOrderUseCase;

    @Mock
    private transient ManageOrderUseCase manageOrderUseCase;

    @Mock
    private transient OrderWebMapper orderWebMapper;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

        given(orderWebMapper.mapToDomainObject(any())).willReturn(Optional.ofNullable(OrderBuilder.mockOrder()));
        this.mockMvc.perform(post(ORDER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(createJsonResource()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(placeOrderUseCase, atLeastOnce()).placeOrder(any());
    }

    @Test
    void shouldThrowMissingDataExceptionForEmptyOptional() {

        given(orderWebMapper.mapToDomainObject(any())).willReturn(Optional.empty());
        final Exception exception = assertThrows(
                ServletException.class,
                () -> this.mockMvc
                        .perform(post(ORDER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(createJsonResource())));

        verify(placeOrderUseCase, never()).placeOrder(null);
        verify(orderWebMapper, atMostOnce()).mapToDomainObject(any());
        assertTrue(exception.getMessage().contains("Order data is missing"));
    }

    @Test
    void shouldResponseWith404IfOrderDoesntExist() throws Exception {

        given(manageOrderUseCase.findOrder(any())).willReturn(null);
        this.mockMvc.perform(get(ORDER_ENDPOINT + "/" + TEST_ORDER_NUMBER)).andExpect(status().isNotFound());
    }

    @Test
    void shouldResponseWithExpectedOrder() throws Exception {

        given(manageOrderUseCase.findOrder(TEST_ORDER_NUMBER)).willReturn(OrderBuilder.mockOrder());
        this.mockMvc.perform(get(ORDER_ENDPOINT + "/" + TEST_ORDER_NUMBER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNumber").value(TEST_ORDER_NUMBER));
    }

    private String createJsonResource() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(OrderResourceBuilder.mockOrderResource());
    }

}
